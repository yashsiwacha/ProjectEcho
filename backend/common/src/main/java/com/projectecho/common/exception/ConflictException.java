package com.projectecho.common.exception;

/**
 * Exception thrown when there's a conflict with the current state of the resource.
 */
public class ConflictException extends ApplicationException {

    private static final String ERROR_CODE = "CONFLICT";

    /**
     * Creates a new ConflictException with a message.
     *
     * @param message the error message
     */
    public ConflictException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Creates a new ConflictException for a specific resource type and identifier.
     *
     * @param resourceType the type of resource
     * @param resourceId   the identifier of the resource
     * @param message      additional conflict details
     */
    public ConflictException(String resourceType, Object resourceId, String message) {
        super(ERROR_CODE, String.format("%s with id [%s] conflict: %s", resourceType, resourceId, message));
    }
}
