package com.projectecho.taxonomy.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateSkillRequest(
        @NotBlank(message = "Skill name is required")
                @Size(max = 255, message = "Skill name too long")
                String name,
        @NotBlank(message = "Category is required") @Size(max = 255, message = "Category too long")
                String category,
        UUID parentSkillId) {}
