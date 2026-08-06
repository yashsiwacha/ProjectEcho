package com.projectecho.ruleengine.presentation;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public record EvaluateReadinessRequest(
        @NotNull(message = "Passport ID is required") UUID passportId,
        @NotNull(message = "Mission ID is required") UUID missionId,
        Set<String> passportSkills,
        boolean isPassportVerified,
        Set<String> missionRequiredSkills,
        boolean isMissionActive) {}
