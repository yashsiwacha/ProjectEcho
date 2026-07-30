package com.projectecho.evidence.infrastructure.persistence.repository;

import com.projectecho.evidence.infrastructure.persistence.entity.EvidenceLineageEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataEvidenceLineageRepository
    extends JpaRepository<EvidenceLineageEntity, UUID> {
        
    @Modifying
    @Query("UPDATE EvidenceLineageEntity e SET e.version = e.version + 1 WHERE e.id = :id AND e.version = :expectedVersion")
    int incrementVersion(@Param("id") UUID id, @Param("expectedVersion") int expectedVersion);
}
