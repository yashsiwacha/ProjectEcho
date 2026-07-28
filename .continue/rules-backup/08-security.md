# Security Rules

## Purpose

This rule defines security engineering practices for ProjectEcho.

It focuses on secure application development.

Security architecture remains governed by repository documentation.

---

# Security Philosophy

Security is a fundamental engineering concern.

Security should be built into the system.

It should never be added as an afterthought.

Always prefer secure defaults.

---

# Authentication

Authentication must be explicit.

Never bypass authentication for convenience.

Do not hardcode credentials.

Always validate authenticated identity before performing protected actions.

---

# Authorization

Authentication identifies users.

Authorization determines permissions.

Never assume authentication implies authorization.

Validate permissions for every protected operation.

---

# Sensitive Data

Never expose:

- passwords
- secrets
- tokens
- private keys
- credentials

Never log sensitive information.

Mask sensitive values whenever possible.

---

# Secrets

Never commit secrets to the repository.

Always externalize:

- API keys
- passwords
- tokens
- certificates

Use environment variables or secret management solutions.

---

# Input Validation

Treat all external input as untrusted.

Validate:

- request bodies
- query parameters
- headers
- uploaded files

Reject invalid input early.

---

# Output Encoding

Never trust output contexts.

Avoid exposing internal implementation details.

Sanitize data where appropriate.

---

# SQL Injection

Never concatenate SQL manually.

Prefer:

- JPA
- prepared statements
- parameterized queries

Avoid dynamic query construction without validation.

---

# Error Handling

Error responses should help clients.

They should NOT expose:

- stack traces
- database schema
- internal implementation
- sensitive configuration

Log detailed errors internally.

Return safe errors externally.

---

# File Uploads

Validate:

- file type
- file size
- file name

Never trust client-provided metadata.

---

# Dependencies

Use maintained libraries.

Avoid deprecated or unsupported dependencies.

Remove unused dependencies whenever practical.

---

# Logging

Logs should assist debugging.

Logs must never leak:

- credentials
- tokens
- personal information
- secrets

---

# Principle of Least Privilege

Grant only the permissions required.

Avoid unnecessary privileges.

Minimize access wherever possible.

---

# Security Checklist

Before completing security-sensitive work, verify:

- Authentication enforced.
- Authorization verified.
- Secrets externalized.
- Input validated.
- Sensitive data protected.
- Safe error responses.
- No secrets logged.
- Least privilege maintained.

