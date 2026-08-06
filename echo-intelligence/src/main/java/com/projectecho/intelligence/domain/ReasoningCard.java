package com.projectecho.intelligence.domain;

import com.projectecho.shared.domain.AggregateRoot;
import com.projectecho.shared.domain.MissionId;
import com.projectecho.shared.domain.PassportId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "intelligence_reasoning_cards")
public class ReasoningCard extends AggregateRoot {

    @Column(nullable = false)
    private PassportId passportId;

    @Column(nullable = false)
    private MissionId missionId;

    @Column(nullable = false)
    private ConfidenceScore confidenceScore;

    @Column(nullable = false, columnDefinition = "TEXT")
    private ReasoningSummary summary;

    protected ReasoningCard() {
        super();
        // JPA
    }

    public ReasoningCard(
            final UUID id,
            final PassportId passportId,
            final MissionId missionId,
            final ConfidenceScore confidenceScore,
            final ReasoningSummary summary) {
        super(id);
        this.passportId = Objects.requireNonNull(passportId, "PassportId cannot be null");
        this.missionId = Objects.requireNonNull(missionId, "MissionId cannot be null");
        this.confidenceScore =
                Objects.requireNonNull(confidenceScore, "ConfidenceScore cannot be null");
        this.summary = Objects.requireNonNull(summary, "ReasoningSummary cannot be null");
    }

    public PassportId getPassportId() {
        return passportId;
    }

    public MissionId getMissionId() {
        return missionId;
    }

    public ConfidenceScore getConfidenceScore() {
        return confidenceScore;
    }

    public ReasoningSummary getSummary() {
        return summary;
    }
}
