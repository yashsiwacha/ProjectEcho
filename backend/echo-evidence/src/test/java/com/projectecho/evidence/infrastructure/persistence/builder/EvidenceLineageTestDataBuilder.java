package com.projectecho.evidence.infrastructure.persistence.builder;

import com.projectecho.common.valueobject.Identifier;
import com.projectecho.evidence.domain.model.Confidence;
import com.projectecho.evidence.domain.model.EvidenceLineage;
import com.projectecho.evidence.domain.model.Provenance;

public class EvidenceLineageTestDataBuilder {
  private Identifier personId = Identifier.generate();
  private Identifier capabilityId = Identifier.generate();
  private Provenance provenance = Provenance.SYSTEM_INGESTION;
  private Confidence confidence = Confidence.of(0.85);
  private String artifactUrl = "https://github.com/projectecho/test";

  public EvidenceLineageTestDataBuilder withPersonId(Identifier personId) {
    this.personId = personId;
    return this;
  }

  public EvidenceLineageTestDataBuilder withCapabilityId(Identifier capabilityId) {
    this.capabilityId = capabilityId;
    return this;
  }

  public EvidenceLineageTestDataBuilder withProvenance(Provenance provenance) {
    this.provenance = provenance;
    return this;
  }

  public EvidenceLineageTestDataBuilder withConfidence(double confidenceValue) {
    this.confidence = Confidence.of(confidenceValue);
    return this;
  }

  public EvidenceLineageTestDataBuilder withArtifactUrl(String artifactUrl) {
    this.artifactUrl = artifactUrl;
    return this;
  }

  public EvidenceLineage build() {
    return EvidenceLineage.create(personId, capabilityId, provenance, confidence, artifactUrl);
  }
}
