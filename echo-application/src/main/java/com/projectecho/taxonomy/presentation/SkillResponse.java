package com.projectecho.taxonomy.presentation;

import com.projectecho.taxonomy.domain.Skill;
import java.time.Instant;
import java.util.UUID;

public record SkillResponse(
        UUID id,
        String name,
        String category,
        UUID parentSkillId,
        Instant createdAt,
        Instant updatedAt) {

    public static SkillResponse from(final Skill skill) {
        return new SkillResponse(
                skill.getId(),
                skill.getName().value(),
                skill.getCategory().value(),
                skill.getParentSkillId().map(sid -> sid.value()).orElse(null),
                skill.getCreatedAt(),
                skill.getUpdatedAt());
    }
}
