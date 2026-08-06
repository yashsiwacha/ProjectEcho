package com.projectecho.ruleengine.application;

import com.projectecho.ruleengine.domain.BusinessRule;
import com.projectecho.ruleengine.domain.MissionStateSnapshot;
import com.projectecho.ruleengine.domain.PassportStateSnapshot;
import com.projectecho.ruleengine.domain.ReadinessAssessment;
import com.projectecho.ruleengine.domain.ReadinessAssessmentRepository;
import com.projectecho.shared.events.DomainEventPublisher;
import com.projectecho.shared.events.IntegrationEvent;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RuleEngineService {

    private final ReadinessAssessmentRepository repository;
    private final DomainEventPublisher domainEventPublisher;

    public RuleEngineService(
            final ReadinessAssessmentRepository repository,
            final DomainEventPublisher domainEventPublisher) {
        this.repository = repository;
        this.domainEventPublisher = domainEventPublisher;
    }

    public ReadinessAssessment evaluate(
            final PassportStateSnapshot passport,
            final MissionStateSnapshot mission,
            final BusinessRule rule) {

        final ReadinessAssessment assessment = ReadinessAssessment.assess(passport, mission, rule);
        repository.save(assessment);

        assessment
                .getDomainEvents()
                .forEach(
                        event -> {
                            if (event instanceof IntegrationEvent integrationEvent) {
                                domainEventPublisher.publish(integrationEvent);
                            }
                        });
        assessment.clearDomainEvents();

        return assessment;
    }

    @Transactional(readOnly = true)
    public Optional<ReadinessAssessment> findById(final UUID id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<ReadinessAssessment> findAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ReadinessAssessment> findByPassportId(
            final UUID passportId, final Pageable pageable) {
        return repository.findByPassportId(passportId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ReadinessAssessment> findByMissionId(
            final UUID missionId, final Pageable pageable) {
        return repository.findByMissionId(missionId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ReadinessAssessment> findByPassportIdAndMissionId(
            final UUID passportId, final UUID missionId, final Pageable pageable) {
        return repository.findByPassportIdAndMissionId(passportId, missionId, pageable);
    }
}
