package com.projectecho.shared.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TrustTierTest {
    @Test
    void highTierShouldReturnCorrectName() {
        TrustTier tier = new High();
        assertEquals("HIGH", tier.name());
    }

    @Test
    void mediumTierShouldReturnCorrectName() {
        TrustTier tier = new Medium();
        assertEquals("MEDIUM", tier.name());
    }

    @Test
    void lowTierShouldReturnCorrectName() {
        TrustTier tier = new Low();
        assertEquals("LOW", tier.name());
    }
}
