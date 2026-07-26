package com.projectecho.common.exception;

import java.util.List;
import java.util.Map;

/**
 * Exception thrown when validation fails for one or more fields.
 */
public class ValidationException extends ApplicationException {

    private static final String ERROR_CODE = "VALIDATION_ERROR";

    private final Map<String, List<String>> fieldErrors;

    /**
     * Creates a new ValidationException with field errors.
     *
     * @param fieldErrors map of field names to lists of error messages
     */
    public ValidationException(Map<String, List<String>> fieldErrors) {
        super(ERROR_CODE, "Validation failed");
        this.fieldErrors = fieldErrors;
    }

    /**
     * Creates a new ValidationException with field errors and message.
     *
     * @param message     the error message
     * @param fieldErrors map of field names to lists of error messages
     */
    public ValidationException(String message, Map<String, List<String>> fieldErrors) {
        super(ERROR_CODE, message);
        this.fieldErrors = fieldErrors;
    }

    /**
     * Gets the field errors.
     *
     * @return map of field names to lists of error messages
     */
    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
}
