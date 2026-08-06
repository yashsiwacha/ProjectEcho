package com.projectecho.application.api;

import com.projectecho.shared.exception.DomainException;
import com.projectecho.shared.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.UnnecessaryConstructor"})
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
        super();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            final ResourceNotFoundException ex, final HttpServletRequest request) {
        if (LOG.isWarnEnabled()) {
            LOG.warn("Resource not found: {}", ex.getMessage());
        }
        final ApiError error =
                new ApiError(
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        ex.getMessage(),
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomain(
            final DomainException ex, final HttpServletRequest request) {
        if (LOG.isWarnEnabled()) {
            LOG.warn("Domain error: {}", ex.getMessage());
        }
        final ApiError error =
                new ApiError(
                        HttpStatus.CONFLICT.value(),
                        "Conflict",
                        ex.getMessage(),
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(
            final IllegalArgumentException ex, final HttpServletRequest request) {
        if (LOG.isWarnEnabled()) {
            LOG.warn("Bad request: {}", ex.getMessage());
        }
        final ApiError error =
                new ApiError(
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        ex.getMessage(),
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(
            final IllegalStateException ex, final HttpServletRequest request) {
        if (LOG.isWarnEnabled()) {
            LOG.warn("Illegal state: {}", ex.getMessage());
        }
        final ApiError error =
                new ApiError(
                        HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        "Unprocessable Entity",
                        ex.getMessage(),
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            final MethodArgumentNotValidException ex, final HttpServletRequest request) {
        final List<String> details =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                        .toList();
        if (LOG.isWarnEnabled()) {
            LOG.warn("Validation failed: {}", details);
        }
        final ApiError error =
                new ApiError(
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation Failed",
                        "Request body validation failed",
                        request.getRequestURI(),
                        details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            final Exception ex, final HttpServletRequest request) {
        if (LOG.isErrorEnabled()) {
            LOG.error("Unexpected error", ex);
        }
        final ApiError error =
                new ApiError(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error",
                        "An unexpected error occurred",
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
