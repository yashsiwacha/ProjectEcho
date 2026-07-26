package com.projectecho.common.exception;

/**
 * Exception thrown when rate limit is exceeded.
 */
public class RateLimitExceededException extends ApplicationException {

    private static final String ERROR_CODE = "RATE_LIMIT_EXCEEDED";

    private final long retryAfterSeconds;

    /**
     * Creates a new RateLimitExceededException with retry after duration.
     *
     * @param message            the error message
     * @param retryAfterSeconds  seconds to wait before retrying
     */
    public RateLimitExceededException(String message, long retryAfterSeconds) {
        super(ERROR_CODE, message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    /**
     * Gets the retry after duration in seconds.
     *
     * @return seconds to wait before retrying
     */
    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}