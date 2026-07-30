package com.projectecho.evidence.domain.event;

import com.projectecho.common.event.BaseDomainEvent;
import com.projectecho.common.valueobject.Identifier;

/** Domain event emitted when a new EvidenceLineage is created. */
public class EvidenceLineageCreatedEvent extends BaseDomainEvent {

  private final Identifier lineageId;
  private final Identifier personId;
  private final Identifier capabilityId;

  public EvidenceLineageCreatedEvent(
      Identifier lineageId, Identifier personId, Identifier capabilityId) {
    super();
    this.lineageId = lineageId;
    this.personId = personId;
    this.capabilityId = capabilityId;
  }

  public Identifier getLineageId() {
    return lineageId;
  }

  public Identifier getPersonId() {
    return personId;
  }

  public Identifier getCapabilityId() {
    return capabilityId;
  }
}
