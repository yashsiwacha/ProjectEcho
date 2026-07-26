package com.projectecho.common.exception;

/**
 * Exception thrown when the request is malformed or invalid.
 */
public class BadRequestException extends ApplicationException {

    private static final String ERROR_CODE = "BAD_REQUEST";

    /**
     * Creates a new BadRequestException with a message.
     *
     * @param message the error message
     */
    public BadRequestException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Creates a new BadRequestException with a message and cause.
     *
     * @param message the error message
     * @param cause   the cause of this exception
     */
    public BadRequestException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }
}
