package com.projectecho.common.shared;

import java.time.Instant;
import java.util.Objects;

/**
 * Standardized API Error Response Payload. Designed to be serialized to JSON by the bootstrap layer
 * (e.g. via @RestControllerAdvice).
 *
 * @param errorCode the categorized error classification
 * @param message the human-readable explanation of the error
 * @param timestamp the exact time the error occurred
 */
public record ErrorResponse(ErrorCode errorCode, String message, Instant timestamp) {

  public ErrorResponse {
    Objects.requireNonNull(errorCode, "ErrorCode must not be null");
    Objects.requireNonNull(message, "Message must not be null");
    Objects.requireNonNull(timestamp, "Timestamp must not be null");
  }

  /**
   * Factory method to create a new ErrorResponse for the current time.
   *
   * @param errorCode the error code
   * @param message the message
   * @return a new ErrorResponse
   */
  public static ErrorResponse of(ErrorCode errorCode, String message) {
    return new ErrorResponse(errorCode, message, Instant.now());
  }
}
