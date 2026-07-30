package com.projectecho.common.exception;

import com.projectecho.common.shared.ErrorCode;

/**
 * Exception thrown when input data violates expected constraints or formats. Maps to HTTP 400 Bad
 * Request at the API edge.
 */
public class ValidationException extends DomainException {

  public ValidationException(String message) {
    super(ErrorCode.VALIDATION_ERROR, message);
  }

  public ValidationException(String message, Throwable cause) {
    super(ErrorCode.VALIDATION_ERROR, message, cause);
  }
}
