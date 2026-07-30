package com.projectecho.evidence.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.projectecho.common.exception.ValidationException;
import org.junit.jupiter.api.Test;

class ConfidenceTest {

  @Test
  void shouldCreateValidConfidence() {
    Confidence confidence = Confidence.of(0.85);
    assertEquals(0.85, confidence.value());
  }

  @Test
  void shouldCreateConfidenceAtLowerBound() {
    Confidence confidence = Confidence.of(0.0);
    assertEquals(0.0, confidence.value());
  }

  @Test
  void shouldCreateConfidenceAtUpperBound() {
    Confidence confidence = Confidence.of(1.0);
    assertEquals(1.0, confidence.value());
  }

  @Test
  void shouldThrowValidationExceptionWhenBelowLowerBound() {
    assertThrows(ValidationException.class, () -> Confidence.of(-0.01));
  }

  @Test
  void shouldThrowValidationExceptionWhenAboveUpperBound() {
    assertThrows(ValidationException.class, () -> Confidence.of(1.01));
  }
}
