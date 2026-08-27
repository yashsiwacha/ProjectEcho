package com.projectecho.ruleengine.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class StandardReadinessRule implements BusinessRule {

    private final UUID ruleId = UUID.randomUUID();

    @Override
    public UUID getRuleId() {
        return ruleId;
    }

    @Override
    public String getDescription() {
        return "Standard Skill Matching Readiness Rule (Deterministic)";
    }

    @Override
    public DecisionGraph evaluate(
            final PassportStateSnapshot passport, final MissionStateSnapshot mission) {

        if (passport == null) {
            throw new IllegalArgumentException("PassportStateSnapshot cannot be null");
        }
        if (mission == null) {
            throw new IllegalArgumentException("MissionStateSnapshot cannot be null");
        }

        final int requiredCount = mission.requiredSkills().size();
        final int matchedCount =
                (int)
                        mission.requiredSkills().stream()
                                .filter(skill -> passport.skills().contains(skill))
                                .count();

        // Deterministic Base Score
        int score;
        if (requiredCount > 0) {
            score = matchedCount * 100 / requiredCount;
        } else {
            score = passport.skills().isEmpty() ? 0 : 50;
        }

        // Apply penalties for unverified profiles
        if (!passport.isVerified()) {
            score = (int) (score * 0.8);
        }

        final boolean eligible = score >= 80 && mission.isActive();

        return new DecisionGraph(
                UUID.randomUUID(),
                Instant.now(),
                ruleId,
                passport.passportId(),
                mission.missionId(),
                List.of(),
                List.of(),
                eligible,
                score,
                "Evaluated standard readiness deterministically",
                UUID.randomUUID());
    }
}
