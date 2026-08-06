package com.projectecho.evidence.domain;

import com.projectecho.shared.domain.AggregateRoot;
import com.projectecho.shared.domain.PassportId;
import com.projectecho.shared.domain.SkillId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "evidence_claims")
public class EvidenceClaim extends AggregateRoot {

    @Column(nullable = false)
    private PassportId passportId;

    @Column(nullable = false)
    private SkillId skillId;

    @Column(nullable = false)
    private SourceURI sourceUri;

    @Column(nullable = false)
    private ValidationStatus validationStatus;

    @Column(nullable = false)
    private TrustTier trustTier;

    protected EvidenceClaim() {
        super();
        // JPA
    }

    public EvidenceClaim(
            final UUID id,
            final PassportId passportId,
            final SkillId skillId,
            final SourceURI sourceUri) {
        super(id);
        this.passportId = Objects.requireNonNull(passportId, "PassportId cannot be null");
        this.skillId = Objects.requireNonNull(skillId, "SkillId cannot be null");
        this.sourceUri = Objects.requireNonNull(sourceUri, "SourceURI cannot be null");
        this.validationStatus = ValidationStatus.PENDING;
        this.trustTier = TrustTier.TIER_1; // Default
    }

    public PassportId getPassportId() {
        return passportId;
    }

    public SkillId getSkillId() {
        return skillId;
    }

    public SourceURI getSourceUri() {
        return sourceUri;
    }

    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public TrustTier getTrustTier() {
        return trustTier;
    }

    public void verify(final TrustTier assignedTier) {
        if (this.validationStatus == ValidationStatus.REJECTED) {
            throw new IllegalStateException("Cannot verify a rejected claim");
        }
        this.validationStatus = ValidationStatus.VERIFIED;
        this.trustTier = Objects.requireNonNull(assignedTier, "TrustTier cannot be null");
    }

    public void reject() {
        this.validationStatus = ValidationStatus.REJECTED;
        this.trustTier = TrustTier.TIER_1;
    }
}
