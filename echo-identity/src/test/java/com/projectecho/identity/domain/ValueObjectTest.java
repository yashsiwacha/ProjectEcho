package com.projectecho.identity.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValueObjectTest {

    @Test
    void emailAddressShouldValidateCorrectly() {
        EmailAddress email = new EmailAddress("test@example.com");
        assertEquals("test@example.com", email.value());

        assertThrows(NullPointerException.class, () -> new EmailAddress(null));
        assertThrows(IllegalArgumentException.class, () -> new EmailAddress(""));
        assertThrows(IllegalArgumentException.class, () -> new EmailAddress("invalid-email"));
    }

    @Test
    void nameShouldValidateCorrectly() {
        Name name = new Name("John Doe");
        assertEquals("John Doe", name.value());

        assertThrows(NullPointerException.class, () -> new Name(null));
        assertThrows(IllegalArgumentException.class, () -> new Name(""));
    }

    @Test
    void jobTitleShouldValidateCorrectly() {
        JobTitle job = new JobTitle("Software Engineer");
        assertEquals("Software Engineer", job.value());

        assertThrows(NullPointerException.class, () -> new JobTitle(null));
        assertThrows(IllegalArgumentException.class, () -> new JobTitle(""));
    }
}
