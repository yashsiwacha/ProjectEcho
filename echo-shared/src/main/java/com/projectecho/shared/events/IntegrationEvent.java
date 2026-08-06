package com.projectecho.shared.events;

import java.time.Instant;
import java.util.UUID;

public interface IntegrationEvent {
    UUID eventId();

    int eventVersion();

    UUID correlationId();

    UUID causationId();

    Instant timestamp();

    UUID aggregateId();
}
