package com.projectecho.intelligence.presentation;

import com.projectecho.intelligence.application.IntelligenceService;
import com.projectecho.shared.domain.MissionId;
import com.projectecho.shared.domain.PassportId;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reasoning-cards")
public class ReasoningCardController {

    private final IntelligenceService service;

    public ReasoningCardController(final IntelligenceService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReasoningCardResponse> findById(@PathVariable final UUID id) {
        return ResponseEntity.ok(ReasoningCardResponse.from(service.findById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<ReasoningCardResponse>> findAll(
            @PageableDefault(size = 20) final Pageable pageable,
            @RequestParam(required = false) final UUID passportId,
            @RequestParam(required = false) final UUID missionId) {
        final Page<ReasoningCardResponse> page;
        if (passportId != null) {
            page =
                    service.findByPassportId(new PassportId(passportId), pageable)
                            .map(ReasoningCardResponse::from);
        } else if (missionId != null) {
            page =
                    service.findByMissionId(new MissionId(missionId), pageable)
                            .map(ReasoningCardResponse::from);
        } else {
            page = service.findAll(pageable).map(ReasoningCardResponse::from);
        }
        return ResponseEntity.ok(page);
    }
}
