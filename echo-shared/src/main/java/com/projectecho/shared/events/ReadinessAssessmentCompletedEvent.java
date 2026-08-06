package com.projectecho.shared.events;

import java.time.Instant;
import java.util.UUID;

public record ReadinessAssessmentCompletedEvent(
        UUID eventId,
        int eventVersion,
        UUID correlationId,
        UUID causationId,
        Instant timestamp,
        UUID aggregateId,
        UUID passportId,
        UUID missionId,
        boolean isEligible,
        int score,
        UUID graphId)
        implements IntegrationEvent {}
