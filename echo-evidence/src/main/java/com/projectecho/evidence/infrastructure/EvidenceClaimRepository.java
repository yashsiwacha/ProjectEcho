package com.projectecho.evidence.infrastructure;

import com.projectecho.evidence.domain.EvidenceClaim;
import com.projectecho.shared.domain.PassportId;
import com.projectecho.shared.domain.SkillId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EvidenceClaimRepository {
    void save(EvidenceClaim evidence);

    Optional<EvidenceClaim> findById(UUID id);

    List<EvidenceClaim> findByPassportId(PassportId passportId);

    List<EvidenceClaim> findBySkillId(SkillId skillId);
}
