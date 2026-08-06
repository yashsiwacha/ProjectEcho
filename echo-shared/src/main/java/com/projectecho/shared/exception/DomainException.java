package com.projectecho.shared.exception;

import java.util.Objects;

public abstract class DomainException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected DomainException(final String message) {
        super(Objects.requireNonNull(message, "DomainException message cannot be null"));
    }
}
