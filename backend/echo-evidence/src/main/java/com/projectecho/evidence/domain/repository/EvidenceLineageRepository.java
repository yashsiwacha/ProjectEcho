package com.projectecho.evidence.domain.repository;

import com.projectecho.common.valueobject.Identifier;
import com.projectecho.evidence.domain.model.EvidenceLineage;
import java.util.Optional;

/** Pure domain repository interface for the EvidenceLineage aggregate. */
public interface EvidenceLineageRepository {

  Optional<EvidenceLineage> findById(Identifier id);

  EvidenceLineage save(EvidenceLineage lineage);
}
