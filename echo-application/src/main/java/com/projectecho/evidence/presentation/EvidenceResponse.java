package com.projectecho.evidence.presentation;

import com.projectecho.evidence.domain.EvidenceClaim;
import java.time.Instant;
import java.util.UUID;

public record EvidenceResponse(
        UUID id,
        UUID passportId,
        UUID skillId,
        String sourceUri,
        String validationStatus,
        String trustTier,
        Instant createdAt,
        Instant updatedAt) {

    public static EvidenceResponse from(final EvidenceClaim claim) {
        return new EvidenceResponse(
                claim.getId(),
                claim.getPassportId().value(),
                claim.getSkillId().value(),
                claim.getSourceUri().value(),
                claim.getValidationStatus().name(),
                claim.getTrustTier().name(),
                claim.getCreatedAt(),
                claim.getUpdatedAt());
    }
}
