package com.projectecho.evidence.domain.event;

import com.projectecho.common.event.BaseDomainEvent;
import com.projectecho.common.valueobject.Identifier;

/** Domain event emitted when an EvidenceLineage is appended to with a new version. */
public class EvidenceLineageAppendedEvent extends BaseDomainEvent {

  private final Identifier lineageId;
  private final Identifier newVersionId;
  private final int sequenceNumber;

  public EvidenceLineageAppendedEvent(
      Identifier lineageId, Identifier newVersionId, int sequenceNumber) {
    super();
    this.lineageId = lineageId;
    this.newVersionId = newVersionId;
    this.sequenceNumber = sequenceNumber;
  }

  public Identifier getLineageId() {
    return lineageId;
  }

  public Identifier getNewVersionId() {
    return newVersionId;
  }

  public int getSequenceNumber() {
    return sequenceNumber;
  }
}
