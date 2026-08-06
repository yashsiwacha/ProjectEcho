package com.projectecho.evidence.application.service;

import com.projectecho.common.exception.BusinessRuleViolationException;
import com.projectecho.common.valueobject.Identifier;
import com.projectecho.evidence.application.command.AppendEvidenceCommand;
import com.projectecho.evidence.application.command.CreateEvidenceLineageCommand;
import com.projectecho.evidence.application.port.in.EvidenceCommandUseCase;
import com.projectecho.shared.events.DomainEventPublisher;
import com.projectecho.evidence.domain.model.Confidence;
import com.projectecho.evidence.domain.model.EvidenceLineage;
import com.projectecho.evidence.domain.model.Provenance;
import com.projectecho.evidence.domain.repository.EvidenceLineageRepository;

/**
 * Application service for the Evidence bounded context. Purely orchestrates domain behavior and
 * persistence. Contains no business rules.
 */
public class EvidenceCommandService implements EvidenceCommandUseCase {

  private final EvidenceLineageRepository repository;
  private final DomainEventPublisher eventPublisher;

  public EvidenceCommandService(
      EvidenceLineageRepository repository, DomainEventPublisher eventPublisher) {
    this.repository = repository;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void createLineage(CreateEvidenceLineageCommand command) {
    // Map DTO primitives to Domain Value Objects
    Identifier personId = new Identifier(command.personId());
    Identifier capabilityId = new Identifier(command.capabilityId());
    Provenance provenance = Provenance.valueOf(command.provenance());
    Confidence confidence = Confidence.of(command.confidence());

    // Delegate to Domain Aggregate
    EvidenceLineage lineage =
        EvidenceLineage.create(
            personId, capabilityId, provenance, confidence, command.artifactUrl());

    // Persist
    repository.save(lineage);

    // Publish Events
    eventPublisher.publishAll(lineage.getDomainEvents());
    lineage.clearDomainEvents();
  }

  @Override
  public void appendEvidence(AppendEvidenceCommand command) {
    Identifier lineageId = new Identifier(command.lineageId());

    // Fetch Aggregate
    EvidenceLineage lineage =
        repository
            .findById(lineageId)
            .orElseThrow(() -> new BusinessRuleViolationException("Evidence lineage not found"));

    // Map DTO to Value Objects
    Provenance provenance = Provenance.valueOf(command.provenance());
    Confidence confidence = Confidence.of(command.confidence());

    // Delegate to Domain
    lineage.append(provenance, confidence, command.artifactUrl());

    // Persist (Optimistic lock handles concurrency)
    repository.save(lineage);

    // Publish Events
    eventPublisher.publishAll(lineage.getDomainEvents());
    lineage.clearDomainEvents();
  }
}
