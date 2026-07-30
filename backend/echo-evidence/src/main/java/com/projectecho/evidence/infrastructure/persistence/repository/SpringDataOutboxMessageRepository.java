package com.projectecho.evidence.infrastructure.persistence.repository;

import com.projectecho.evidence.infrastructure.persistence.entity.OutboxMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataOutboxMessageRepository extends JpaRepository<OutboxMessageEntity, UUID> {
}
