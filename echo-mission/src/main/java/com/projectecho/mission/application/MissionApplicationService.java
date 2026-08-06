package com.projectecho.mission.application;

import com.projectecho.mission.domain.Mission;
import com.projectecho.mission.domain.MissionRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MissionApplicationService {

    private final MissionRepository missionRepository;

    public MissionApplicationService(final MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    public UUID createMission(final String title) {
        final UUID id = UUID.randomUUID();
        final Mission mission = Mission.draft(id, title);
        missionRepository.save(mission);
        return id;
    }

    public void activateMission(final UUID missionId) {
        final Mission mission =
                missionRepository
                        .findById(missionId)
                        .orElseThrow(() -> new IllegalArgumentException("Mission not found"));
        mission.activate();
        missionRepository.save(mission);
    }
}
