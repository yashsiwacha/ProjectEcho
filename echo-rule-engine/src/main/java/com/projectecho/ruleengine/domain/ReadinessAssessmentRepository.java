package com.projectecho.ruleengine.domain;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadinessAssessmentRepository extends JpaRepository<ReadinessAssessment, UUID> {

    Page<ReadinessAssessment> findByPassportId(UUID passportId, Pageable pageable);

    Page<ReadinessAssessment> findByMissionId(UUID missionId, Pageable pageable);

    Page<ReadinessAssessment> findByPassportIdAndMissionId(
            UUID passportId, UUID missionId, Pageable pageable);
}
