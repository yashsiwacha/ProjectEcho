package com.projectecho.mission.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateMissionRequest(
        @NotBlank(message = "Title is required")
                @Size(max = 200, message = "Title cannot exceed 200 characters")
                String title,
        UUID organizationId,
        UUID teamId) {}
