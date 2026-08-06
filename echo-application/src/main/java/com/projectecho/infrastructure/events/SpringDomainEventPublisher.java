package com.projectecho.infrastructure.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectecho.infrastructure.outbox.OutboxEvent;
import com.projectecho.infrastructure.outbox.OutboxEventRepository;
import com.projectecho.shared.events.DomainEventPublisher;
import com.projectecho.shared.events.IntegrationEvent;
import java.util.Collection;
import java.util.Objects;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public SpringDomainEventPublisher(
            final ApplicationEventPublisher applicationEventPublisher,
            final OutboxEventRepository outboxEventRepository,
            final ObjectMapper objectMapper) {
        this.applicationEventPublisher = Objects.requireNonNull(applicationEventPublisher);
        this.outboxEventRepository = Objects.requireNonNull(outboxEventRepository);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public void publish(final IntegrationEvent event) {
        if (event != null) {
            try {
                final String payload = objectMapper.writeValueAsString(event);
                final OutboxEvent outboxEvent =
                        new OutboxEvent(
                                event.eventId(),
                                event.getClass().getSimpleName(),
                                event.timestamp(),
                                payload);
                outboxEventRepository.save(outboxEvent);
            } catch (final JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize IntegrationEvent to outbox", e);
            }
        }
    }

    @Override
    public void publishAll(final Collection<IntegrationEvent> events) {
        if (events != null) {
            events.forEach(this::publish);
        }
    }
}
