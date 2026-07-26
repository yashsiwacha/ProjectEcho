package com.projectecho.common.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
public class DuplicateResourceException extends ConflictException {

    /**
     * Creates a new DuplicateResourceException with a message.
     *
     * @param message the error message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Creates a new DuplicateResourceException for a specific resource type and field.
     *
     * @param resourceType the type of resource
     * @param fieldName    the name of the field that caused the conflict
     * @param fieldValue   the value that caused the conflict
     */
    public DuplicateResourceException(String resourceType, String fieldName, Object fieldValue) {
        super(String.format("%s with %s [%s] already exists", resourceType, fieldName, fieldValue));
    }
}
