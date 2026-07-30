package com.projectecho.evidence.infrastructure.persistence;

import com.projectecho.evidence.domain.model.Confidence;
import com.projectecho.evidence.domain.model.EvidenceLineage;
import com.projectecho.evidence.domain.model.Provenance;
import com.projectecho.evidence.domain.repository.EvidenceLineageRepository;
import com.projectecho.evidence.infrastructure.persistence.builder.EvidenceLineageTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class EvidenceLineageTransactionIntegrationTest extends IntegrationTestBase {

    @Autowired
    private EvidenceLineageRepository repository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Creates a TransactionTemplate that forces a NEW transaction.
     * This allows us to test commit/rollback mechanics independently of the 
     * outer @DataJpaTest transaction boundary.
     */
    private TransactionTemplate getRequiresNewTemplate() {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return template;
    }

    @Test
    void givenNewAggregate_whenSaveFails_thenNoEvidenceVersionRowsRemain() {
        // Arrange
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();

        // Act
        try {
            getRequiresNewTemplate().execute(status -> {
                repository.save(lineage);
                entityManager.flush();
                // 1. Simulate failure during persistence rolls back the entire transaction
                throw new RuntimeException("Simulated catastrophic failure");
            });
        } catch (RuntimeException e) {
            // Expected
        }

        // Assert
        // 2. Aggregate save fails -> no EvidenceVersion rows remain.
        Integer versionCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM evidence_version WHERE lineage_id = ?",
                Integer.class,
                lineage.getId().value()
        );
        assertThat(versionCount).isEqualTo(0);
    }

    @Test
    void givenExistingAggregate_whenUpdateFails_thenRootVersionIsUnchangedAndStateRemainsConsistent() {
        // Arrange - Save initial state successfully
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
        getRequiresNewTemplate().execute(status -> {
            repository.save(lineage);
            return null;
        });
        
        int initialVersion = lineage.getVersion();

        // Act - Attempt update and fail
        try {
            getRequiresNewTemplate().execute(status -> {
                EvidenceLineage loaded = repository.findById(lineage.getId()).orElseThrow();
                loaded.append(Provenance.PEER_VALIDATED, Confidence.of(0.95), "url-update");
                repository.save(loaded);
                entityManager.flush();
                throw new RuntimeException("Simulated failure during update");
            });
        } catch (RuntimeException e) {
            // Expected
        }

        // Assert
        EvidenceLineage afterRollback = repository.findById(lineage.getId()).orElseThrow();
        
        // 3. Aggregate root version is unchanged after rollback.
        assertThat(afterRollback.getVersion()).isEqualTo(initialVersion);
        
        // 6. Repository state remains unchanged after rollback.
        assertThat(afterRollback.getVersions()).hasSize(1);
    }

    @Test
    void givenChildEntities_whenTransactionFails_thenChildEntitiesAreNotPartiallyCommitted() {
        // Arrange
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
        getRequiresNewTemplate().execute(status -> {
            repository.save(lineage);
            return null;
        });

        // Act - Attempt multiple appends and fail
        try {
            getRequiresNewTemplate().execute(status -> {
                EvidenceLineage loaded = repository.findById(lineage.getId()).orElseThrow();
                // Append 3 new versions
                loaded.append(Provenance.SYSTEM_INGESTION, Confidence.of(0.9), "url-1");
                loaded.append(Provenance.SYSTEM_INGESTION, Confidence.of(0.8), "url-2");
                loaded.append(Provenance.PEER_VALIDATED, Confidence.of(1.0), "url-3");
                repository.save(loaded);
                entityManager.flush();
                throw new RuntimeException("Simulated failure after processing child entities");
            });
        } catch (RuntimeException e) {
            // Expected
        }

        // Assert
        // 4. Child entities are not partially committed. (It's all or nothing)
        Integer totalVersions = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM evidence_version WHERE lineage_id = ?",
                Integer.class,
                lineage.getId().value()
        );
        assertThat(totalVersions).isEqualTo(1); // Only the initial create version should exist
    }

    @Test
    void givenMultipleAggregateOperations_whenOneFails_thenBothRollBackAtomically() {
        // Arrange
        EvidenceLineage lineage1 = new EvidenceLineageTestDataBuilder().build();
        EvidenceLineage lineage2 = new EvidenceLineageTestDataBuilder().build();

        // Act
        try {
            getRequiresNewTemplate().execute(status -> {
                repository.save(lineage1);
                entityManager.flush(); // Lineage 1 is flushed to DB successfully
                
                repository.save(lineage2);
                entityManager.flush();
                
                throw new RuntimeException("Simulated failure after multiple operations");
            });
        } catch (RuntimeException e) {
            // Expected
        }

        // Assert
        // 5. Multiple aggregate operations inside one transaction roll back atomically.
        Optional<EvidenceLineage> loaded1 = repository.findById(lineage1.getId());
        Optional<EvidenceLineage> loaded2 = repository.findById(lineage2.getId());
        
        assertThat(loaded1).isEmpty();
        assertThat(loaded2).isEmpty();
    }
}
