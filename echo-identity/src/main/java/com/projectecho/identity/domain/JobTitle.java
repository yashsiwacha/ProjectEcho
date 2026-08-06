package com.projectecho.identity.domain;

import java.util.Objects;

public record JobTitle(String value) {
    public JobTitle {
        Objects.requireNonNull(value, "Job title cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Job title cannot be blank");
        }
    }
}
