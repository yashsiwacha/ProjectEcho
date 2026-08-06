package com.projectecho.taxonomy.domain;

import java.util.Objects;

public record SkillName(String value) {
    public SkillName {
        Objects.requireNonNull(value, "Skill name cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Skill name cannot be blank");
        }
    }
}
