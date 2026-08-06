package com.projectecho.taxonomy.application;

import com.projectecho.shared.domain.SkillId;
import com.projectecho.taxonomy.domain.SkillCategory;
import com.projectecho.taxonomy.domain.SkillName;
import java.util.Optional;

public interface RegisterSkillUseCase {
    SkillId register(SkillName name, SkillCategory category, Optional<SkillId> parentSkillId);
}
