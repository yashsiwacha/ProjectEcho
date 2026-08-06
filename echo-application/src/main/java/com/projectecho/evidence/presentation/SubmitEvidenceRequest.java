package com.projectecho.evidence.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SubmitEvidenceRequest(
        @NotNull(message = "Passport ID is required") UUID passportId,
        @NotNull(message = "Skill ID is required") UUID skillId,
        @NotBlank(message = "Source URI is required") String sourceUri) {}
