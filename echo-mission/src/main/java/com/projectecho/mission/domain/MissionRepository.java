package com.projectecho.mission.domain;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, UUID> {

    Page<Mission> findByStatus(MissionStatus status, Pageable pageable);

    Page<Mission> findByPassportId(UUID passportId, Pageable pageable);

    Page<Mission> findByPassportIdAndStatus(
            UUID passportId, MissionStatus status, Pageable pageable);
}
