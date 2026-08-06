package com.projectecho.intelligence.presentation;

import com.projectecho.intelligence.domain.ReasoningCard;
import java.time.Instant;
import java.util.UUID;

public record ReasoningCardResponse(
        UUID id,
        UUID passportId,
        UUID missionId,
        int confidenceScore,
        String summary,
        Instant createdAt,
        Instant updatedAt) {

    public static ReasoningCardResponse from(final ReasoningCard card) {
        return new ReasoningCardResponse(
                card.getId(),
                card.getPassportId().value(),
                card.getMissionId().value(),
                card.getConfidenceScore().percentage(),
                card.getSummary().text(),
                card.getCreatedAt(),
                card.getUpdatedAt());
    }
}
