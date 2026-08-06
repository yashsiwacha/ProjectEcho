package com.projectecho.evidence.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public record SourceURI(String value) {
    public SourceURI {
        Objects.requireNonNull(value, "Source URI cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Source URI cannot be blank");
        }
        try {
            new URI(value);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI format: " + value, e);
        }
    }
}
