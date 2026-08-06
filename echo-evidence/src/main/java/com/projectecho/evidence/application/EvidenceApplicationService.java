package com.projectecho.evidence.application;

import com.projectecho.evidence.domain.EvidenceClaim;
import com.projectecho.evidence.domain.EvidenceClaimRepository;
import com.projectecho.evidence.domain.SourceURI;
import com.projectecho.evidence.domain.TrustTier;
import com.projectecho.evidence.domain.ValidationStatus;
import com.projectecho.shared.domain.PassportId;
import com.projectecho.shared.domain.SkillId;
import com.projectecho.shared.events.DomainEventPublisher;
import com.projectecho.shared.events.TrustTierAssessedEvent;
import com.projectecho.shared.exception.ResourceNotFoundException;
import java.time.Instant;
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
public class EvidenceApplicationService implements SubmitEvidenceUseCase, VerifyEvidenceUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(EvidenceApplicationService.class);
    private final EvidenceClaimRepository repository;
    private final DomainEventPublisher eventPublisher;

    public EvidenceApplicationService(
            final EvidenceClaimRepository repository, final DomainEventPublisher eventPublisher) {
        this.repository = Objects.requireNonNull(repository);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public UUID submitEvidence(
            final PassportId passportId, final SkillId skillId, final SourceURI sourceUri) {

        final UUID id = UUID.randomUUID();
        final EvidenceClaim claim = new EvidenceClaim(id, passportId, skillId, sourceUri);
        repository.save(claim);

        if (LOG.isInfoEnabled()) {
            LOG.info("Evidence submitted: {} for passport {}", id, passportId.value());
        }

        return id;
    }

    @Override
    public void verifyEvidence(final UUID evidenceId, final TrustTier assignedTier) {
        final EvidenceClaim claim = findClaimById(evidenceId);
        claim.verify(assignedTier);
        repository.save(claim);

        if (LOG.isInfoEnabled()) {
            LOG.info("Evidence verified: {} with tier {}", evidenceId, assignedTier);
        }

        eventPublisher.publish(
                new TrustTierAssessedEvent(
                        UUID.randomUUID(),
                        1,
                        evidenceId,
                        evidenceId,
                        Instant.now(),
                        evidenceId,
                        claim.getPassportId().value(),
                        assignedTier.name()));
    }

    @Override
    public void rejectEvidence(final UUID evidenceId) {
        final EvidenceClaim claim = findClaimById(evidenceId);
        claim.reject();
        repository.save(claim);

        if (LOG.isInfoEnabled()) {
            LOG.info("Evidence rejected: {}", evidenceId);
        }
    }

    @Transactional(readOnly = true)
    public EvidenceClaim findById(final UUID evidenceId) {
        return findClaimById(evidenceId);
    }

    @Transactional(readOnly = true)
    public Page<EvidenceClaim> findAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<EvidenceClaim> findByPassportId(
            final PassportId passportId, final Pageable pageable) {
        return repository.findByPassportId(passportId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<EvidenceClaim> findByStatus(
            final ValidationStatus status, final Pageable pageable) {
        return repository.findByValidationStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<EvidenceClaim> findByPassportIdAndStatus(
            final PassportId passportId, final ValidationStatus status, final Pageable pageable) {
        return repository.findByPassportIdAndValidationStatus(passportId, status, pageable);
    }

    private EvidenceClaim findClaimById(final UUID evidenceId) {
        return repository
                .findById(evidenceId)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Evidence claim not found: " + evidenceId));
    }
}
