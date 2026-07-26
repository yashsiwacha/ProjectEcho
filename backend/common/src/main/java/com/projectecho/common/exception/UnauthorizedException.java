package com.projectecho.common.exception;

/**
 * Exception thrown when authentication is required or fails.
 */
public class UnauthorizedException extends ApplicationException {

    private static final String ERROR_CODE = "UNAUTHORIZED";

    /**
     * Creates a new UnauthorizedException with a message.
     *
     * @param message the error message
     */
    public UnauthorizedException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Creates a new UnauthorizedException for authentication failure.
     */
    public UnauthorizedException() {
        super(ERROR_CODE, "Authentication required");
    }
}
