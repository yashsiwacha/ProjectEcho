package com.projectecho.intelligence.application;

import com.projectecho.intelligence.domain.ConfidenceScore;
import com.projectecho.intelligence.domain.ReasoningCard;
import com.projectecho.intelligence.domain.ReasoningCardRepository;
import com.projectecho.intelligence.domain.ReasoningSummary;
import com.projectecho.shared.domain.MissionId;
import com.projectecho.shared.domain.PassportId;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IntelligenceService {

    private final ReasoningCardRepository repository;

    public IntelligenceService(final ReasoningCardRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public ReasoningCard generateReasoningCard(
            final PassportId passportId,
            final MissionId missionId,
            final boolean isEligible,
            final int score) {

        final ConfidenceScore confidenceScore =
                isEligible
                        ? new ConfidenceScore(95)
                        : score > 0 ? new ConfidenceScore(50) : new ConfidenceScore(10);

        final String summaryText =
                isEligible
                        ? "Assessment criteria fully met. The candidate is highly recommended."
                        : "Assessment criteria not fully met. Additional evidence may be required.";

        final ReasoningSummary summary = new ReasoningSummary(summaryText);

        final ReasoningCard card =
                new ReasoningCard(
                        UUID.randomUUID(), passportId, missionId, confidenceScore, summary);

        return repository.save(card);
    }
}
