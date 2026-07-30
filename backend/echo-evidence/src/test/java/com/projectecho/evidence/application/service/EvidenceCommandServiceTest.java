package com.projectecho.evidence.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.projectecho.common.exception.BusinessRuleViolationException;
import com.projectecho.common.valueobject.Identifier;
import com.projectecho.evidence.application.command.AppendEvidenceCommand;
import com.projectecho.evidence.application.command.CreateEvidenceLineageCommand;
import com.projectecho.evidence.application.port.out.DomainEventPublisher;
import com.projectecho.evidence.domain.model.Confidence;
import com.projectecho.evidence.domain.model.EvidenceLineage;
import com.projectecho.evidence.domain.model.Provenance;
import com.projectecho.evidence.domain.repository.EvidenceLineageRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EvidenceCommandServiceTest {

  @Mock private EvidenceLineageRepository repository;

  @Mock private DomainEventPublisher eventPublisher;

  private EvidenceCommandService service;

  @BeforeEach
  void setUp() {
    service = new EvidenceCommandService(repository, eventPublisher);
  }

  @Test
  void shouldCreateLineage() {
    UUID personId = UUID.randomUUID();
    UUID capabilityId = UUID.randomUUID();
    CreateEvidenceLineageCommand command =
        new CreateEvidenceLineageCommand(personId, capabilityId, "SELF_REPORTED", 0.5, "url");

    when(repository.save(any(EvidenceLineage.class))).thenAnswer(i -> i.getArgument(0));

    service.createLineage(command);

    ArgumentCaptor<EvidenceLineage> lineageCaptor = ArgumentCaptor.forClass(EvidenceLineage.class);
    verify(repository).save(lineageCaptor.capture());
    EvidenceLineage savedLineage = lineageCaptor.getValue();

    assertEquals(personId.toString(), savedLineage.getPersonId().value().toString());
    assertEquals(capabilityId.toString(), savedLineage.getCapabilityId().value().toString());

    // Verify events were published
    verify(eventPublisher).publishAll(anyList());
    assertEquals(
        0, savedLineage.getDomainEvents().size(), "Events should be cleared after publishing");
  }

  @Test
  void shouldAppendEvidence() {
    UUID lineageId = UUID.randomUUID();
    EvidenceLineage lineage =
        EvidenceLineage.create(
            Identifier.generate(),
            Identifier.generate(),
            Provenance.SYSTEM_INGESTION,
            Confidence.of(0.5),
            "url");
    lineage.clearDomainEvents(); // Simulate fetched from DB without pending events

    when(repository.findById(any(Identifier.class))).thenReturn(Optional.of(lineage));
    when(repository.save(any(EvidenceLineage.class))).thenAnswer(i -> i.getArgument(0));

    AppendEvidenceCommand command =
        new AppendEvidenceCommand(lineageId, "PEER_VALIDATED", 1.0, "newUrl");

    service.appendEvidence(command);

    verify(repository).findById(any(Identifier.class));
    verify(repository).save(lineage);
    verify(eventPublisher).publishAll(anyList());

    assertEquals(2, lineage.getVersions().size());
    assertEquals(0, lineage.getDomainEvents().size(), "Events should be cleared after publishing");
  }

  @Test
  void shouldThrowExceptionWhenAppendingToUnknownLineage() {
    when(repository.findById(any())).thenReturn(Optional.empty());

    AppendEvidenceCommand command =
        new AppendEvidenceCommand(UUID.randomUUID(), "PEER_VALIDATED", 1.0, "newUrl");

    assertThrows(BusinessRuleViolationException.class, () -> service.appendEvidence(command));

    verify(repository, never()).save(any());
    verify(eventPublisher, never()).publishAll(anyList());
  }
}
