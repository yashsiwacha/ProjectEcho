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

@Component
public class JpaEvidenceLineageRepositoryAdapter implements EvidenceLineageRepository {

    private final SpringDataEvidenceLineageRepository repository;
    private final SpringDataOutboxMessageRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public JpaEvidenceLineageRepositoryAdapter(
            SpringDataEvidenceLineageRepository repository,
            SpringDataOutboxMessageRepository outboxRepository,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public EvidenceLineage save(EvidenceLineage lineage) {
        EvidenceLineageEntity entity = EvidencePersistenceMapper.toEntity(lineage);
        EvidenceLineageEntity savedEntity = repository.save(entity);

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
        return EvidencePersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<EvidenceLineage> findById(Identifier id) {
        return repository.findById(id.value()).map(EvidencePersistenceMapper::toDomain);
    }
}

