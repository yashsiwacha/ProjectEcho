package com.projectecho.evidence.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "evidence_lineage")
public class EvidenceLineageEntity {

  @Id private UUID id;

  @Column(name = "person_id", nullable = false)
  private UUID personId;

  @Column(name = "capability_id", nullable = false)
  private UUID capabilityId;

  @Version
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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public List<EvidenceVersionEntity> getVersions() {
    return versions;
  }
}
