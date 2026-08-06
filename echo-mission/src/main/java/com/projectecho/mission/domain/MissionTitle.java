package com.projectecho.mission.domain;

import java.util.Objects;

public record MissionTitle(String value) {
    private static final int MAX_LENGTH = 200;

    public MissionTitle {
        Objects.requireNonNull(value, "Mission title cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Mission title cannot be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Mission title cannot exceed 200 characters");
        }
    }
}
