package com.projectecho.ruleengine.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DecisionGraph(
        UUID graphId,
        Instant timestamp,
        UUID ruleId,
        UUID passportId,
        UUID missionId,
        List<UUID> contributingEvidenceIds,
        List<UUID> contributingEventIds,
        boolean isEligible,
        int score,
        String rationale,
        UUID reasoningCardId) {
    public DecisionGraph {
        if (graphId == null || ruleId == null) {
            throw new IllegalArgumentException("Graph ID and Rule ID are required");
        }
    }
}
