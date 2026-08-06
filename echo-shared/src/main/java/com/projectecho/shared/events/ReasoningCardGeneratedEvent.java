package com.projectecho.shared.events;

import java.time.Instant;
import java.util.UUID;

public record ReasoningCardGeneratedEvent(
        UUID eventId,
        int eventVersion,
        UUID correlationId,
        UUID causationId,
        Instant timestamp,
        UUID aggregateId,
        UUID passportId,
        UUID missionId,
        int confidenceScore)
        implements IntegrationEvent {}
