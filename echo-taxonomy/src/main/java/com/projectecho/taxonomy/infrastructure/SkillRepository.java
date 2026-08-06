package com.projectecho.taxonomy.infrastructure;

import com.projectecho.taxonomy.domain.Skill;
import com.projectecho.taxonomy.domain.SkillName;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SkillRepository {
    void save(Skill skill);

    Optional<Skill> findById(UUID id);

    List<Skill> searchByName(SkillName name);
}
