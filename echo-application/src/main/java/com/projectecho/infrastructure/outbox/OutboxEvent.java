package com.projectecho.infrastructure.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id private UUID id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    protected OutboxEvent() {}

    public OutboxEvent(
            final UUID id, final String eventType, final Instant createdAt, final String payload) {
        this.id = id;
        this.eventType = eventType;
        this.createdAt = createdAt;
        this.payload = payload;
    }

    public UUID getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getPayload() {
        return payload;
    }
}
