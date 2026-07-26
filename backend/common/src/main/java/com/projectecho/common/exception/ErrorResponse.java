package com.projectecho.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Standard error response object for API responses.
 *
 * @param timestamp  the time the error occurred
 * @param status     the HTTP status code
 * @param error      the HTTP status reason phrase
 * @param errorCode  the application error code
 * @param message    the error message
 * @param details    additional error details
 * @param path       the request path
 * @param fieldErrors validation errors by field name
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String errorCode,
        String message,
        String details,
        String path,
        Map<String, List<String>> fieldErrors
) {

    /**
     * Creates a new ErrorResponse without field errors.
     */
    public ErrorResponse(Instant timestamp, int status, String error, String errorCode,
                         String message, String details, String path) {
        this(timestamp, status, error, errorCode, message, details, path, null);
    }

    /**
     * Creates a new ErrorResponse from an ApplicationException.
     */
    public static ErrorResponse fromException(ApplicationException ex, int status, String path) {
        return new ErrorResponse(
                Instant.now(),
                status,
                getStatusText(status),
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getDetails(),
                path,
                null
        );
    }

    /**
     * Creates a new ErrorResponse from a ValidationException.
     */
    public static ErrorResponse fromValidationException(ValidationException ex, int status, String path) {
        return new ErrorResponse(
                Instant.now(),
                status,
                getStatusText(status),
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getDetails(),
                path,
                ex.getFieldErrors()
        );
    }

    private static String getStatusText(int status) {
        return switch (status) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            case 429 -> "Too Many Requests";
            case 500 -> "Internal Server Error";
            case 503 -> "Service Unavailable";
            default -> "Error";
        };
    }
}
