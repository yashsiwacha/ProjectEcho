package com.projectecho.shared.events;

import java.time.Instant;
import java.util.UUID;

public record MissionCreatedEvent(
        UUID eventId,
        int eventVersion,
        UUID correlationId,
        UUID causationId,
        Instant timestamp,
        UUID aggregateId,
        String title)
        implements IntegrationEvent {}
