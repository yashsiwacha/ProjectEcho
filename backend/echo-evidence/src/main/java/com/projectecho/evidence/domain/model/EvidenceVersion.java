package com.projectecho.evidence.domain.model;

import com.projectecho.common.exception.ValidationException;
import com.projectecho.common.valueobject.Identifier;
import com.projectecho.common.valueobject.Timestamp;

/** Immutable entity representing a single point-in-time proof within a lineage. */
public class EvidenceVersion {
  private final Identifier id;
  private final int sequenceNumber;
  private final Provenance provenance;
  private final Confidence confidence;
  private final String artifactUrl;
  private final Timestamp createdAt;

  // Package-private constructor. Should only be created by EvidenceLineage.
  EvidenceVersion(
      Identifier id,
      int sequenceNumber,
      Provenance provenance,
      Confidence confidence,
      String artifactUrl,
      Timestamp createdAt) {
    if (id == null) throw new ValidationException("id cannot be null");
    if (sequenceNumber < 1) throw new ValidationException("sequenceNumber must be >= 1");
    if (provenance == null) throw new ValidationException("provenance cannot be null");
    if (confidence == null) throw new ValidationException("confidence cannot be null");
    if (artifactUrl == null || artifactUrl.isBlank())
      throw new ValidationException("artifactUrl cannot be empty");
    if (createdAt == null) throw new ValidationException("createdAt cannot be null");

    this.id = id;
    this.sequenceNumber = sequenceNumber;
    this.provenance = provenance;
    this.confidence = confidence;
    this.artifactUrl = artifactUrl;
    this.createdAt = createdAt;
  }

  public Identifier getId() {
    return id;
  }

  public int getSequenceNumber() {
    return sequenceNumber;
  }

  public Provenance getProvenance() {
    return provenance;
  }

  public Confidence getConfidence() {
    return confidence;
  }

  public String getArtifactUrl() {
    return artifactUrl;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }
}
