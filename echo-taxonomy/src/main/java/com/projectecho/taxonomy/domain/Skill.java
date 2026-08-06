package com.projectecho.taxonomy.domain;

import com.projectecho.shared.domain.AggregateRoot;
import com.projectecho.shared.domain.SkillId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "taxonomy_skills")
public class Skill extends AggregateRoot {

    @Column(nullable = false, unique = true)
    private SkillName name;

    @Column(nullable = false)
    private SkillCategory category;

    @Column private SkillId parentSkillId;

    protected Skill() {
        super();
    }

    public Skill(
            final UUID id,
            final SkillName name,
            final SkillCategory category,
            final SkillId parentSkillId) {
        super(id);
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.parentSkillId = parentSkillId; // Can be null if it's a root category
    }

    public SkillName getName() {
        return name;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public Optional<SkillId> getParentSkillId() {
        return Optional.ofNullable(parentSkillId);
    }
}
