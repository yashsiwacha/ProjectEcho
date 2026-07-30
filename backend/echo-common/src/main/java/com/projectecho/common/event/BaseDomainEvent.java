package com.projectecho.common.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Abstract base class for domain events. Automatically provisions an immutable unique identifier
 * and an exact occurrence timestamp.
 */
public abstract class BaseDomainEvent implements DomainEvent {

  private final UUID eventId;
  private final Instant occurredAt;

  protected BaseDomainEvent() {
    this.eventId = UUID.randomUUID();
    this.occurredAt = Instant.now();
  }

  @Override
  public UUID getEventId() {
    return eventId;
  }

  @Override
  public Instant getOccurredAt() {
    return occurredAt;
  }
}
