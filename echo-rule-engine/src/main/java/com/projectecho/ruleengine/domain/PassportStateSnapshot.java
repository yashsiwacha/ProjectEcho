package com.projectecho.ruleengine.domain;

import java.util.Set;
import java.util.UUID;

public record PassportStateSnapshot(UUID passportId, Set<String> skills, boolean isVerified) {
    public PassportStateSnapshot {
        if (passportId == null) {
            throw new IllegalArgumentException("Passport ID cannot be null");
        }
    }
}
