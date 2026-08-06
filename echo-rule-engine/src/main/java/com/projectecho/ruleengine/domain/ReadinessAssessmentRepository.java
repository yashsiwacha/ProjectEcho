package com.projectecho.ruleengine.domain;

import java.util.Optional;
import java.util.UUID;

public interface ReadinessAssessmentRepository {
    void save(ReadinessAssessment assessment);

    Optional<ReadinessAssessment> findById(UUID id);
}
