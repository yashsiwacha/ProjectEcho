package com.projectecho.evidence.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evidence_version")
public class EvidenceVersionEntity {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lineage_id", nullable = false)
  private EvidenceLineageEntity lineage;

  @Column(name = "sequence_number", nullable = false)
  private Integer sequenceNumber;

  @Column(nullable = false, length = 50)
  private String provenance;

  @Column(nullable = false, precision = 3, scale = 2)
  private BigDecimal confidence;

  @Column(name = "artifact_url", nullable = false, length = 1024)
  private String artifactUrl;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  protected EvidenceVersionEntity() {}

  public EvidenceVersionEntity(
      UUID id,
      Integer sequenceNumber,
      String provenance,
      BigDecimal confidence,
      String artifactUrl) {
    this.id = id;
    this.sequenceNumber = sequenceNumber;
    this.provenance = provenance;
    this.confidence = confidence;
    this.artifactUrl = artifactUrl;
    this.createdAt = Instant.now();
  }

  public void setLineage(EvidenceLineageEntity lineage) {
    this.lineage = lineage;
  }

  public UUID getId() {
    return id;
  }

  public EvidenceLineageEntity getLineage() {
    return lineage;
  }

  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  public String getProvenance() {
    return provenance;
  }

  public BigDecimal getConfidence() {
    return confidence;
  }

  public String getArtifactUrl() {
    return artifactUrl;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
