package com.projectecho.identity.exception;

import com.projectecho.shared.exception.DomainException;

/** Concrete domain exception for identity and authentication failures (FD-0018). */
public class IdentityException extends DomainException {
    private static final long serialVersionUID = 1L;

    public IdentityException(final String message) {
        super(message);
    }
}
