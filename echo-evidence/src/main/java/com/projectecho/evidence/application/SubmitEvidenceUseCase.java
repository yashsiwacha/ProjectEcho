package com.projectecho.evidence.application;

import com.projectecho.evidence.domain.SourceURI;
import com.projectecho.shared.domain.PassportId;
import com.projectecho.shared.domain.SkillId;
import java.util.UUID;

public interface SubmitEvidenceUseCase {
    UUID submitEvidence(PassportId passportId, SkillId skillId, SourceURI sourceUri);
}
