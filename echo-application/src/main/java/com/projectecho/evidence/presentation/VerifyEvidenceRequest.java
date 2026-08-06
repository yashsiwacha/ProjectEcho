package com.projectecho.evidence.presentation;

import jakarta.validation.constraints.NotNull;

public record VerifyEvidenceRequest(
        @NotNull(message = "Trust tier is required") String trustTier) {}
