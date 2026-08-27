package com.projectecho.evidence.presentation;

import com.projectecho.evidence.application.EvidenceApplicationService;
import com.projectecho.evidence.domain.SourceURI;
import com.projectecho.evidence.domain.TrustTier;
import com.projectecho.evidence.domain.ValidationStatus;
import com.projectecho.shared.domain.PassportId;
import com.projectecho.shared.domain.SkillId;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/evidence")
public class EvidenceController {

    private final EvidenceApplicationService service;
    private final com.projectecho.application.controller.security.ResourceOwnershipService
            ownershipService;

    public EvidenceController(
            final EvidenceApplicationService service,
            final com.projectecho.application.controller.security.ResourceOwnershipService
                    ownershipService) {
        this.service = service;
        this.ownershipService = ownershipService;
    }

    private void verifyOwnership(final UUID passportId, final java.security.Principal principal) {
        ownershipService.verifyPassportOwnership(passportId, principal);
    }

    @PostMapping
    public ResponseEntity<EvidenceResponse> submit(
            @Valid @RequestBody final SubmitEvidenceRequest request,
            final java.security.Principal principal) {
        verifyOwnership(request.passportId(), principal);
        final UUID id =
                service.submitEvidence(
                        new PassportId(request.passportId()),
                        new SkillId(request.skillId()),
                        new SourceURI(request.sourceUri()));
        final EvidenceResponse response = EvidenceResponse.from(service.findById(id));
        return ResponseEntity.created(URI.create("/api/v1/evidence/" + id)).body(response);
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<EvidenceResponse> verify(
            @PathVariable final UUID id, @Valid @RequestBody final VerifyEvidenceRequest request) {
        // Admin only in real app, skipping Principal check for verification service for now
        service.verifyEvidence(id, TrustTier.valueOf(request.trustTier()));
        return ResponseEntity.ok(EvidenceResponse.from(service.findById(id)));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<EvidenceResponse> reject(@PathVariable final UUID id) {
        service.rejectEvidence(id);
        return ResponseEntity.ok(EvidenceResponse.from(service.findById(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvidenceResponse> findById(
            @PathVariable final UUID id, final java.security.Principal principal) {
        final com.projectecho.evidence.domain.EvidenceClaim claim = service.findById(id);
        verifyOwnership(claim.getPassportId().value(), principal);
        return ResponseEntity.ok(EvidenceResponse.from(claim));
    }

    @GetMapping
    public ResponseEntity<Page<EvidenceResponse>> findAll(
            @PageableDefault(size = 20) final Pageable pageable,
            @RequestParam(required = false) final UUID passportId,
            @RequestParam(required = false) final String status,
            final java.security.Principal principal) {

        UUID effectivePassportId = passportId;
        if (effectivePassportId == null && principal != null) {
            effectivePassportId = ownershipService.getPassportIdByPrincipal(principal);
        }

        final Page<EvidenceResponse> page;
        if (effectivePassportId != null && status != null) {
            page =
                    service.findByPassportIdAndStatus(
                                    new PassportId(effectivePassportId),
                                    ValidationStatus.valueOf(status),
                                    pageable)
                            .map(EvidenceResponse::from);
        } else if (effectivePassportId != null) {
            page =
                    service.findByPassportId(new PassportId(effectivePassportId), pageable)
                            .map(EvidenceResponse::from);
        } else if (status != null) {
            page =
                    service.findByStatus(ValidationStatus.valueOf(status), pageable)
                            .map(EvidenceResponse::from);
        } else {
            page = service.findAll(pageable).map(EvidenceResponse::from);
        }
        return ResponseEntity.ok(page);
    }
}
