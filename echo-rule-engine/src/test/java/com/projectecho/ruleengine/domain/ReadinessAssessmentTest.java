package com.projectecho.ruleengine.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReadinessAssessmentTest {

    @Test
    void shouldAssessEligible() {
        final PassportStateSnapshot passport =
                new PassportStateSnapshot(UUID.randomUUID(), Set.of("Java"), true);
        final MissionStateSnapshot mission =
                new MissionStateSnapshot(UUID.randomUUID(), Set.of("Java"), true);

        final BusinessRule alwaysEligible =
                new BusinessRule() {
                    @Override
                    public UUID getRuleId() {
                        return UUID.randomUUID();
                    }

                    @Override
                    public String getDescription() {
                        return "Always Eligible";
                    }

                    @Override
                    public DecisionGraph evaluate(PassportStateSnapshot p, MissionStateSnapshot m) {
                        return new DecisionGraph(
                                UUID.randomUUID(),
                                Instant.now(),
                                getRuleId(),
                                p.passportId(),
                                m.missionId(),
                                java.util.List.of(),
                                java.util.List.of(),
                                true,
                                100,
                                "Passed",
                                null);
                    }
                };

        final ReadinessAssessment result =
                ReadinessAssessment.assess(passport, mission, alwaysEligible);

        assertThat(result.isEligible()).isTrue();
        assertThat(result.getScore()).isEqualTo(100);
        assertThat(result.getDomainEvents()).hasSize(1);
    }
}
