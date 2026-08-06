package com.projectecho.identity.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class CareerPassportTest {

    @Test
    void shouldCreateCareerPassport() {
        UUID id = UUID.randomUUID();
        Name name = new Name("Alice Smith");
        EmailAddress email = new EmailAddress("alice@example.com");
        JobTitle jobTitle = new JobTitle("Software Engineer");

        CareerPassport passport = new CareerPassport(id, name, email, jobTitle);

        assertEquals(id, passport.getId());
        assertEquals("Alice Smith", passport.getName().value());
        assertEquals("alice@example.com", passport.getEmail().value());
        assertEquals("Software Engineer", passport.getJobTitle().value());
    }

    @Test
    void shouldThrowIfAnyFieldIsNull() {
        UUID id = UUID.randomUUID();
        Name name = new Name("Alice");
        EmailAddress email = new EmailAddress("alice@example.com");
        JobTitle jobTitle = new JobTitle("Engineer");

        assertThrows(
                NullPointerException.class, () -> new CareerPassport(id, null, email, jobTitle));
        assertThrows(
                NullPointerException.class, () -> new CareerPassport(id, name, null, jobTitle));
        assertThrows(NullPointerException.class, () -> new CareerPassport(id, name, email, null));
    }
}
