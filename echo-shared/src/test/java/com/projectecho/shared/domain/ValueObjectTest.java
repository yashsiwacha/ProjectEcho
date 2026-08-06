package com.projectecho.shared.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class ValueObjectTest {
    @Test
    void passportIdShouldStoreValue() {
        UUID id = UUID.randomUUID();
        PassportId passportId = new PassportId(id);
        assertEquals(id, passportId.value());
    }

    @Test
    void passportIdShouldThrowOnNull() {
        assertThrows(NullPointerException.class, () -> new PassportId(null));
    }

    @Test
    void skillIdShouldStoreValue() {
        UUID id = UUID.randomUUID();
        SkillId skillId = new SkillId(id);
        assertEquals(id, skillId.value());
    }

    @Test
    void skillIdShouldThrowOnNull() {
        assertThrows(NullPointerException.class, () -> new SkillId(null));
    }
}
