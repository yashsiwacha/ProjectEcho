package com.projectecho.mission.presentation;

import com.projectecho.mission.application.MissionApplicationService;
import com.projectecho.mission.domain.MissionStatus;
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
@RequestMapping("/api/v1/missions")
public class MissionController {

    private final MissionApplicationService service;
    private final com.projectecho.application.controller.security.ResourceOwnershipService
            ownershipService;

    public MissionController(
            final MissionApplicationService service,
            final com.projectecho.application.controller.security.ResourceOwnershipService
                    ownershipService) {
        this.service = service;
        this.ownershipService = ownershipService;
    }

    private void verifyOwnership(final UUID passportId, final java.security.Principal principal) {
        ownershipService.verifyPassportOwnership(passportId, principal);
    }

    @PostMapping
    public ResponseEntity<MissionResponse> create(
            @Valid @RequestBody final CreateMissionRequest request,
            final java.security.Principal principal) {
        // Authenticated user creates a mission for their own passport
        final UUID defaultPassportId = ownershipService.getPassportIdByPrincipal(principal);
        final UUID id = service.createMission(request.title(), defaultPassportId);
        final MissionResponse response = MissionResponse.from(service.findById(id));
        return ResponseEntity.created(URI.create("/api/v1/missions/" + id)).body(response);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<MissionResponse> activate(
            @PathVariable final UUID id, final java.security.Principal principal) {
        final com.projectecho.mission.domain.Mission mission = service.findById(id);
        verifyOwnership(mission.getPassportId(), principal);
        service.activateMission(id);
        return ResponseEntity.ok(MissionResponse.from(service.findById(id)));
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<MissionResponse> archive(
            @PathVariable final UUID id, final java.security.Principal principal) {
        final com.projectecho.mission.domain.Mission mission = service.findById(id);
        verifyOwnership(mission.getPassportId(), principal);
        service.archiveMission(id);
        return ResponseEntity.ok(MissionResponse.from(service.findById(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionResponse> findById(
            @PathVariable final UUID id, final java.security.Principal principal) {
        final com.projectecho.mission.domain.Mission mission = service.findById(id);
        verifyOwnership(mission.getPassportId(), principal);
        return ResponseEntity.ok(MissionResponse.from(mission));
    }

    @GetMapping
    public ResponseEntity<Page<MissionResponse>> findAll(
            @PageableDefault(size = 20) final Pageable pageable,
            @RequestParam(required = false) final String status,
            final java.security.Principal principal) {

        final ResponseEntity<Page<MissionResponse>> response;
        if (principal == null) {
            response = ResponseEntity.ok(Page.empty(pageable));
        } else {
            final UUID passportId = ownershipService.getPassportIdByPrincipal(principal);
            final Page<MissionResponse> page;
            if (status != null && !status.isBlank()) {
                page =
                        service.findByPassportIdAndStatus(
                                        passportId, MissionStatus.valueOf(status), pageable)
                                .map(MissionResponse::from);
            } else {
                page = service.findByPassportId(passportId, pageable).map(MissionResponse::from);
            }
            response = ResponseEntity.ok(page);
        }
        return response;
    }
}
