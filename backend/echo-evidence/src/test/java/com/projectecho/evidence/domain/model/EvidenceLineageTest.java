package com.projectecho.evidence.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.projectecho.common.exception.ValidationException;
import com.projectecho.common.valueobject.Identifier;
import com.projectecho.evidence.domain.event.EvidenceLineageAppendedEvent;
import com.projectecho.evidence.domain.event.EvidenceLineageCreatedEvent;
import org.junit.jupiter.api.Test;

class EvidenceLineageTest {

  @Test
  void shouldCreateLineageSuccessfully() {
    Identifier personId = Identifier.generate();
    Identifier capabilityId = Identifier.generate();
    Provenance provenance = Provenance.SYSTEM_INGESTION;
    Confidence confidence = Confidence.of(0.9);
    String url = "https://github.com/pulls/123";

    EvidenceLineage lineage =
        EvidenceLineage.create(personId, capabilityId, provenance, confidence, url);

    assertNotNull(lineage.getId());
    assertEquals(personId, lineage.getPersonId());
    assertEquals(capabilityId, lineage.getCapabilityId());
    assertNotNull(lineage.getCreatedAt());

    // Version starts at 2 (1 for creation, +1 for the first append inside create)
    assertEquals(2, lineage.getVersion());

    assertEquals(1, lineage.getVersions().size());
    EvidenceVersion current = lineage.getCurrentVersion();
    assertNotNull(current);
    assertEquals(1, current.getSequenceNumber());
    assertEquals(provenance, current.getProvenance());
    assertEquals(confidence, current.getConfidence());
    assertEquals(url, current.getArtifactUrl());

    // Verify creation event was registered (Appended event is NOT registered for seq=1)
    assertEquals(1, lineage.getDomainEvents().size());
    assertTrue(lineage.getDomainEvents().get(0) instanceof EvidenceLineageCreatedEvent);
  }

  @Test
  void shouldAppendToLineageSuccessfully() {
    EvidenceLineage lineage =
        EvidenceLineage.create(
            Identifier.generate(),
            Identifier.generate(),
            Provenance.SELF_REPORTED,
            Confidence.of(0.2),
            "oldUrl");

    lineage.clearDomainEvents();
    int initialVersion = lineage.getVersion();

    Provenance newProvenance = Provenance.PEER_VALIDATED;
    Confidence newConfidence = Confidence.of(1.0);
    String newUrl = "newUrl";

    lineage.append(newProvenance, newConfidence, newUrl);

    assertEquals(2, lineage.getVersions().size());
    assertEquals(initialVersion + 1, lineage.getVersion()); // Optimistic lock increments

    EvidenceVersion current = lineage.getCurrentVersion();
    assertEquals(2, current.getSequenceNumber());
    assertEquals(newProvenance, current.getProvenance());
    assertEquals(newConfidence, current.getConfidence());
    assertEquals(newUrl, current.getArtifactUrl());

    // Verify appended event was registered
    assertEquals(1, lineage.getDomainEvents().size());
    assertTrue(lineage.getDomainEvents().get(0) instanceof EvidenceLineageAppendedEvent);
    EvidenceLineageAppendedEvent event =
        (EvidenceLineageAppendedEvent) lineage.getDomainEvents().get(0);
    assertEquals(lineage.getId(), event.getLineageId());
    assertEquals(current.getId(), event.getNewVersionId());
    assertEquals(2, event.getSequenceNumber());
  }

  @Test
  void shouldThrowExceptionWhenAppendingInvalidData() {
    EvidenceLineage lineage =
        EvidenceLineage.create(
            Identifier.generate(),
            Identifier.generate(),
            Provenance.SELF_REPORTED,
            Confidence.of(0.2),
            "url");

    assertThrows(ValidationException.class, () -> lineage.append(null, Confidence.of(1.0), "url"));

    assertThrows(
        ValidationException.class, () -> lineage.append(Provenance.PEER_VALIDATED, null, "url"));

    assertThrows(
        ValidationException.class,
        () -> lineage.append(Provenance.PEER_VALIDATED, Confidence.of(1.0), ""));
  }
}
