package com.projectecho.shared.exception;

public class ResourceNotFoundException extends DomainException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
