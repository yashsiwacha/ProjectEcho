package com.projectecho.intelligence.domain;

public record ConfidenceScore(int percentage) {
    public ConfidenceScore {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Confidence score must be between 0 and 100");
        }
    }
}
