package com.projectecho.mission.presentation;

import com.projectecho.mission.domain.Mission;
import java.time.Instant;
import java.util.UUID;

public record MissionResponse(
        UUID id, String title, String status, Instant createdAt, Instant updatedAt) {

    public static MissionResponse from(final Mission mission) {
        return new MissionResponse(
                mission.getId(),
                mission.getTitle().value(),
                mission.getStatus().name(),
                mission.getCreatedAt(),
                mission.getUpdatedAt());
    }
}
