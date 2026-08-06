package com.projectecho.shared.events;

import java.util.Collection;

public interface DomainEventPublisher {
    void publish(IntegrationEvent event);

    void publishAll(Collection<IntegrationEvent> events);
}
