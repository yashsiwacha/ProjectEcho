package com.projectecho.common.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class TimestampTest {

  @Test
  void shouldCaptureCurrentTime() {
    Timestamp now = Timestamp.now();
    assertNotNull(now.value());
    assertTrue(now.value().isBefore(Instant.now().plusSeconds(1)));
  }

  @Test
  void shouldCreateFromExistingInstant() {
    Instant exactTime = Instant.parse("2026-01-01T10:00:00Z");
    Timestamp timestamp = Timestamp.from(exactTime);

    assertEquals(exactTime, timestamp.value());
  }

  @Test
  void shouldThrowWhenNullValueProvided() {
    assertThrows(NullPointerException.class, () -> new Timestamp(null));
  }
}
