package com.projectecho.shared.events;

import java.time.Instant;
import java.util.UUID;

public record SkillRegisteredEvent(
        UUID eventId,
        int eventVersion,
        UUID correlationId,
        UUID causationId,
        Instant timestamp,
        UUID aggregateId,
        String name,
        String category,
        UUID parentSkillId)
        implements IntegrationEvent {}
