package com.projectecho.ruleengine.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class StandardReadinessRuleTest {

    @Test
    void shouldThrowIfPassportIsNull() {
        final StandardReadinessRule rule = new StandardReadinessRule();
        assertThatThrownBy(() -> rule.evaluate(null, createMission(Set.of(), true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldScore100WhenAllSkillsMatchAndVerified() {
        final StandardReadinessRule rule = new StandardReadinessRule();
        final PassportStateSnapshot passport = createPassport(Set.of("Java", "Spring"), true);
        final MissionStateSnapshot mission = createMission(Set.of("Java", "Spring"), true);

        final DecisionGraph result = rule.evaluate(passport, mission);

        assertThat(result.score()).isEqualTo(100);
        assertThat(result.isEligible()).isTrue();
    }

    @Test
    void shouldApplyPenaltyWhenUnverified() {
        final StandardReadinessRule rule = new StandardReadinessRule();
        final PassportStateSnapshot passport = createPassport(Set.of("Java", "Spring"), false);
        final MissionStateSnapshot mission = createMission(Set.of("Java", "Spring"), true);

        final DecisionGraph result = rule.evaluate(passport, mission);

        assertThat(result.score()).isEqualTo(80); // 100 * 0.8
        assertThat(result.isEligible()).isTrue(); // 80 >= 80
    }

    @Test
    void shouldScore50WhenHalfSkillsMatch() {
        final StandardReadinessRule rule = new StandardReadinessRule();
        final PassportStateSnapshot passport = createPassport(Set.of("Java"), true);
        final MissionStateSnapshot mission = createMission(Set.of("Java", "Spring"), true);

        final DecisionGraph result = rule.evaluate(passport, mission);

        assertThat(result.score()).isEqualTo(50);
        assertThat(result.isEligible()).isFalse(); // 50 < 80
    }

    @Test
    void shouldScore0WhenEmptyProfile() {
        final StandardReadinessRule rule = new StandardReadinessRule();
        final PassportStateSnapshot passport = createPassport(Set.of(), true);
        final MissionStateSnapshot mission = createMission(Set.of("Java"), true);

        final DecisionGraph result = rule.evaluate(passport, mission);

        assertThat(result.score()).isEqualTo(0);
        assertThat(result.isEligible()).isFalse();
    }

    private PassportStateSnapshot createPassport(Set<String> skills, boolean verified) {
        return new PassportStateSnapshot(UUID.randomUUID(), skills, verified);
    }

    private MissionStateSnapshot createMission(Set<String> requiredSkills, boolean active) {
        return new MissionStateSnapshot(UUID.randomUUID(), requiredSkills, active);
    }
}
