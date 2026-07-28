# Spring Boot Rules

## Purpose

This rule defines Spring Boot engineering practices for ProjectEcho.

It focuses on framework usage.

Architecture is governed by ADRs and architecture documents.

---

# Spring Version

Use:

- Spring Boot 3.x
- Spring Framework 6.x
- Java 21

Use modern Spring APIs.

Avoid deprecated APIs unless required for compatibility.

---

# Dependency Injection

Always use:

- Constructor Injection

Never use:

- Field Injection

Dependencies should be explicit.

Classes should remain easy to test.

---

# Bean Design

Beans should have a single responsibility.

Avoid oversized service classes.

Prefer composition over large service hierarchies.

Do not introduce unnecessary beans.

---

# Configuration

Prefer:

- @ConfigurationProperties
- External configuration
- Environment-specific configuration

Avoid:

- hard-coded values
- duplicated configuration

---

# Transactions

Use @Transactional only where required.

Keep transactions:

- small
- explicit
- well-defined

Do not place unnecessary business logic inside transactions.

---

# Validation

Validate requests at the application boundary.

Prefer Bean Validation annotations.

Fail fast on invalid input.

Do not duplicate validation across multiple layers.

---

# Exception Handling

Use centralized exception handling.

Prefer:

- @RestControllerAdvice

Avoid:

- duplicated try/catch blocks
- inconsistent error responses

Always preserve useful error information.

---

# Layering

Keep responsibilities separated.

Typical layers include:

- Controller
- Service
- Repository
- Domain

Controllers should coordinate.

Services should contain business logic.

Repositories should handle persistence.

---

# Spring Data

Prefer repository interfaces.

Avoid unnecessary custom queries.

Keep repository methods focused.

Business logic does not belong in repositories.

---

# Configuration Classes

Configuration classes should configure infrastructure.

Avoid business logic inside configuration.

Keep configuration modular.

---

# Events

Use Spring events only when appropriate.

Do not replace explicit service interactions with events without architectural justification.

---

# Testing

Spring Boot integration tests should verify:

- configuration
- wiring
- transactions
- persistence
- API behavior

Do not rely exclusively on integration tests.

Maintain a healthy balance of unit and integration testing.

---

# Spring Checklist

Before completing Spring Boot work, verify:

- Constructor Injection used.
- Layering respected.
- Transactions appropriate.
- Validation present.
- Configuration externalized.
- Exceptions centralized.
- Business logic isolated.
- Spring conventions followed.

