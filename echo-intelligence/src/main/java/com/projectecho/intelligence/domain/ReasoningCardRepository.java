package com.projectecho.intelligence.domain;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasoningCardRepository extends CrudRepository<ReasoningCard, UUID> {}
