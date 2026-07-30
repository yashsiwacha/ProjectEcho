package com.projectecho.common.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Marker interface for all domain events across the Modular Monolith. Ensures all events are
 * serializable and have standard metadata identifiers.
 */
public interface DomainEvent extends Serializable {

  /**
   * Retrieves the unique identifier of this event instance.
   *
   * @return the event UUID
   */
  UUID getEventId();

  /**
   * Retrieves the timestamp when this event occurred.
   *
   * @return the exact occurrence time
   */
  Instant getOccurredAt();
}
