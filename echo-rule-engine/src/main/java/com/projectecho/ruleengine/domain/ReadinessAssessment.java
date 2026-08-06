package com.projectecho.ruleengine.domain;

import com.projectecho.shared.domain.AggregateRoot;
import com.projectecho.shared.events.ReadinessAssessmentCompletedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "readiness_assessments")
public class ReadinessAssessment extends AggregateRoot {

    @Column(nullable = false, updatable = false)
    private UUID passportId;

    @Column(nullable = false, updatable = false)
    private UUID missionId;

    @Column(nullable = false)
    private boolean eligible;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false, updatable = false)
    private UUID graphId;

    protected ReadinessAssessment() {
        super();
    }

    private ReadinessAssessment(
            final UUID id,
            final UUID passportId,
            final UUID missionId,
            final boolean eligible,
            final int score,
            final UUID graphId) {
        super(id);
        this.passportId = passportId;
        this.missionId = missionId;
        this.eligible = eligible;
        this.score = score;
        this.graphId = graphId;
    }

    public static ReadinessAssessment assess(
            final PassportStateSnapshot passport,
            final MissionStateSnapshot mission,
            final BusinessRule rule) {

        final DecisionGraph graph = rule.evaluate(passport, mission);
        final UUID id = UUID.randomUUID();

        final ReadinessAssessment assessment =
                new ReadinessAssessment(
                        id,
                        passport.passportId(),
                        mission.missionId(),
                        graph.isEligible(),
                        graph.score(),
                        graph.graphId());

        assessment.registerEvent(
                new ReadinessAssessmentCompletedEvent(
                        UUID.randomUUID(),
                        1,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        graph.timestamp(),
                        id,
                        passport.passportId(),
                        mission.missionId(),
                        graph.isEligible(),
                        graph.score(),
                        graph.graphId()));

        return assessment;
    }

    public UUID getPassportId() {
        return passportId;
    }

    public UUID getMissionId() {
        return missionId;
    }

    public boolean isEligible() {
        return eligible;
    }

    public int getScore() {
        return score;
    }

    public UUID getGraphId() {
        return graphId;
    }
}
