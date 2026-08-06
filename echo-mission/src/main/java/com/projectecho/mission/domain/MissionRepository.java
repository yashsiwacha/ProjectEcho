package com.projectecho.mission.domain;

import java.util.Optional;
import java.util.UUID;

public interface MissionRepository {
    void save(Mission mission);

    Optional<Mission> findById(UUID id);
}
