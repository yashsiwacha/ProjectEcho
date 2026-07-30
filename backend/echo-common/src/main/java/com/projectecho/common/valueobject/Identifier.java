package com.projectecho.common.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Immutable value object representing a unique identifier. Wraps a java.util.UUID to provide type
 * safety over primitive Strings.
 *
 * @param value the underlying UUID
 */
public record Identifier(UUID value) {

  public Identifier {
    Objects.requireNonNull(value, "Identifier value must not be null");
  }

  /**
   * Generates a new random Identifier.
   *
   * @return a new Identifier
   */
  public static Identifier generate() {
    return new Identifier(UUID.randomUUID());
  }

  /**
   * Parses a UUID string into an Identifier.
   *
   * @param uuidString the valid UUID string
   * @return an Identifier
   */
  public static Identifier from(String uuidString) {
    return new Identifier(UUID.fromString(uuidString));
  }
}
