package com.projectecho.common.shared;

/**
 * Standardized error classifications for the ProjectEcho Modular Monolith. Enforces explainability
 * by requiring all faults to map to a known domain or system error state.
 */
public enum ErrorCode {
  /** Client provided invalid input. Equivalent to HTTP 400. */
  VALIDATION_ERROR,

  /** Client attempted an operation that violated a domain invariant. Equivalent to HTTP 422. */
  BUSINESS_RULE_VIOLATION,

  /** Unhandled system fault. Equivalent to HTTP 500. */
  SYSTEM_ERROR,

  /** Entity or resource was not found. Equivalent to HTTP 404. */
  NOT_FOUND,

  /** The client is unauthenticated or the token is invalid. Equivalent to HTTP 401. */
  UNAUTHORIZED,

  /** The client lacks sufficient permission for the resource. Equivalent to HTTP 403. */
  FORBIDDEN
}
