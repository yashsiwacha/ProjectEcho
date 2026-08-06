package com.projectecho.identity.domain;

import java.util.Objects;

public record Name(String value) {
    public Name {
        Objects.requireNonNull(value, "Name cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
    }
}
