package com.projectecho.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.projectecho.common.shared.ErrorCode;
import org.junit.jupiter.api.Test;

class ExceptionHierarchyTest {

  @Test
  void validationExceptionShouldHaveCorrectErrorCode() {
    ValidationException exception = new ValidationException("Invalid email format");

    assertEquals(ErrorCode.VALIDATION_ERROR, exception.getErrorCode());
    assertEquals("Invalid email format", exception.getMessage());
  }

  @Test
  void businessRuleViolationShouldHaveCorrectErrorCode() {
    BusinessRuleViolationException exception =
        new BusinessRuleViolationException("User is not active");

    assertEquals(ErrorCode.BUSINESS_RULE_VIOLATION, exception.getErrorCode());
    assertEquals("User is not active", exception.getMessage());
  }
}
