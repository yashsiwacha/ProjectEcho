package com.projectecho.evidence.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EvidenceLineageConstraintIntegrationTest extends IntegrationTestBase {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID validLineageId;

    @BeforeEach
    void setUp() {
        validLineageId = UUID.randomUUID();
        // Setup: Insert a valid lineage root to satisfy the foreign key for version tests.
        // We use JdbcTemplate throughout to explicitly bypass JPA mapping and domain rules.
        jdbcTemplate.update(
                "INSERT INTO evidence_lineage (id, person_id, capability_id, version, created_at) VALUES (?, ?, ?, ?, ?)",
                validLineageId, UUID.randomUUID(), UUID.randomUUID(), 1, java.sql.Timestamp.from(Instant.now())
        );
    }

    @Test
    void givenInvalidConfidence_whenInserted_thenThrowsDataIntegrityViolationException() {
        // Arrange
        UUID versionId = UUID.randomUUID();

        // Act & Assert
        // We use direct SQL to bypass the Domain Model (which would otherwise reject confidence > 1.0).
        // This validates that the DB strictly enforces the CHECK(confidence >= 0.0 AND confidence <= 1.0) invariant.
        assertThatThrownBy(() -> {
            jdbcTemplate.update(
                    "INSERT INTO evidence_version (id, lineage_id, sequence_number, provenance, confidence, artifact_url, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    versionId, validLineageId, 1, "SYSTEM_INGESTION", 1.5, "url", java.sql.Timestamp.from(Instant.now())
            );
        }).isInstanceOf(DataIntegrityViolationException.class)
          .hasMessageContaining("chk_confidence_bounds"); // Verifies the exact constraint
    }

    @Test
    void givenZeroSequenceNumber_whenInserted_thenThrowsDataIntegrityViolationException() {
        // Arrange
        UUID versionId = UUID.randomUUID();

        // Act & Assert
        // Bypassing domain logic to verify CHECK(sequence_number > 0)
        assertThatThrownBy(() -> {
            jdbcTemplate.update(
                    "INSERT INTO evidence_version (id, lineage_id, sequence_number, provenance, confidence, artifact_url, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    versionId, validLineageId, 0, "SYSTEM_INGESTION", 0.9, "url", java.sql.Timestamp.from(Instant.now())
            );
        }).isInstanceOf(DataIntegrityViolationException.class)
          .hasMessageContaining("chk_sequence_positive");
    }

    @Test
    void givenDuplicateSequenceNumber_whenInserted_thenThrowsDuplicateKeyException() {
        // Arrange
        UUID versionId1 = UUID.randomUUID();
        UUID versionId2 = UUID.randomUUID();

        jdbcTemplate.update(
                "INSERT INTO evidence_version (id, lineage_id, sequence_number, provenance, confidence, artifact_url, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                versionId1, validLineageId, 1, "SYSTEM_INGESTION", 0.9, "url", java.sql.Timestamp.from(Instant.now())
        );

        // Act & Assert
        // Validates UNIQUE(lineage_id, sequence_number) preventing branching history
        assertThatThrownBy(() -> {
            jdbcTemplate.update(
                    "INSERT INTO evidence_version (id, lineage_id, sequence_number, provenance, confidence, artifact_url, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    versionId2, validLineageId, 1, "SYSTEM_INGESTION", 0.9, "url", java.sql.Timestamp.from(Instant.now())
            );
        }).isInstanceOf(DuplicateKeyException.class)
          .hasMessageContaining("uq_lineage_sequence");
    }

    @Test
    void givenInvalidForeignLineageId_whenInserted_thenThrowsDataIntegrityViolationException() {
        // Arrange
        UUID versionId = UUID.randomUUID();
        UUID nonExistentLineageId = UUID.randomUUID();

        // Act & Assert
        // Validates FOREIGN KEY (lineage_id) REFERENCES evidence_lineage(id)
        assertThatThrownBy(() -> {
            jdbcTemplate.update(
                    "INSERT INTO evidence_version (id, lineage_id, sequence_number, provenance, confidence, artifact_url, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    versionId, nonExistentLineageId, 1, "SYSTEM_INGESTION", 0.9, "url", java.sql.Timestamp.from(Instant.now())
            );
        }).isInstanceOf(DataIntegrityViolationException.class)
          .hasMessageContaining("fk_evidence_version_lineage");
    }

    @Test
    void givenNullMandatoryColumn_whenInserted_thenThrowsDataIntegrityViolationException() {
        // Arrange
        UUID versionId = UUID.randomUUID();

        // Act & Assert
        // Validates NOT NULL constraint on provenance
        assertThatThrownBy(() -> {
            jdbcTemplate.update(
                    "INSERT INTO evidence_version (id, lineage_id, sequence_number, provenance, confidence, artifact_url, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    versionId, validLineageId, 1, null, 0.9, "url", java.sql.Timestamp.from(Instant.now())
            );
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void givenExistingLineageWithVersions_whenLineageDeleted_thenVersionsAreCascaded() {
        // Arrange
        UUID versionId = UUID.randomUUID();
        jdbcTemplate.update(
                "INSERT INTO evidence_version (id, lineage_id, sequence_number, provenance, confidence, artifact_url, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                versionId, validLineageId, 1, "SYSTEM_INGESTION", 0.9, "url", java.sql.Timestamp.from(Instant.now())
        );
        
        Integer countBefore = jdbcTemplate.queryForObject("SELECT count(*) FROM evidence_version WHERE lineage_id = ?", Integer.class, validLineageId);
        assertThat(countBefore).isEqualTo(1);

        // Act
        // The repository doesn't support deletion, so we test ON DELETE CASCADE manually via SQL.
        // This validates the structural safety net in PostgreSQL.
        jdbcTemplate.update("DELETE FROM evidence_lineage WHERE id = ?", validLineageId);

        // Assert
        Integer countAfter = jdbcTemplate.queryForObject("SELECT count(*) FROM evidence_version WHERE lineage_id = ?", Integer.class, validLineageId);
        assertThat(countAfter).isEqualTo(0);
    }
}
