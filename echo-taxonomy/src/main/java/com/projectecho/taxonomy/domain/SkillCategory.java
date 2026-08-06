package com.projectecho.taxonomy.domain;

import java.util.Objects;

public record SkillCategory(String value) {
    public SkillCategory {
        Objects.requireNonNull(value, "Skill category cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Skill category cannot be blank");
        }
    }
}
