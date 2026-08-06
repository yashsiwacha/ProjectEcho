package com.projectecho.shared.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class AggregateRootTest {
    private static class TestAggregate extends AggregateRoot {
        TestAggregate(UUID id) {
            super(id);
        }

        void update() {
            markUpdated();
        }
    }

    @Test
    void shouldInitializeCorrectly() {
        UUID id = UUID.randomUUID();
        TestAggregate aggregate = new TestAggregate(id);

        assertEquals(id, aggregate.getId());
        assertEquals(0L, aggregate.getVersion());
        assertNotNull(aggregate.getCreatedAt());
        assertEquals(aggregate.getCreatedAt(), aggregate.getUpdatedAt());
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> new TestAggregate(null));
    }

    @Test
    void shouldUpdateTimestampWhenMarkedUpdated() throws InterruptedException {
        UUID id = UUID.randomUUID();
        TestAggregate aggregate = new TestAggregate(id);

        Thread.sleep(10); // Ensure timestamp difference
        aggregate.update();

        assertTrue(aggregate.getUpdatedAt().isAfter(aggregate.getCreatedAt()));
    }
}
