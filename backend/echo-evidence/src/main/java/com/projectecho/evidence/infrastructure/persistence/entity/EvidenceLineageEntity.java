package com.projectecho.evidence.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Infrastructure representation of the EvidenceLineage aggregate.
 * <p>
 * <b>CRITICAL ARCHITECTURAL DECISION: DO NOT ADD @Version to this entity.</b>
 * <br>
 * This aggregate relies on a manual atomic update query for optimistic locking
 * rather than JPA's @Version. Because this is an append-only aggregate, modifications 
 * only insert new child EvidenceVersionEntity rows. By default, Hibernate queues 
 * child INSERTs before parent UPDATEs (ActionQueue ordering).
 * <br>
 * If @Version is used, concurrent modifications will trigger a database 
 * ConstraintViolationException (on uq_lineage_sequence) BEFORE the optimistic 
 * lock check evaluates, resulting in an unhandled infrastructure exception 
 * instead of the required ObjectOptimisticLockingFailureException.
 * <br>
 * Concurrency is instead managed via {@code incrementVersion} in the Repository Adapter.
 * </p>
 */
@Entity
@Table(name = "evidence_lineage")
public class EvidenceLineageEntity {

  @Id private UUID id;

  @Column(name = "person_id", nullable = false)
  private UUID personId;

  @Column(name = "capability_id", nullable = false)
  private UUID capabilityId;

  @Column(nullable = false)
  private Integer version;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @OneToMany(
      mappedBy = "lineage",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @OrderBy("sequenceNumber ASC")
  private List<EvidenceVersionEntity> versions = new ArrayList<>();

  protected EvidenceLineageEntity() {}

  public EvidenceLineageEntity(UUID id, UUID personId, UUID capabilityId, Integer version) {
    this.id = id;
    this.personId = personId;
    this.capabilityId = capabilityId;
    this.version = version;
    this.createdAt = Instant.now();
  }

  public void addVersion(EvidenceVersionEntity versionEntity) {
    versions.add(versionEntity);
    versionEntity.setLineage(this);
  }

  public UUID getId() {
    return id;
  }

  public UUID getPersonId() {
    return personId;
  }

  public UUID getCapabilityId() {
    return capabilityId;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public List<EvidenceVersionEntity> getVersions() {
    return versions;
  }
}
