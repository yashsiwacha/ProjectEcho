package com.projectecho.evidence.infrastructure.persistence;

import com.projectecho.evidence.domain.model.Confidence;
import com.projectecho.evidence.domain.model.EvidenceLineage;
import com.projectecho.evidence.domain.model.Provenance;
import com.projectecho.evidence.domain.repository.EvidenceLineageRepository;
import com.projectecho.evidence.infrastructure.persistence.builder.EvidenceLineageTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EvidenceLineageOutboxIntegrationTest extends IntegrationTestBase {

    @Autowired
    private EvidenceLineageRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private TransactionTemplate getRequiresNewTemplate() {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return template;
    }

    @Test
    void givenNewEvidenceLineage_whenSaved_thenExactlyOneOutboxMessageIsPersisted() {
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
        repository.save(lineage);
        flushAndClear();

        Integer eventCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM outbox_message WHERE aggregate_id = ?",
                Integer.class, lineage.getId().value()
        );
        assertThat(eventCount).isEqualTo(1);
    }

    @Test
    void givenExistingEvidenceLineage_whenVersionAppended_thenExpectedOutboxMessageIsPersisted() {
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
        repository.save(lineage);
        flushAndClear();

        EvidenceLineage loaded = repository.findById(lineage.getId()).orElseThrow();
        loaded.append(Provenance.PEER_VALIDATED, Confidence.of(0.95), "url-update");
        repository.save(loaded);
        flushAndClear();

        Integer eventCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM outbox_message WHERE aggregate_id = ?",
                Integer.class, lineage.getId().value()
        );
        assertThat(eventCount).isEqualTo(2); // 1 create, 1 append
    }

    @Test
    void givenAggregateSaveFails_whenRolledBack_thenAggregateAndOutboxAreAtomicallyRolledBack() {
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
        
        try {
            getRequiresNewTemplate().execute(status -> {
                repository.save(lineage);
                entityManager.flush();
                throw new RuntimeException("Simulated catastrophic failure");
            });
        } catch (RuntimeException e) {
            // Expected
        }

        Integer eventCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM outbox_message WHERE aggregate_id = ?",
                Integer.class, lineage.getId().value()
        );
        assertThat(eventCount).isEqualTo(0);
    }

    @Test
    void givenOptimisticLockFailure_whenRolledBack_thenOutboxMessagesAreNotPersisted() {
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
        repository.save(lineage);
        flushAndClear();

        EvidenceLineage tx1 = repository.findById(lineage.getId()).orElseThrow();
        EvidenceLineage tx2 = repository.findById(lineage.getId()).orElseThrow();

        tx1.append(Provenance.PEER_VALIDATED, Confidence.of(0.95), "url-1");
        repository.save(tx1);
        flushAndClear();

        tx2.append(Provenance.SYSTEM_INGESTION, Confidence.of(0.85), "url-2");
        assertThatThrownBy(() -> {
            repository.save(tx2);
            flushAndClear();
        }).isInstanceOf(ObjectOptimisticLockingFailureException.class);

        Integer eventCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM outbox_message WHERE aggregate_id = ?",
                Integer.class, lineage.getId().value()
        );
        assertThat(eventCount).isEqualTo(2); // Initial create and tx1 append. tx2 rolled back.
    }

    @Test
    void givenLineageSaved_whenOutboxQueried_thenPayloadCorrectlySerializesDomainEvent() {
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
        repository.save(lineage);
        flushAndClear();

        String payload = jdbcTemplate.queryForObject(
                "SELECT payload FROM outbox_message WHERE aggregate_id = ? LIMIT 1",
                String.class, lineage.getId().value()
        );
        
        assertThat(payload).isNotBlank();
        assertThat(payload).contains("eventId");
        assertThat(payload).contains("occurredAt");
    }

    @Test
    void givenLineageSaved_whenOutboxQueried_thenEventMetadataIsCorrect() {
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
        repository.save(lineage);
        flushAndClear();

        Map<String, Object> outboxRow = jdbcTemplate.queryForMap(
                "SELECT * FROM outbox_message WHERE aggregate_id = ? LIMIT 1",
                lineage.getId().value()
        );
        
        assertThat(outboxRow.get("id")).isNotNull();
        assertThat(outboxRow.get("aggregate_id")).isEqualTo(lineage.getId().value());
        assertThat(outboxRow.get("aggregate_type")).isEqualTo("EvidenceLineage");
        assertThat(outboxRow.get("type")).isEqualTo("EvidenceLineageCreatedEvent");
        assertThat(outboxRow.get("occurred_at")).isNotNull();
        assertThat(outboxRow.get("processed")).isEqualTo(false);
    }

    @Test
    void givenMultipleEvents_whenSaved_thenEventOrderingMatchesAggregateVersionOrdering() {
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
        lineage.append(Provenance.PEER_VALIDATED, Confidence.of(0.95), "url-2");
        lineage.append(Provenance.SYSTEM_INGESTION, Confidence.of(0.85), "url-3");
        
        repository.save(lineage);
        flushAndClear();

        List<Map<String, Object>> outboxRows = jdbcTemplate.queryForList(
                "SELECT * FROM outbox_message WHERE aggregate_id = ? ORDER BY occurred_at ASC",
                lineage.getId().value()
        );
        
        assertThat(outboxRows).hasSize(3);
        assertThat(outboxRows.get(0).get("type")).isEqualTo("EvidenceLineageCreatedEvent");
        assertThat(outboxRows.get(1).get("type")).isEqualTo("EvidenceLineageAppendedEvent");
        assertThat(outboxRows.get(2).get("type")).isEqualTo("EvidenceLineageAppendedEvent");
    }

    @Test
    void givenUnchangedAggregate_whenSaved_thenNoAdditionalOutboxEntriesAreProduced() {
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
        EvidenceLineage savedLineage = repository.save(lineage);
        flushAndClear();

        EvidenceLineage reloaded = repository.findById(savedLineage.getId()).orElseThrow();
        repository.save(reloaded); // No changes made
        flushAndClear();

        Integer eventCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM outbox_message WHERE aggregate_id = ?",
                Integer.class, lineage.getId().value()
        );
        assertThat(eventCount).isEqualTo(1); // Still only the initial create event
    }

    @Test
    void givenAggregateWithPendingEvents_whenSavedRepeatedly_thenPreviouslyEmittedEventsAreNotDuplicated() {
        EvidenceLineage lineage = new EvidenceLineageTestDataBuilder().build();
        EvidenceLineage savedLineage = repository.save(lineage); // Saves and clears pending events
        flushAndClear();

        repository.save(savedLineage); // Save the exact same aggregate instance again
        flushAndClear();

        Integer eventCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM outbox_message WHERE aggregate_id = ?",
                Integer.class, lineage.getId().value()
        );
        assertThat(eventCount).isEqualTo(1); // Ensures clearDomainEvents() worked
    }
}
