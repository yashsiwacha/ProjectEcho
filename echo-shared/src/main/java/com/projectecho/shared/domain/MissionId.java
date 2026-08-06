package com.projectecho.shared.domain;

import java.util.Objects;
import java.util.UUID;

public record MissionId(UUID value) {
    public MissionId {
        Objects.requireNonNull(value, "MissionId value cannot be null");
    }
}
