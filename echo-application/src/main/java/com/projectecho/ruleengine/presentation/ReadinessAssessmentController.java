package com.projectecho.ruleengine.presentation;

import com.projectecho.ruleengine.application.RuleEngineService;
import com.projectecho.ruleengine.domain.BusinessRule;
import com.projectecho.ruleengine.domain.DecisionGraph;
import com.projectecho.ruleengine.domain.MissionStateSnapshot;
import com.projectecho.ruleengine.domain.PassportStateSnapshot;
import com.projectecho.ruleengine.domain.ReadinessAssessment;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assessments")
public class ReadinessAssessmentController {

    private final RuleEngineService service;

    public ReadinessAssessmentController(final RuleEngineService service) {
        this.service = service;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<ReadinessAssessmentResponse> evaluate(
            @Valid @RequestBody final EvaluateReadinessRequest request) {

        final PassportStateSnapshot passport =
                new PassportStateSnapshot(
                        request.passportId(),
                        request.passportSkills() != null
                                ? request.passportSkills()
                                : Collections.emptySet(),
                        request.isPassportVerified());

        final MissionStateSnapshot mission =
                new MissionStateSnapshot(
                        request.missionId(),
                        request.missionRequiredSkills() != null
                                ? request.missionRequiredSkills()
                                : Collections.emptySet(),
                        request.isMissionActive());

        final BusinessRule defaultRule = new StandardReadinessRule();

        final ReadinessAssessment assessment = service.evaluate(passport, mission, defaultRule);
        final ReadinessAssessmentResponse response = ReadinessAssessmentResponse.from(assessment);

        return ResponseEntity.created(URI.create("/api/v1/assessments/" + assessment.getId()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadinessAssessmentResponse> findById(@PathVariable final UUID id) {
        return service.findById(id)
                .map(assessment -> ResponseEntity.ok(ReadinessAssessmentResponse.from(assessment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<ReadinessAssessmentResponse>> findAll(
            @PageableDefault(size = 20) final Pageable pageable,
            @RequestParam(required = false) final UUID passportId,
            @RequestParam(required = false) final UUID missionId) {
        final Page<ReadinessAssessmentResponse> page;
        if (passportId != null && missionId != null) {
            page =
                    service.findByPassportIdAndMissionId(passportId, missionId, pageable)
                            .map(ReadinessAssessmentResponse::from);
        } else if (passportId != null) {
            page =
                    service.findByPassportId(passportId, pageable)
                            .map(ReadinessAssessmentResponse::from);
        } else if (missionId != null) {
            page =
                    service.findByMissionId(missionId, pageable)
                            .map(ReadinessAssessmentResponse::from);
        } else {
            page = service.findAll(pageable).map(ReadinessAssessmentResponse::from);
        }
        return ResponseEntity.ok(page);
    }

    private static class StandardReadinessRule implements BusinessRule {
        private final UUID ruleId = UUID.randomUUID();

        @Override
        public UUID getRuleId() {
            return ruleId;
        }

        @Override
        public String getDescription() {
            return "Standard Skill Matching Readiness Rule";
        }

        @Override
        public DecisionGraph evaluate(
                final PassportStateSnapshot passport, final MissionStateSnapshot mission) {
            final boolean eligible =
                    passport.isVerified()
                            && mission.isActive()
                            && passport.skills().containsAll(mission.requiredSkills());
            final int score = eligible ? 100 : passport.skills().isEmpty() ? 0 : 50;

            return new DecisionGraph(
                    UUID.randomUUID(),
                    Instant.now(),
                    ruleId,
                    passport.passportId(),
                    mission.missionId(),
                    List.of(),
                    List.of(),
                    eligible,
                    score,
                    "Evaluated standard readiness",
                    UUID.randomUUID());
        }
    }
}
