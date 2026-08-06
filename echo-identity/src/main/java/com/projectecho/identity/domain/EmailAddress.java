package com.projectecho.identity.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public record EmailAddress(String value) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public EmailAddress {
        Objects.requireNonNull(value, "Email address cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Email address cannot be blank");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
