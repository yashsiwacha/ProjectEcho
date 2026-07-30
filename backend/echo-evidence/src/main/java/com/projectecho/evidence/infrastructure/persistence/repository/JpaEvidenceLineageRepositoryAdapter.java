package com.projectecho.evidence.infrastructure.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectecho.common.event.DomainEvent;
import com.projectecho.common.valueobject.Identifier;
import com.projectecho.evidence.domain.model.EvidenceLineage;
import com.projectecho.evidence.domain.repository.EvidenceLineageRepository;
import com.projectecho.evidence.infrastructure.persistence.entity.EvidenceLineageEntity;
import com.projectecho.evidence.infrastructure.persistence.entity.OutboxMessageEntity;
import com.projectecho.evidence.infrastructure.persistence.mapper.EvidencePersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

@Component
public class JpaEvidenceLineageRepositoryAdapter implements EvidenceLineageRepository {

    private final SpringDataEvidenceLineageRepository repository;
    private final SpringDataOutboxMessageRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    public JpaEvidenceLineageRepositoryAdapter(
            SpringDataEvidenceLineageRepository repository,
            SpringDataOutboxMessageRepository outboxRepository,
            ObjectMapper objectMapper,
            EntityManager entityManager) {
        this.repository = repository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
        this.entityManager = entityManager;
    }

    @Override
    public EvidenceLineage save(EvidenceLineage lineage) {
        EvidenceLineageEntity entity = EvidencePersistenceMapper.toEntity(lineage);
        
        Optional<EvidenceLineageEntity> existing = repository.findById(lineage.getId().value());
        
        // If it's a new aggregate, save it. Otherwise we just update it.
        if (existing.isEmpty()) {
            entity = repository.save(entity);
        } else {
            EvidenceLineageEntity existingEntity = existing.get();
            int dbVersion = existingEntity.getVersion();
            long newAppends = lineage.getDomainEvents().stream()
                .filter(e -> e instanceof com.projectecho.evidence.domain.event.EvidenceLineageAppendedEvent)
                .count();
                
            int expectedOldVersion = lineage.getVersion() - (int) newAppends;
            
            if (dbVersion != expectedOldVersion) {
                throw new org.springframework.orm.ObjectOptimisticLockingFailureException(EvidenceLineage.class, lineage.getId().value());
            }
            
            // Execute the atomic version increment
            int updated = repository.incrementVersion(lineage.getId().value(), dbVersion);
            if (updated == 0) {
                throw new org.springframework.orm.ObjectOptimisticLockingFailureException(EvidenceLineage.class, lineage.getId().value());
            }
            
            // After incrementing, the persistence context is stale, but we only need to save the child entities.
            // Since we removed @Version, we can safely merge the updated state, setting version to the NEW version 
            // so Hibernate doesn't revert our update if it flushes the parent again.
            entity.setVersion(dbVersion + 1);
            
            // We must flush before saving children to ensure the parent update happens first if needed, 
            // although incrementVersion executes immediately.
            entity = repository.save(entity);
        }

        List<DomainEvent> events = lineage.getDomainEvents();
        for (DomainEvent event : events) {
            try {
                String payload = objectMapper.writeValueAsString(event);
                OutboxMessageEntity outboxMessage = new OutboxMessageEntity(
                        event.getEventId(),
                        lineage.getId().value(),
                        EvidenceLineage.class.getSimpleName(),
                        event.getClass().getSimpleName(),
                        event.getOccurredAt(),
                        payload
                );
                outboxRepository.save(outboxMessage);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize domain event for outbox", e);
            }
        }
        
        lineage.clearDomainEvents();
        return EvidencePersistenceMapper.toDomain(entity);
    }

    @Override
    public Optional<EvidenceLineage> findById(Identifier id) {
        return repository.findById(id.value()).map(EvidencePersistenceMapper::toDomain);
    }
}

