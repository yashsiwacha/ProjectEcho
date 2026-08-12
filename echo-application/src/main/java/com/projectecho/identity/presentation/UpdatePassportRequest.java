package com.projectecho.identity.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePassportRequest(
        @NotBlank(message = "Name is required") @Size(max = 255, message = "Name too long")
                String name,
        @NotBlank(message = "Job title is required")
                @Size(max = 255, message = "Job title too long")
                String jobTitle) {}
