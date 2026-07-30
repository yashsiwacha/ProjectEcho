package com.projectecho.evidence.domain.model;

import com.projectecho.common.exception.ValidationException;

/**
 * Value Object representing the systemic certainty that a Capability is truly possessed. Ranges
 * from 0.0 to 1.0.
 */
public record Confidence(double value) {

  public Confidence {
    if (value < 0.0 || value > 1.0) {
      throw new ValidationException("Confidence must be between 0.0 and 1.0");
    }
  }

  public static Confidence of(double value) {
    return new Confidence(value);
  }
}
