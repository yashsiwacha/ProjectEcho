package com.projectecho.common.exception;

/**
 * Exception thrown for internal server errors.
 */
public class InternalServerException extends ApplicationException {

    private static final String ERROR_CODE = "INTERNAL_SERVER_ERROR";

    /**
     * Creates a new InternalServerException with a message.
     *
     * @param message the error message
     */
    public InternalServerException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Creates a new InternalServerException with a message and cause.
     *
     * @param message the error message
     * @param cause   the cause of this exception
     */
    public InternalServerException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }
}
