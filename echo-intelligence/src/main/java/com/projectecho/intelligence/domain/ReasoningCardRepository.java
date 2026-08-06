package com.projectecho.intelligence.domain;

import com.projectecho.shared.domain.MissionId;
import com.projectecho.shared.domain.PassportId;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasoningCardRepository extends JpaRepository<ReasoningCard, UUID> {

    Page<ReasoningCard> findByPassportId(PassportId passportId, Pageable pageable);

    Page<ReasoningCard> findByMissionId(MissionId missionId, Pageable pageable);
}
