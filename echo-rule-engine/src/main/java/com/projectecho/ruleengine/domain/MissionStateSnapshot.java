package com.projectecho.ruleengine.domain;

import java.util.Set;
import java.util.UUID;

public record MissionStateSnapshot(UUID missionId, Set<String> requiredSkills, boolean isActive) {
    public MissionStateSnapshot {
        if (missionId == null) {
            throw new IllegalArgumentException("Mission ID cannot be null");
        }
    }
}
