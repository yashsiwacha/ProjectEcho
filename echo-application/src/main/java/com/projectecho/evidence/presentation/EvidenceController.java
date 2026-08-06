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

    public EvidenceController(final EvidenceApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EvidenceResponse> submit(
            @Valid @RequestBody final SubmitEvidenceRequest request) {
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
        service.verifyEvidence(id, TrustTier.valueOf(request.trustTier()));
        return ResponseEntity.ok(EvidenceResponse.from(service.findById(id)));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<EvidenceResponse> reject(@PathVariable final UUID id) {
        service.rejectEvidence(id);
        return ResponseEntity.ok(EvidenceResponse.from(service.findById(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvidenceResponse> findById(@PathVariable final UUID id) {
        return ResponseEntity.ok(EvidenceResponse.from(service.findById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<EvidenceResponse>> findAll(
            @PageableDefault(size = 20) final Pageable pageable,
            @RequestParam(required = false) final UUID passportId,
            @RequestParam(required = false) final String status) {
        final Page<EvidenceResponse> page;
        if (passportId != null && status != null) {
            page =
                    service.findByPassportIdAndStatus(
                                    new PassportId(passportId),
                                    ValidationStatus.valueOf(status),
                                    pageable)
                            .map(EvidenceResponse::from);
        } else if (passportId != null) {
            page =
                    service.findByPassportId(new PassportId(passportId), pageable)
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
