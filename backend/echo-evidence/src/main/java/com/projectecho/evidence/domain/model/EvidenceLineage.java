package com.projectecho.evidence.domain.model;

import com.projectecho.common.event.DomainEvent;
import com.projectecho.common.exception.ValidationException;
import com.projectecho.common.valueobject.Identifier;
import com.projectecho.common.valueobject.Timestamp;
import com.projectecho.evidence.domain.event.EvidenceLineageAppendedEvent;
import com.projectecho.evidence.domain.event.EvidenceLineageCreatedEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate Root for the Evidence domain. Represents the entire historical timeline of proof for a
 * capability.
 */
public class EvidenceLineage {

  private final Identifier id;
  private final Identifier personId;
  private final Identifier capabilityId;
  private int version; // Optimistic locking
  private final Timestamp createdAt;

  private final List<EvidenceVersion> versions = new ArrayList<>();
  private final List<DomainEvent> domainEvents = new ArrayList<>();

  private EvidenceLineage(
      Identifier id,
      Identifier personId,
      Identifier capabilityId,
      int version,
      Timestamp createdAt) {
    this.id = id;
    this.personId = personId;
    this.capabilityId = capabilityId;
    this.version = version;
    this.createdAt = createdAt;
  }

  /** Factory method to create a new EvidenceLineage. */
  public static EvidenceLineage create(
      Identifier personId,
      Identifier capabilityId,
      Provenance provenance,
      Confidence confidence,
      String artifactUrl) {
    if (personId == null) throw new ValidationException("personId cannot be null");
    if (capabilityId == null) throw new ValidationException("capabilityId cannot be null");

    Identifier lineageId = Identifier.generate();
    EvidenceLineage lineage =
        new EvidenceLineage(lineageId, personId, capabilityId, 1, Timestamp.now());

    lineage.registerEvent(new EvidenceLineageCreatedEvent(lineageId, personId, capabilityId));

    // Append the initial version
    lineage.append(provenance, confidence, artifactUrl);
    return lineage;
  }

  /** Appends a new version to this lineage. */
  public void append(Provenance provenance, Confidence confidence, String artifactUrl) {
    int nextSequence = versions.size() + 1;
    Identifier versionId = Identifier.generate();

    EvidenceVersion newVersion =
        new EvidenceVersion(
            versionId, nextSequence, provenance, confidence, artifactUrl, Timestamp.now());

    this.versions.add(newVersion);
    this.version++; // Increment aggregate version for optimistic locking

    // Don't emit appended event on the very first version, since Created event covers it.
    if (nextSequence > 1) {
      registerEvent(new EvidenceLineageAppendedEvent(this.id, versionId, nextSequence));
    }
  }

  /** Retrieves the most recent active version of evidence in this lineage. */
  public EvidenceVersion getCurrentVersion() {
    if (versions.isEmpty()) {
      return null;
    }
    return versions.get(versions.size() - 1);
  }

  public List<EvidenceVersion> getVersions() {
    return Collections.unmodifiableList(versions);
  }

  private void registerEvent(DomainEvent event) {
    this.domainEvents.add(event);
  }

  public List<DomainEvent> getDomainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }

  public void clearDomainEvents() {
    this.domainEvents.clear();
  }

  public Identifier getId() {
    return id;
  }

  public Identifier getPersonId() {
    return personId;
  }

  public Identifier getCapabilityId() {
    return capabilityId;
  }

  public int getVersion() {
    return version;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }
}
