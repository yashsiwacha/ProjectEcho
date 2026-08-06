package com.projectecho.evidence.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValueObjectTest {

    @Test
    void sourceUriShouldValidateCorrectly() {
        SourceURI uri = new SourceURI("https://example.com/certificate/123");
        assertEquals("https://example.com/certificate/123", uri.value());

        assertThrows(NullPointerException.class, () -> new SourceURI(null));
        assertThrows(IllegalArgumentException.class, () -> new SourceURI(""));
        assertThrows(IllegalArgumentException.class, () -> new SourceURI("invalid url string"));
    }

    @Test
    void trustTierShouldReturnCorrectLevels() {
        assertEquals(1, TrustTier.TIER_1.getLevel());
        assertEquals(2, TrustTier.TIER_2.getLevel());
        assertEquals(3, TrustTier.TIER_3.getLevel());
        assertEquals(4, TrustTier.TIER_4.getLevel());
        assertEquals(5, TrustTier.TIER_5.getLevel());
    }
}
