package com.projectecho.ruleengine.presentation;

import com.projectecho.ruleengine.domain.ReadinessAssessment;
import java.time.Instant;
import java.util.UUID;

public record ReadinessAssessmentResponse(
        UUID id,
        UUID passportId,
        UUID missionId,
        boolean eligible,
        int score,
        UUID graphId,
        Instant createdAt) {

    public static ReadinessAssessmentResponse from(final ReadinessAssessment assessment) {
        return new ReadinessAssessmentResponse(
                assessment.getId(),
                assessment.getPassportId(),
                assessment.getMissionId(),
                assessment.isEligible(),
                assessment.getScore(),
                assessment.getGraphId(),
                assessment.getCreatedAt());
    }
}
