package com.projectecho.evidence.application;

import com.projectecho.evidence.domain.TrustTier;
import java.util.UUID;

public interface VerifyEvidenceUseCase {
    void verifyEvidence(UUID evidenceId, TrustTier assignedTier);

    void rejectEvidence(UUID evidenceId);
}
