package com.projectecho.evidence.exception;

import com.projectecho.shared.exception.DomainException;

public class InvalidEvidenceSourceException extends DomainException {
    private static final long serialVersionUID = 1L;

    public InvalidEvidenceSourceException(final String message) {
        super(message);
    }
}
