package com.projectecho.identity.presentation;

import com.projectecho.identity.domain.CareerPassport;
import java.time.Instant;
import java.util.UUID;

public record PassportResponse(
        UUID id, String name, String email, String jobTitle, Instant createdAt, Instant updatedAt) {

    public static PassportResponse from(final CareerPassport passport) {
        return new PassportResponse(
                passport.getId(),
                passport.getName().value(),
                passport.getEmail().value(),
                passport.getJobTitle().value(),
                passport.getCreatedAt(),
                passport.getUpdatedAt());
    }
}
