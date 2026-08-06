package com.projectecho.application.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        int status,
        String error,
        String message,
        String path,
        Instant timestamp,
        List<String> details) {

    public ApiError(final int status, final String error, final String message, final String path) {
        this(status, error, message, path, Instant.now(), null);
    }

    public ApiError(
            final int status,
            final String error,
            final String message,
            final String path,
            final List<String> details) {
        this(status, error, message, path, Instant.now(), details);
    }
}
