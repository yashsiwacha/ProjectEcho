package com.projectecho.evidence.domain;

import com.projectecho.shared.domain.PassportId;
import com.projectecho.shared.domain.SkillId;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvidenceClaimRepository extends JpaRepository<EvidenceClaim, UUID> {

    Page<EvidenceClaim> findByPassportId(PassportId passportId, Pageable pageable);

    Page<EvidenceClaim> findBySkillId(SkillId skillId, Pageable pageable);

    Page<EvidenceClaim> findByValidationStatus(ValidationStatus status, Pageable pageable);

    Page<EvidenceClaim> findByPassportIdAndValidationStatus(
            PassportId passportId, ValidationStatus status, Pageable pageable);
}
