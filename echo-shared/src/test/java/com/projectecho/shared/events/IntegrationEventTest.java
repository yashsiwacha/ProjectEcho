package com.projectecho.shared.events;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class IntegrationEventTest {

    @Test
    void trustTierAssessedEventShouldStoreValues() {
        UUID eventId = UUID.randomUUID();
        int version = 1;
        UUID correlationId = UUID.randomUUID();
        UUID causationId = UUID.randomUUID();
        Instant timestamp = Instant.now();
        UUID aggregateId = UUID.randomUUID();
        UUID passportId = UUID.randomUUID();
        String tier = "HIGH";

        TrustTierAssessedEvent event =
                new TrustTierAssessedEvent(
                        eventId,
                        version,
                        correlationId,
                        causationId,
                        timestamp,
                        aggregateId,
                        passportId,
                        tier);

        assertEquals(eventId, event.eventId());
        assertEquals(version, event.eventVersion());
        assertEquals(correlationId, event.correlationId());
        assertEquals(causationId, event.causationId());
        assertEquals(timestamp, event.timestamp());
        assertEquals(aggregateId, event.aggregateId());
        assertEquals(passportId, event.passportId());
        assertEquals(tier, event.trustTier());
    }

    @Test
    void intelligenceScoreCalculatedEventShouldStoreValues() {
        UUID eventId = UUID.randomUUID();
        int version = 1;
        UUID correlationId = UUID.randomUUID();
        UUID causationId = UUID.randomUUID();
        Instant timestamp = Instant.now();
        UUID aggregateId = UUID.randomUUID();
        UUID passportId = UUID.randomUUID();
        int score = 85;

        IntelligenceScoreCalculatedEvent event =
                new IntelligenceScoreCalculatedEvent(
                        eventId,
                        version,
                        correlationId,
                        causationId,
                        timestamp,
                        aggregateId,
                        passportId,
                        score);

        assertEquals(eventId, event.eventId());
        assertEquals(score, event.score());
    }

    @Test
    void careerPassportInitializedEventShouldStoreValues() {
        UUID eventId = UUID.randomUUID();
        int version = 1;
        UUID correlationId = UUID.randomUUID();
        UUID causationId = UUID.randomUUID();
        Instant timestamp = Instant.now();
        UUID aggregateId = UUID.randomUUID();
        String name = "John Doe";
        String email = "john@example.com";
        String jobTitle = "Engineer";

        CareerPassportInitializedEvent event =
                new CareerPassportInitializedEvent(
                        eventId,
                        version,
                        correlationId,
                        causationId,
                        timestamp,
                        aggregateId,
                        name,
                        email,
                        jobTitle);

        assertEquals(eventId, event.eventId());
        assertEquals(name, event.name());
        assertEquals(email, event.email());
        assertEquals(jobTitle, event.jobTitle());
    }

    @Test
    void skillRegisteredEventShouldStoreValues() {
        UUID eventId = UUID.randomUUID();
        int version = 1;
        UUID correlationId = UUID.randomUUID();
        UUID causationId = UUID.randomUUID();
        Instant timestamp = Instant.now();
        UUID aggregateId = UUID.randomUUID();
        String name = "Java";
        String category = "Programming Language";
        UUID parentSkillId = UUID.randomUUID();

        SkillRegisteredEvent event =
                new SkillRegisteredEvent(
                        eventId,
                        version,
                        correlationId,
                        causationId,
                        timestamp,
                        aggregateId,
                        name,
                        category,
                        parentSkillId);

        assertEquals(eventId, event.eventId());
        assertEquals(name, event.name());
        assertEquals(category, event.category());
        assertEquals(parentSkillId, event.parentSkillId());
    }
}
