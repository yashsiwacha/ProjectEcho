package com.projectecho.common.exception;

/**
 * Exception thrown when access to a resource is forbidden.
 */
public class ForbiddenException extends ApplicationException {

    private static final String ERROR_CODE = "FORBIDDEN";

    /**
     * Creates a new ForbiddenException with a message.
     *
     * @param message the error message
     */
    public ForbiddenException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Creates a new ForbiddenException for a specific resource type.
     *
     * @param resourceType the type of resource
     * @param resourceId   the identifier of the resource
     */
    public ForbiddenException(String resourceType, Object resourceId) {
        super(ERROR_CODE, String.format("Access to %s with id [%s] is forbidden", resourceType, resourceId));
    }
}
