package com.projectecho.evidence.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.projectecho.common.valueobject.Identifier;
import com.projectecho.evidence.domain.model.Confidence;
import com.projectecho.evidence.domain.model.EvidenceLineage;
import com.projectecho.evidence.domain.model.EvidenceVersion;
import com.projectecho.evidence.domain.model.Provenance;
import com.projectecho.evidence.domain.repository.EvidenceLineageRepository;
import com.projectecho.evidence.infrastructure.persistence.builder.EvidenceLineageTestDataBuilder;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EvidenceLineageRepositoryIntegrationTest extends IntegrationTestBase {

  @Autowired private EvidenceLineageRepository repository;

  @Test
  void givenNewEvidenceLineage_whenSaved_thenItCanBeLoaded() {
    // Arrange
    EvidenceLineage newLineage = new EvidenceLineageTestDataBuilder().build();

    // Act
    repository.save(newLineage);
    flushAndClear(); // Force DB sync and L1 cache clear
    Optional<EvidenceLineage> loadedLineage = repository.findById(newLineage.getId());

    // Assert
    assertThat(loadedLineage).isPresent();
  }

  @Test
  void givenExistingEvidenceLineage_whenLoaded_thenAggregateIdentityIsPreserved() {
    // Arrange
    Identifier personId = generateIsolatedId();
    Identifier capabilityId = generateIsolatedId();
    EvidenceLineage originalLineage =
        new EvidenceLineageTestDataBuilder()
            .withPersonId(personId)
            .withCapabilityId(capabilityId)
            .build();
    repository.save(originalLineage);
    flushAndClear();

    // Act
    EvidenceLineage loadedLineage = repository.findById(originalLineage.getId()).orElseThrow();

    // Assert
    assertThat(loadedLineage.getId()).isEqualTo(originalLineage.getId());
    assertThat(loadedLineage.getPersonId()).isEqualTo(personId);
    assertThat(loadedLineage.getCapabilityId()).isEqualTo(capabilityId);
  }

  @Test
  void givenEvidenceLineageWithMultipleVersions_whenLoaded_thenReconstructedCorrectly() {
    // Arrange
    EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
    lineage.append(Provenance.PEER_VALIDATED, Confidence.of(0.95), "url-2");
    lineage.append(Provenance.SYSTEM_INGESTION, Confidence.of(0.99), "url-3");

    repository.save(lineage);
    flushAndClear();

    // Act
    EvidenceLineage loadedLineage = repository.findById(lineage.getId()).orElseThrow();

    // Assert
    List<EvidenceVersion> versions = loadedLineage.getVersions();
    assertThat(versions).hasSize(3);
  }

  @Test
  void givenNonExistentLineageId_whenLoaded_thenReturnsEmpty() {
    // Arrange
    Identifier nonExistentId = generateIsolatedId();

    // Act
    Optional<EvidenceLineage> result = repository.findById(nonExistentId);

    // Assert
    assertThat(result).isEmpty();
  }

  @Test
  void givenEvidenceLineageWithMultipleVersions_whenLoaded_thenVersionOrderingIsPreserved() {
    // Arrange
    EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
    lineage.append(Provenance.PEER_VALIDATED, Confidence.of(0.95), "url-2");
    lineage.append(Provenance.SYSTEM_INGESTION, Confidence.of(0.99), "url-3");

    repository.save(lineage);
    flushAndClear();

    // Act
    EvidenceLineage loadedLineage = repository.findById(lineage.getId()).orElseThrow();

    // Assert
    List<EvidenceVersion> versions = loadedLineage.getVersions();
    assertThat(versions.get(0).getSequenceNumber()).isEqualTo(1);
    assertThat(versions.get(1).getSequenceNumber()).isEqualTo(2);
    assertThat(versions.get(2).getSequenceNumber()).isEqualTo(3);

    // Latest version check
    assertThat(loadedLineage.getCurrentVersion().getSequenceNumber()).isEqualTo(3);
  }

  @Test
  void givenEvidenceLineage_whenSavedAndLoaded_thenValueObjectsArePreserved() {
    // Arrange
    Provenance testProvenance = Provenance.PEER_VALIDATED;
    Confidence testConfidence = Confidence.of(0.77);
    String testUrl = "https://example.com/artifact";

    EvidenceLineage lineage =
        new EvidenceLineageTestDataBuilder()
            .withProvenance(testProvenance)
            .withConfidence(testConfidence.value())
            .withArtifactUrl(testUrl)
            .build();

    repository.save(lineage);
    flushAndClear();

    // Act
    EvidenceLineage loadedLineage = repository.findById(lineage.getId()).orElseThrow();
    EvidenceVersion version = loadedLineage.getCurrentVersion();

    // Assert
    assertThat(version.getProvenance()).isEqualTo(testProvenance);
    assertThat(version.getConfidence().value()).isEqualTo(testConfidence.value());
    assertThat(version.getArtifactUrl()).isEqualTo(testUrl);
    assertThat(version.getCreatedAt()).isNotNull();
  }
}
