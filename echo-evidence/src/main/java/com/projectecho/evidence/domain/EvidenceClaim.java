package com.projectecho.evidence.domain;

import com.projectecho.shared.domain.AggregateRoot;
import com.projectecho.shared.domain.Description;
import com.projectecho.shared.domain.PassportId;
import com.projectecho.shared.domain.SkillId;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @Embedded private Description description;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "embedding")
    private Double[] embedding;

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
            final SourceURI sourceUri,
            final Description description) {
        super(id);
        this.passportId = Objects.requireNonNull(passportId, "PassportId cannot be null");
        this.skillId = Objects.requireNonNull(skillId, "SkillId cannot be null");
        this.sourceUri = Objects.requireNonNull(sourceUri, "SourceURI cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.validationStatus = ValidationStatus.PENDING;
        this.trustTier = TrustTier.TIER_1; // Default
    }

    public EvidenceClaim(
            final UUID id,
            final PassportId passportId,
            final SkillId skillId,
            final SourceURI sourceUri) {
        this(id, passportId, skillId, sourceUri, new Description("No description provided"));
    }

    public Description getDescription() {
        return description;
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
