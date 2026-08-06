package com.projectecho.identity.presentation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePassportRequest(
        @NotBlank(message = "Name is required") @Size(max = 255, message = "Name too long")
                String name,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format")
                String email,
        @NotBlank(message = "Job title is required")
                @Size(max = 255, message = "Job title too long")
                String jobTitle) {}
