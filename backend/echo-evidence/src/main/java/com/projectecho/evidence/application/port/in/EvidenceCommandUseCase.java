package com.projectecho.evidence.application.port.in;

import com.projectecho.evidence.application.command.AppendEvidenceCommand;
import com.projectecho.evidence.application.command.CreateEvidenceLineageCommand;

/** Inbound port for Evidence commands. */
public interface EvidenceCommandUseCase {

  void createLineage(CreateEvidenceLineageCommand command);

  void appendEvidence(AppendEvidenceCommand command);
}
