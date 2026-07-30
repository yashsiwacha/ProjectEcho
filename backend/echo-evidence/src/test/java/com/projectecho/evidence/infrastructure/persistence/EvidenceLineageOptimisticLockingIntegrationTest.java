package com.projectecho.evidence.infrastructure.persistence;

import com.projectecho.evidence.domain.model.Confidence;
import com.projectecho.evidence.domain.model.EvidenceLineage;
import com.projectecho.evidence.domain.model.Provenance;
import com.projectecho.evidence.domain.repository.EvidenceLineageRepository;
import com.projectecho.evidence.infrastructure.persistence.builder.EvidenceLineageTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EvidenceLineageOptimisticLockingIntegrationTest extends IntegrationTestBase {

    @Autowired
    private EvidenceLineageRepository repository;

    @Test
    void givenConcurrentModification_whenStaleAggregateSaved_thenThrowsOptimisticLockExceptionAndStateIsConsistent() {
        // Arrange
        EvidenceLineage initialLineage = new EvidenceLineageTestDataBuilder().build();
        repository.save(initialLineage);
        flushAndClear();

        // 1. Concurrent modification using two independently loaded aggregates
        // (Because the repository returns mapped domain objects, these are two distinct object instances)
        EvidenceLineage user1Lineage = repository.findById(initialLineage.getId()).orElseThrow();
        EvidenceLineage user2Lineage = repository.findById(initialLineage.getId()).orElseThrow();

        // 2. Saving the first aggregate succeeds.
        user1Lineage.append(Provenance.PEER_VALIDATED, Confidence.of(0.95), "url-user-1");
        repository.save(user1Lineage);
        flushAndClear();

        // 3. Saving the stale aggregate fails.
        user2Lineage.append(Provenance.SYSTEM_INGESTION, Confidence.of(0.85), "url-user-2");
        assertThatThrownBy(() -> {
            repository.save(user2Lineage);
            flushAndClear(); // Force the UPDATE to happen immediately
        }).isInstanceOf(ObjectOptimisticLockingFailureException.class);

        // Clear persistence context to ensure we read fresh state from DB
        entityManager.clear();
        EvidenceLineage latestLineage = repository.findById(initialLineage.getId()).orElseThrow();

        // 4. Aggregate version increments after successful save.
        assertThat(latestLineage.getVersion()).isGreaterThan(initialLineage.getVersion());

        // 5. No partial EvidenceVersion rows remain after rollback. Only User 1's append should exist.
        // 6. Repository state remains consistent after optimistic lock failure.
        assertThat(latestLineage.getVersions()).hasSize(2);
        
        // Verify that only one append operation is persisted
        assertThat(latestLineage.getCurrentVersion().getArtifactUrl()).isEqualTo("url-user-1");
        
        // Verify that sequence numbers remain contiguous and no branching history exists
        assertThat(latestLineage.getCurrentVersion().getSequenceNumber()).isEqualTo(2);
    }
}
