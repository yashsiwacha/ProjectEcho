package com.projectecho.evidence.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.projectecho.shared.domain.PassportId;
import com.projectecho.shared.domain.SkillId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EvidenceClaimTest {

    @Test
    void shouldCreateEvidenceClaim() {
        UUID id = UUID.randomUUID();
        PassportId passportId = new PassportId(UUID.randomUUID());
        SkillId skillId = new SkillId(UUID.randomUUID());
        SourceURI sourceUri = new SourceURI("https://example.com/cert");

        EvidenceClaim claim = new EvidenceClaim(id, passportId, skillId, sourceUri);

        assertEquals(id, claim.getId());
        assertEquals(passportId, claim.getPassportId());
        assertEquals(skillId, claim.getSkillId());
        assertEquals(sourceUri, claim.getSourceUri());
        assertEquals(ValidationStatus.PENDING, claim.getValidationStatus());
        assertEquals(TrustTier.TIER_1, claim.getTrustTier());
    }

    @Test
    void shouldVerifyEvidenceClaim() {
        UUID id = UUID.randomUUID();
        PassportId passportId = new PassportId(UUID.randomUUID());
        SkillId skillId = new SkillId(UUID.randomUUID());
        SourceURI sourceUri = new SourceURI("https://example.com/cert");

        EvidenceClaim claim = new EvidenceClaim(id, passportId, skillId, sourceUri);
        claim.verify(TrustTier.TIER_3);

        assertEquals(ValidationStatus.VERIFIED, claim.getValidationStatus());
        assertEquals(TrustTier.TIER_3, claim.getTrustTier());
    }

    @Test
    void shouldRejectEvidenceClaim() {
        UUID id = UUID.randomUUID();
        PassportId passportId = new PassportId(UUID.randomUUID());
        SkillId skillId = new SkillId(UUID.randomUUID());
        SourceURI sourceUri = new SourceURI("https://example.com/cert");

        EvidenceClaim claim = new EvidenceClaim(id, passportId, skillId, sourceUri);
        claim.reject();

        assertEquals(ValidationStatus.REJECTED, claim.getValidationStatus());
        assertEquals(TrustTier.TIER_1, claim.getTrustTier());
    }

    @Test
    void shouldNotVerifyRejectedClaim() {
        UUID id = UUID.randomUUID();
        PassportId passportId = new PassportId(UUID.randomUUID());
        SkillId skillId = new SkillId(UUID.randomUUID());
        SourceURI sourceUri = new SourceURI("https://example.com/cert");

        EvidenceClaim claim = new EvidenceClaim(id, passportId, skillId, sourceUri);
        claim.reject();

        assertThrows(IllegalStateException.class, () -> claim.verify(TrustTier.TIER_2));
    }

    @Test
    void shouldThrowIfRequiredFieldIsNull() {
        UUID id = UUID.randomUUID();
        PassportId passportId = new PassportId(UUID.randomUUID());
        SkillId skillId = new SkillId(UUID.randomUUID());
        SourceURI sourceUri = new SourceURI("https://example.com/cert");

        assertThrows(
                NullPointerException.class, () -> new EvidenceClaim(id, null, skillId, sourceUri));
        assertThrows(
                NullPointerException.class,
                () -> new EvidenceClaim(id, passportId, null, sourceUri));
        assertThrows(
                NullPointerException.class, () -> new EvidenceClaim(id, passportId, skillId, null));
    }
}
