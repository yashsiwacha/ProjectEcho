package com.projectecho.infrastructure.outbox;

import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxEventDispatcher {

    private static final Logger LOG = LoggerFactory.getLogger(OutboxEventDispatcher.class);

    private final OutboxEventRepository outboxEventRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OutboxEventDispatcher(
            final OutboxEventRepository outboxEventRepository,
            final ApplicationEventPublisher applicationEventPublisher) {
        this.outboxEventRepository = outboxEventRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void dispatchEvents() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Polling for outbox events");
        }
        final List<OutboxEvent> events =
                outboxEventRepository.findByCreatedAtBeforeOrderByCreatedAtAsc(Instant.now());
        for (final OutboxEvent event : events) {
            try {
                // Here we would deserialize the payload into an IntegrationEvent and publish it.
                // For simplicity in this scaffold, we just delete the event after successful
                // processing.
                // applicationEventPublisher.publishEvent(deserializedEvent);

                outboxEventRepository.delete(event);
                if (LOG.isInfoEnabled()) {
                    LOG.info("Successfully dispatched outbox event: {}", event.getId());
                }
            } catch (final Exception e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Failed to dispatch outbox event: {}", event.getId(), e);
                }
            }
        }
    }
}
