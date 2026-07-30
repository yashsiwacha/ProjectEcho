package com.projectecho.evidence.infrastructure.persistence.repository;

import com.projectecho.evidence.infrastructure.persistence.entity.EvidenceLineageEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataEvidenceLineageRepository
    extends JpaRepository<EvidenceLineageEntity, UUID> {}
