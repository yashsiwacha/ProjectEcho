package com.projectecho.shared.events;

import java.time.Instant;
import java.util.UUID;

public record CareerPassportInitializedEvent(
        UUID eventId,
        int eventVersion,
        UUID correlationId,
        UUID causationId,
        Instant timestamp,
        UUID aggregateId,
        String name,
        String email,
        String jobTitle)
        implements IntegrationEvent {}
