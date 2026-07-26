package com.projectecho.common.exception;

/**
 * Base exception class for all ProjectEcho application exceptions.
 * Provides a structured way to handle errors with error codes and messages.
 */
public class ApplicationException extends RuntimeException {

    private final String errorCode;
    private final String details;

    /**
     * Creates a new ApplicationException with error code and message.
     *
     * @param errorCode the error code (e.g., "RESOURCE_NOT_FOUND")
     * @param message   the error message
     */
    public ApplicationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    /**
     * Creates a new ApplicationException with error code, message, and cause.
     *
     * @param errorCode the error code
     * @param message   the error message
     * @param cause     the cause of this exception
     */
    public ApplicationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = null;
    }

    /**
     * Creates a new ApplicationException with error code, message, and details.
     *
     * @param errorCode the error code
     * @param message   the error message
     * @param details   additional error details
     */
    public ApplicationException(String errorCode, String message, String details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the error details.
     *
     * @return the error details, or null if not provided
     */
    public String getDetails() {
        return details;
    }
}
