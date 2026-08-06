package com.projectecho.taxonomy.domain;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {

    @Query("SELECT s FROM Skill s WHERE s.name LIKE %:name%")
    Page<Skill> searchByName(@Param("name") String name, Pageable pageable);

    Page<Skill> findByCategory(SkillCategory category, Pageable pageable);

    boolean existsByName(SkillName name);
}
