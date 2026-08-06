package com.projectecho.ruleengine.application;

import com.projectecho.ruleengine.domain.BusinessRule;
import com.projectecho.ruleengine.domain.MissionStateSnapshot;
import com.projectecho.ruleengine.domain.PassportStateSnapshot;
import com.projectecho.ruleengine.domain.ReadinessAssessment;
import com.projectecho.ruleengine.domain.ReadinessAssessmentRepository;
import com.projectecho.shared.events.DomainEventPublisher;
import com.projectecho.shared.events.IntegrationEvent;
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
}
