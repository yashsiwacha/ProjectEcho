package com.projectecho.common.exception;

/**
 * Exception thrown when a service is temporarily unavailable.
 */
public class ServiceUnavailableException extends ApplicationException {

    private static final String ERROR_CODE = "SERVICE_UNAVAILABLE";

    /**
     * Creates a new ServiceUnavailableException with a message.
     *
     * @param message the error message
     */
    public ServiceUnavailableException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Creates a new ServiceUnavailableException with a message and cause.
     *
     * @param message the error message
     * @param cause   the cause of this exception
     */
    public ServiceUnavailableException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }
}
