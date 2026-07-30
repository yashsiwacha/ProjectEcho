package com.projectecho.common.exception;

import com.projectecho.common.shared.ErrorCode;

/**
 * Base RuntimeException for all domain-specific faults. Mandates the inclusion of an ErrorCode to
 * ensure programmatic explainability.
 */
public abstract class DomainException extends RuntimeException {

  private final ErrorCode errorCode;

  protected DomainException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  protected DomainException(ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  /**
   * Returns the categorized error code.
   *
   * @return the ErrorCode
   */
  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
