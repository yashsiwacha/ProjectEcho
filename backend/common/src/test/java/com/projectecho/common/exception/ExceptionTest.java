package com.projectecho.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Exception Classes Tests")
class ExceptionTest {

    @Nested
    @DisplayName("ApplicationException Tests")
    class ApplicationExceptionTests {

        @Test
        @DisplayName("Should create exception with error code and message")
        void shouldCreateExceptionWithErrorCodeAndMessage() {
            // Given
            String errorCode = "TEST_ERROR";
            String message = "Test error message";

            // When
            ApplicationException exception = new ApplicationException(errorCode, message);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo(errorCode);
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getDetails()).isNull();
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Should create exception with error code, message, and cause")
        void shouldCreateExceptionWithCause() {
            // Given
            String errorCode = "TEST_ERROR";
            String message = "Test error message";
            RuntimeException cause = new RuntimeException("Root cause");

            // When
            ApplicationException exception = new ApplicationException(errorCode, message, cause);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo(errorCode);
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isEqualTo(cause);
        }

        @Test
        @DisplayName("Should create exception with error code, message, and details")
        void shouldCreateExceptionWithDetails() {
            // Given
            String errorCode = "TEST_ERROR";
            String message = "Test error message";
            String details = "Additional details";

            // When
            ApplicationException exception = new ApplicationException(errorCode, message, details);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo(errorCode);
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getDetails()).isEqualTo(details);
        }
    }

    @Nested
    @DisplayName("ResourceNotFoundException Tests")
    class ResourceNotFoundExceptionTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Given
            String message = "Resource not found";

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(message);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("RESOURCE_NOT_FOUND");
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should create exception with resource type and id")
        void shouldCreateExceptionWithResourceTypeAndId() {
            // Given
            String resourceType = "User";
            Object resourceId = 123L;

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(resourceType, resourceId);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("RESOURCE_NOT_FOUND");
            assertThat(exception.getMessage()).contains("User");
            assertThat(exception.getMessage()).contains("123");
        }

        @Test
        @DisplayName("Should create exception with resource type, field name, and field value")
        void shouldCreateExceptionWithFieldNameAndValue() {
            // Given
            String resourceType = "User";
            String fieldName = "email";
            Object fieldValue = "test@example.com";

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(resourceType, fieldName, fieldValue);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("RESOURCE_NOT_FOUND");
            assertThat(exception.getMessage()).contains("User");
            assertThat(exception.getMessage()).contains("email");
            assertThat(exception.getMessage()).contains("test@example.com");
        }
    }

    @Nested
    @DisplayName("ValidationException Tests")
    class ValidationExceptionTests {

        @Test
        @DisplayName("Should create exception with field errors")
        void shouldCreateExceptionWithFieldErrors() {
            // Given
            Map<String, List<String>> fieldErrors = Map.of(
                    "email", List.of("Email is required", "Email is invalid"),
                    "name", List.of("Name is required")
            );

            // When
            ValidationException exception = new ValidationException(fieldErrors);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("VALIDATION_ERROR");
            assertThat(exception.getFieldErrors()).hasSize(2);
            assertThat(exception.getFieldErrors().get("email")).hasSize(2);
            assertThat(exception.getFieldErrors().get("name")).hasSize(1);
        }

        @Test
        @DisplayName("Should create exception with message and field errors")
        void shouldCreateExceptionWithMessageAndFieldErrors() {
            // Given
            String message = "Validation failed";
            Map<String, List<String>> fieldErrors = Map.of("email", List.of("Email is required"));

            // When
            ValidationException exception = new ValidationException(message, fieldErrors);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("VALIDATION_ERROR");
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getFieldErrors()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("BadRequestException Tests")
    class BadRequestExceptionTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Given
            String message = "Bad request";

            // When
            BadRequestException exception = new BadRequestException(message);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("BAD_REQUEST");
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithCause() {
            // Given
            String message = "Bad request";
            RuntimeException cause = new RuntimeException("Root cause");

            // When
            BadRequestException exception = new BadRequestException(message, cause);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("BAD_REQUEST");
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isEqualTo(cause);
        }
    }

    @Nested
    @DisplayName("ConflictException Tests")
    class ConflictExceptionTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Given
            String message = "Conflict occurred";

            // When
            ConflictException exception = new ConflictException(message);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("CONFLICT");
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should create exception with resource type, id, and message")
        void shouldCreateExceptionWithResourceDetails() {
            // Given
            String resourceType = "User";
            Object resourceId = 123L;
            String message = "already active";

            // When
            ConflictException exception = new ConflictException(resourceType, resourceId, message);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("CONFLICT");
            assertThat(exception.getMessage()).contains("User");
            assertThat(exception.getMessage()).contains("123");
            assertThat(exception.getMessage()).contains("already active");
        }
    }

    @Nested
    @DisplayName("InternalServerException Tests")
    class InternalServerExceptionTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Given
            String message = "Internal error";

            // When
            InternalServerException exception = new InternalServerException(message);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("INTERNAL_SERVER_ERROR");
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithCause() {
            // Given
            String message = "Internal error";
            RuntimeException cause = new RuntimeException("Root cause");

            // When
            InternalServerException exception = new InternalServerException(message, cause);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("INTERNAL_SERVER_ERROR");
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isEqualTo(cause);
        }
    }

    @Nested
    @DisplayName("ForbiddenException Tests")
    class ForbiddenExceptionTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Given
            String message = "Access forbidden";

            // When
            ForbiddenException exception = new ForbiddenException(message);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("FORBIDDEN");
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should create exception with resource type and id")
        void shouldCreateExceptionWithResourceDetails() {
            // Given
            String resourceType = "Document";
            Object resourceId = 456L;

            // When
            ForbiddenException exception = new ForbiddenException(resourceType, resourceId);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("FORBIDDEN");
            assertThat(exception.getMessage()).contains("Document");
            assertThat(exception.getMessage()).contains("456");
        }
    }

    @Nested
    @DisplayName("UnauthorizedException Tests")
    class UnauthorizedExceptionTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Given
            String message = "Authentication required";

            // When
            UnauthorizedException exception = new UnauthorizedException(message);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("UNAUTHORIZED");
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should create default exception")
        void shouldCreateDefaultException() {
            // When
            UnauthorizedException exception = new UnauthorizedException();

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("UNAUTHORIZED");
            assertThat(exception.getMessage()).isEqualTo("Authentication required");
        }
    }

    @Nested
    @DisplayName("ServiceUnavailableException Tests")
    class ServiceUnavailableExceptionTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Given
            String message = "Service unavailable";

            // When
            ServiceUnavailableException exception = new ServiceUnavailableException(message);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("SERVICE_UNAVAILABLE");
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithCause() {
            // Given
            String message = "Service unavailable";
            RuntimeException cause = new RuntimeException("Connection refused");

            // When
            ServiceUnavailableException exception = new ServiceUnavailableException(message, cause);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("SERVICE_UNAVAILABLE");
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isEqualTo(cause);
        }
    }

    @Nested
    @DisplayName("RateLimitExceededException Tests")
    class RateLimitExceededExceptionTests {

        @Test
        @DisplayName("Should create exception with message and retry after")
        void shouldCreateExceptionWithRetryAfter() {
            // Given
            String message = "Rate limit exceeded";
            long retryAfterSeconds = 60;

            // When
            RateLimitExceededException exception = new RateLimitExceededException(message, retryAfterSeconds);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("RATE_LIMIT_EXCEEDED");
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getRetryAfterSeconds()).isEqualTo(retryAfterSeconds);
        }
    }

    @Nested
    @DisplayName("DuplicateResourceException Tests")
    class DuplicateResourceExceptionTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Given
            String message = "Resource already exists";

            // When
            DuplicateResourceException exception = new DuplicateResourceException(message);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("CONFLICT");
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should create exception with resource type, field name, and value")
        void shouldCreateExceptionWithFieldDetails() {
            // Given
            String resourceType = "User";
            String fieldName = "email";
            Object fieldValue = "test@example.com";

            // When
            DuplicateResourceException exception = new DuplicateResourceException(resourceType, fieldName, fieldValue);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("CONFLICT");
            assertThat(exception.getMessage()).contains("User");
            assertThat(exception.getMessage()).contains("email");
            assertThat(exception.getMessage()).contains("test@example.com");
        }
    }

    @Nested
    @DisplayName("ExternalServiceException Tests")
    class ExternalServiceExceptionTests {

        @Test
        @DisplayName("Should create exception with service name and message")
        void shouldCreateExceptionWithServiceName() {
            // Given
            String serviceName = "PaymentService";
            String message = "Connection timeout";

            // When
            ExternalServiceException exception = new ExternalServiceException(serviceName, message);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("EXTERNAL_SERVICE_ERROR");
            assertThat(exception.getServiceName()).isEqualTo(serviceName);
            assertThat(exception.getMessage()).contains("PaymentService");
            assertThat(exception.getMessage()).contains("Connection timeout");
        }

        @Test
        @DisplayName("Should create exception with service name, message, and cause")
        void shouldCreateExceptionWithCause() {
            // Given
            String serviceName = "PaymentService";
            String message = "Connection timeout";
            RuntimeException cause = new RuntimeException("Socket timeout");

            // When
            ExternalServiceException exception = new ExternalServiceException(serviceName, message, cause);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("EXTERNAL_SERVICE_ERROR");
            assertThat(exception.getServiceName()).isEqualTo(serviceName);
            assertThat(exception.getCause()).isEqualTo(cause);
        }
    }

    @Nested
    @DisplayName("ErrorCodes Tests")
    class ErrorCodesTests {

        @Test
        @DisplayName("Should have all error code constants")
        void shouldHaveAllErrorCodes() {
            // Then
            assertThat(ErrorCodes.RESOURCE_NOT_FOUND).isEqualTo("RESOURCE_NOT_FOUND");
            assertThat(ErrorCodes.VALIDATION_ERROR).isEqualTo("VALIDATION_ERROR");
            assertThat(ErrorCodes.BAD_REQUEST).isEqualTo("BAD_REQUEST");
            assertThat(ErrorCodes.CONFLICT).isEqualTo("CONFLICT");
            assertThat(ErrorCodes.UNAUTHORIZED).isEqualTo("UNAUTHORIZED");
            assertThat(ErrorCodes.FORBIDDEN).isEqualTo("FORBIDDEN");
            assertThat(ErrorCodes.INTERNAL_SERVER_ERROR).isEqualTo("INTERNAL_SERVER_ERROR");
            assertThat(ErrorCodes.SERVICE_UNAVAILABLE).isEqualTo("SERVICE_UNAVAILABLE");
            assertThat(ErrorCodes.RATE_LIMIT_EXCEEDED).isEqualTo("RATE_LIMIT_EXCEEDED");
            assertThat(ErrorCodes.DUPLICATE_RESOURCE).isEqualTo("DUPLICATE_RESOURCE");
            assertThat(ErrorCodes.EXTERNAL_SERVICE_ERROR).isEqualTo("EXTERNAL_SERVICE_ERROR");
        }
    }

    @Nested
    @DisplayName("ErrorResponse Tests")
    class ErrorResponseTests {

        @Test
        @DisplayName("Should create error response from exception")
        void shouldCreateFromException() {
            // Given
            ResourceNotFoundException exception = new ResourceNotFoundException("User", 123L);
            String path = "/api/users/123";

            // When
            ErrorResponse response = ErrorResponse.fromException(exception, 404, path);

            // Then
            assertThat(response.status()).isEqualTo(404);
            assertThat(response.error()).isEqualTo("Not Found");
            assertThat(response.errorCode()).isEqualTo("RESOURCE_NOT_FOUND");
            assertThat(response.message()).contains("User");
            assertThat(response.path()).isEqualTo(path);
            assertThat(response.timestamp()).isNotNull();
            assertThat(response.fieldErrors()).isNull();
        }

        @Test
        @DisplayName("Should create error response from validation exception")
        void shouldCreateFromValidationException() {
            // Given
            Map<String, List<String>> fieldErrors = Map.of("email", List.of("Email is required"));
            ValidationException exception = new ValidationException(fieldErrors);
            String path = "/api/users";

            // When
            ErrorResponse response = ErrorResponse.fromValidationException(exception, 400, path);

            // Then
            assertThat(response.status()).isEqualTo(400);
            assertThat(response.error()).isEqualTo("Bad Request");
            assertThat(response.errorCode()).isEqualTo("VALIDATION_ERROR");
            assertThat(response.fieldErrors()).hasSize(1);
            assertThat(response.fieldErrors().get("email")).contains("Email is required");
        }
    }
}