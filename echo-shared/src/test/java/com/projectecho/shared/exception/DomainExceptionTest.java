package com.projectecho.shared.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DomainExceptionTest {
    private static class TestException extends DomainException {
        TestException(String message) {
            super(message);
        }
    }

    @Test
    void shouldStoreMessage() {
        String message = "test error";
        TestException exception = new TestException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldThrowOnNullMessage() {
        assertThrows(NullPointerException.class, () -> new TestException(null));
    }

    @Test
    void resourceNotFoundExceptionShouldStoreMessage() {
        String message = "Resource missing";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }
}
