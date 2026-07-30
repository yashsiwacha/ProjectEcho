package com.projectecho.common.valueobject;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable value object representing a point in time. Wraps a java.time.Instant for
 * domain-specific date-time representations.
 *
 * @param value the underlying Instant
 */
public record Timestamp(Instant value) {

  public Timestamp {
    Objects.requireNonNull(value, "Timestamp value must not be null");
  }

  /**
   * Captures the exact current time.
   *
   * @return a new Timestamp representing now
   */
  public static Timestamp now() {
    return new Timestamp(Instant.now());
  }

  /**
   * Creates a Timestamp from an existing Instant.
   *
   * @param instant the instant
   * @return a Timestamp
   */
  public static Timestamp from(Instant instant) {
    return new Timestamp(instant);
  }
}
