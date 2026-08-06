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

    public MissionController(final MissionApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MissionResponse> create(
            @Valid @RequestBody final CreateMissionRequest request) {
        final UUID id = service.createMission(request.title());
        final MissionResponse response = MissionResponse.from(service.findById(id));
        return ResponseEntity.created(URI.create("/api/v1/missions/" + id)).body(response);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<MissionResponse> activate(@PathVariable final UUID id) {
        service.activateMission(id);
        return ResponseEntity.ok(MissionResponse.from(service.findById(id)));
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<MissionResponse> archive(@PathVariable final UUID id) {
        service.archiveMission(id);
        return ResponseEntity.ok(MissionResponse.from(service.findById(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionResponse> findById(@PathVariable final UUID id) {
        return ResponseEntity.ok(MissionResponse.from(service.findById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<MissionResponse>> findAll(
            @PageableDefault(size = 20) final Pageable pageable,
            @RequestParam(required = false) final String status) {
        final Page<MissionResponse> page;
        if (status != null && !status.isBlank()) {
            page =
                    service.findByStatus(MissionStatus.valueOf(status), pageable)
                            .map(MissionResponse::from);
        } else {
            page = service.findAll(pageable).map(MissionResponse::from);
        }
        return ResponseEntity.ok(page);
    }
}
