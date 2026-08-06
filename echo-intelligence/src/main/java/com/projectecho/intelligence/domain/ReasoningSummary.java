package com.projectecho.intelligence.domain;

import java.util.Objects;

public record ReasoningSummary(String text) {
    public ReasoningSummary {
        Objects.requireNonNull(text, "Reasoning summary cannot be null");
        if (text.isBlank()) {
            throw new IllegalArgumentException("Reasoning summary cannot be blank");
        }
    }
}
