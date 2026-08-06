package com.projectecho.mission.application;

import com.projectecho.mission.domain.Mission;
import com.projectecho.mission.domain.MissionRepository;
import com.projectecho.mission.domain.MissionStatus;
import com.projectecho.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MissionApplicationService {

    private static final Logger LOG = LoggerFactory.getLogger(MissionApplicationService.class);
    private final MissionRepository missionRepository;

    public MissionApplicationService(final MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    public UUID createMission(final String title) {
        final UUID id = UUID.randomUUID();
        final Mission mission = Mission.draft(id, title);
        missionRepository.save(mission);

        if (LOG.isInfoEnabled()) {
            LOG.info("Mission created: {} ({})", title, id);
        }

        return id;
    }

    public void activateMission(final UUID missionId) {
        final Mission mission = findMissionById(missionId);
        mission.activate();
        missionRepository.save(mission);

        if (LOG.isInfoEnabled()) {
            LOG.info("Mission activated: {}", missionId);
        }
    }

    public void archiveMission(final UUID missionId) {
        final Mission mission = findMissionById(missionId);
        mission.archive();
        missionRepository.save(mission);

        if (LOG.isInfoEnabled()) {
            LOG.info("Mission archived: {}", missionId);
        }
    }

    @Transactional(readOnly = true)
    public Mission findById(final UUID missionId) {
        return findMissionById(missionId);
    }

    @Transactional(readOnly = true)
    public Page<Mission> findAll(final Pageable pageable) {
        return missionRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Mission> findByStatus(final MissionStatus status, final Pageable pageable) {
        return missionRepository.findByStatus(status, pageable);
    }

    private Mission findMissionById(final UUID missionId) {
        return missionRepository
                .findById(missionId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Mission not found: " + missionId));
    }
}
