# API Rules

## Purpose

This rule defines API engineering practices for ProjectEcho.

It governs how REST APIs should be designed and implemented.

Business rules remain defined by governance and architecture documents.

---

# API Philosophy

APIs are contracts.

Design APIs for:

- clarity
- consistency
- stability
- maintainability
- versionability

Avoid exposing internal implementation details.

---

# Resource Design

Prefer resource-oriented endpoints.

Examples:

GET    /users/{id}
POST   /users
PUT    /users/{id}
PATCH  /users/{id}
DELETE /users/{id}

Avoid RPC-style endpoints unless explicitly justified.

---

# HTTP Methods

Use HTTP methods according to their semantics.

GET

- safe
- idempotent

POST

- create resources
- execute non-idempotent operations

PUT

- replace resources

PATCH

- partial updates

DELETE

- remove resources

---

# HTTP Status Codes

Use standard HTTP status codes consistently.

Examples:

200 OK

201 Created

202 Accepted

204 No Content

400 Bad Request

401 Unauthorized

403 Forbidden

404 Not Found

409 Conflict

422 Unprocessable Entity

500 Internal Server Error

Avoid returning 200 for failures.

---

# Request DTOs

Never expose entities directly.

Always use DTOs for:

- requests
- responses

Validate requests at the API boundary.

---

# Response DTOs

Responses should be:

- explicit
- stable
- predictable

Do not expose internal identifiers unless required.

Avoid leaking implementation details.

---

# Validation

Validate incoming requests.

Prefer Bean Validation annotations.

Return clear validation errors.

Do not duplicate validation unnecessarily.

---

# Error Responses

Use a consistent error model.

Every error response should contain enough information for clients to understand:

- what failed
- why it failed
- how to correct the request when appropriate

Avoid inconsistent error formats.

---

# Versioning

Design APIs to evolve safely.

Avoid breaking changes.

Prefer additive evolution.

Document breaking changes explicitly.

---

# Pagination

Large collections should support pagination.

Avoid returning unbounded datasets.

---

# Filtering

Filtering should be:

- explicit
- documented
- predictable

Avoid ambiguous query parameters.

---

# Idempotency

Idempotent operations should remain idempotent.

Consider idempotency keys where appropriate for create operations.

---

# Documentation

Public APIs should be documented.

Keep API documentation synchronized with implementation.

---

# API Checklist

Before completing API work, verify:

- Resource-oriented design.
- Correct HTTP methods.
- Correct status codes.
- DTOs used.
- Validation present.
- Consistent error responses.
- No entity exposure.
- API remains backward compatible.

