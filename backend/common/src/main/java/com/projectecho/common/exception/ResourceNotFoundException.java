package com.projectecho.common.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends ApplicationException {

    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

    /**
     * Creates a new ResourceNotFoundException with a message.
     *
     * @param message the error message
     */
    public ResourceNotFoundException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Creates a new ResourceNotFoundException for a specific resource type and identifier.
     *
     * @param resourceType the type of resource (e.g., "User")
     * @param resourceId   the identifier of the resource
     */
    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(ERROR_CODE, String.format("%s with id [%s] not found", resourceType, resourceId));
    }

    /**
     * Creates a new ResourceNotFoundException for a specific resource type and field value.
     *
     * @param resourceType the type of resource
     * @param fieldName    the name of the field used for lookup
     * @param fieldValue   the value of the field
     */
    public ResourceNotFoundException(String resourceType, String fieldName, Object fieldValue) {
        super(ERROR_CODE, String.format("%s with %s [%s] not found", resourceType, fieldName, fieldValue));
    }
}
