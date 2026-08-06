package com.projectecho.identity.exception;

import com.projectecho.shared.exception.DomainException;

public class DuplicatePassportException extends DomainException {
    private static final long serialVersionUID = 1L;

    public DuplicatePassportException(final String email) {
        super("A Career Passport already exists for email: " + email);
    }
}
