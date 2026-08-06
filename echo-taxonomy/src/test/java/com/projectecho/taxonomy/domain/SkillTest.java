package com.projectecho.taxonomy.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.projectecho.shared.domain.SkillId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SkillTest {

    @Test
    void shouldCreateSkillWithParent() {
        UUID id = UUID.randomUUID();
        SkillName name = new SkillName("Java");
        SkillCategory category = new SkillCategory("Programming Language");
        SkillId parentId = new SkillId(UUID.randomUUID());

        Skill skill = new Skill(id, name, category, parentId);

        assertEquals(id, skill.getId());
        assertEquals("Java", skill.getName().value());
        assertEquals("Programming Language", skill.getCategory().value());
        assertTrue(skill.getParentSkillId().isPresent());
        assertEquals(parentId, skill.getParentSkillId().get());
    }

    @Test
    void shouldCreateSkillWithoutParent() {
        UUID id = UUID.randomUUID();
        SkillName name = new SkillName("Software Engineering");
        SkillCategory category = new SkillCategory("Domain");

        Skill skill = new Skill(id, name, category, null);

        assertEquals(id, skill.getId());
        assertEquals("Software Engineering", skill.getName().value());
        assertFalse(skill.getParentSkillId().isPresent());
    }

    @Test
    void shouldThrowIfRequiredFieldIsNull() {
        UUID id = UUID.randomUUID();
        SkillName name = new SkillName("Java");
        SkillCategory category = new SkillCategory("Language");

        assertThrows(NullPointerException.class, () -> new Skill(id, null, category, null));
        assertThrows(NullPointerException.class, () -> new Skill(id, name, null, null));
    }
}
