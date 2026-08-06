package com.projectecho.taxonomy.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValueObjectTest {

    @Test
    void skillNameShouldValidateCorrectly() {
        SkillName name = new SkillName("Java");
        assertEquals("Java", name.value());

        assertThrows(NullPointerException.class, () -> new SkillName(null));
        assertThrows(IllegalArgumentException.class, () -> new SkillName(""));
    }

    @Test
    void skillCategoryShouldValidateCorrectly() {
        SkillCategory category = new SkillCategory("Programming");
        assertEquals("Programming", category.value());

        assertThrows(NullPointerException.class, () -> new SkillCategory(null));
        assertThrows(IllegalArgumentException.class, () -> new SkillCategory(""));
    }
}
