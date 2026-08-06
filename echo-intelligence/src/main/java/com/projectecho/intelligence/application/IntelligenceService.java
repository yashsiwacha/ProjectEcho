package com.projectecho.intelligence.application;

import com.projectecho.intelligence.domain.ConfidenceScore;
import com.projectecho.intelligence.domain.ReasoningCard;
import com.projectecho.intelligence.domain.ReasoningCardRepository;
import com.projectecho.intelligence.domain.ReasoningSummary;
import com.projectecho.shared.domain.MissionId;
import com.projectecho.shared.domain.PassportId;
import com.projectecho.shared.exception.ResourceNotFoundException;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IntelligenceService {

    private static final Logger LOG = LoggerFactory.getLogger(IntelligenceService.class);
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

        if (LOG.isInfoEnabled()) {
            LOG.info(
                    "Reasoning card generated for passport {} mission {}",
                    passportId.value(),
                    missionId.value());
        }

        return repository.save(card);
    }

    @Transactional(readOnly = true)
    public ReasoningCard findById(final UUID cardId) {
        return repository
                .findById(cardId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Reasoning card not found: " + cardId));
    }

    @Transactional(readOnly = true)
    public Page<ReasoningCard> findAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ReasoningCard> findByPassportId(
            final PassportId passportId, final Pageable pageable) {
        return repository.findByPassportId(passportId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ReasoningCard> findByMissionId(final MissionId missionId, final Pageable pageable) {
        return repository.findByMissionId(missionId, pageable);
    }
}
