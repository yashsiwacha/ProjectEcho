package com.projectecho.shared.domain;

import java.util.Objects;
import java.util.UUID;

public record PassportId(UUID value) {
    public PassportId {
        Objects.requireNonNull(value, "PassportId cannot be null");
    }
}
