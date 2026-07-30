package com.projectecho.common.exception;

import com.projectecho.common.shared.ErrorCode;

/**
 * Exception thrown when an operation violates a domain invariant or business rule. Maps to HTTP 422
 * Unprocessable Entity at the API edge.
 */
public class BusinessRuleViolationException extends DomainException {

  public BusinessRuleViolationException(String message) {
    super(ErrorCode.BUSINESS_RULE_VIOLATION, message);
  }

  public BusinessRuleViolationException(String message, Throwable cause) {
    super(ErrorCode.BUSINESS_RULE_VIOLATION, message, cause);
  }
}
