package com.projectecho.shared.domain;

import java.util.Objects;
import java.util.UUID;

public record SkillId(UUID value) {
    public SkillId {
        Objects.requireNonNull(value, "SkillId cannot be null");
    }
}
