package com.projectecho.shared.events;

import java.time.Instant;
import java.util.UUID;

public record IntelligenceScoreCalculatedEvent(
        UUID eventId,
        int eventVersion,
        UUID correlationId,
        UUID causationId,
        Instant timestamp,
        UUID aggregateId,
        UUID passportId,
        int score)
        implements IntegrationEvent {}
