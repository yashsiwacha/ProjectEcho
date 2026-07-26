package com.projectecho.common.exception;

/**
 * Exception thrown when an external service call fails.
 */
public class ExternalServiceException extends ApplicationException {

    private static final String ERROR_CODE = "EXTERNAL_SERVICE_ERROR";

    private final String serviceName;

    /**
     * Creates a new ExternalServiceException with service name and message.
     *
     * @param serviceName the name of the external service
     * @param message     the error message
     */
    public ExternalServiceException(String serviceName, String message) {
        super(ERROR_CODE, String.format("External service [%s] error: %s", serviceName, message));
        this.serviceName = serviceName;
    }

    /**
     * Creates a new ExternalServiceException with service name, message, and cause.
     *
     * @param serviceName the name of the external service
     * @param message     the error message
     * @param cause       the cause of this exception
     */
    public ExternalServiceException(String serviceName, String message, Throwable cause) {
        super(ERROR_CODE, String.format("External service [%s] error: %s", serviceName, message), cause);
        this.serviceName = serviceName;
    }

    /**
     * Gets the name of the external service.
     *
     * @return the service name
     */
    public String getServiceName() {
        return serviceName;
    }
}
