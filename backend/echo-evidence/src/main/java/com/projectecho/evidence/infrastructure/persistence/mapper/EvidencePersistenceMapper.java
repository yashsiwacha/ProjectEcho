package com.projectecho.evidence.infrastructure.persistence.mapper;

import com.projectecho.common.valueobject.Identifier;
import com.projectecho.common.valueobject.Timestamp;
import com.projectecho.evidence.domain.model.Confidence;
import com.projectecho.evidence.domain.model.EvidenceLineage;
import com.projectecho.evidence.domain.model.EvidenceVersion;
import com.projectecho.evidence.domain.model.Provenance;
import com.projectecho.evidence.infrastructure.persistence.entity.EvidenceLineageEntity;
import com.projectecho.evidence.infrastructure.persistence.entity.EvidenceVersionEntity;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class EvidencePersistenceMapper {

  public static EvidenceLineageEntity toEntity(EvidenceLineage domain) {
    EvidenceLineageEntity entity =
        new EvidenceLineageEntity(
            domain.getId().value(),
            domain.getPersonId().value(),
            domain.getCapabilityId().value(),
            domain.getVersion());

    for (EvidenceVersion version : domain.getVersions()) {
      EvidenceVersionEntity versionEntity =
          new EvidenceVersionEntity(
              version.getId().value(),
              version.getSequenceNumber(),
              version.getProvenance().name(),
              java.math.BigDecimal.valueOf(version.getConfidence().value()),
              version.getArtifactUrl());
      entity.addVersion(versionEntity);
    }

    return entity;
  }

  public static EvidenceLineage toDomain(EvidenceLineageEntity entity) {
    try {
      // Use reflection to bypass private constructors and keep Domain pure
      Constructor<EvidenceLineage> lineageConstructor =
          EvidenceLineage.class.getDeclaredConstructor(
              Identifier.class, Identifier.class, Identifier.class, int.class, Timestamp.class);
      lineageConstructor.setAccessible(true);

      EvidenceLineage lineage =
          lineageConstructor.newInstance(
              new Identifier(entity.getId()),
              new Identifier(entity.getPersonId()),
              new Identifier(entity.getCapabilityId()),
              entity.getVersion(),
              Timestamp.from(entity.getCreatedAt()));

      Constructor<EvidenceVersion> versionConstructor =
          EvidenceVersion.class.getDeclaredConstructor(
              Identifier.class,
              int.class,
              Provenance.class,
              Confidence.class,
              String.class,
              Timestamp.class);
      versionConstructor.setAccessible(true);

      Field versionsField = EvidenceLineage.class.getDeclaredField("versions");
      versionsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      List<EvidenceVersion> domainVersions = (List<EvidenceVersion>) versionsField.get(lineage);

      for (EvidenceVersionEntity versionEntity : entity.getVersions()) {
        EvidenceVersion domainVersion =
            versionConstructor.newInstance(
                new Identifier(versionEntity.getId()),
                versionEntity.getSequenceNumber(),
                Provenance.valueOf(versionEntity.getProvenance()),
                Confidence.of(versionEntity.getConfidence().doubleValue()),
                versionEntity.getArtifactUrl(),
                Timestamp.from(versionEntity.getCreatedAt()));
        domainVersions.add(domainVersion);
      }

      return lineage;
    } catch (Exception e) {
      throw new RuntimeException("Failed to map EvidenceLineage entity to domain", e);
    }
  }
}
