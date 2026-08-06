---
Document ID: ADR-009
Title: Adr 009 Error Handling Strategy
Version: 1.0
Status: Frozen
Classification: Architecture
Owner: Principal Architect
Authority Level: 4
Primary Audience: Engineers
Governed By: CIF-0001
Review Cadence: N/A
Last Updated: 2026-08-04
Next Review: N/A
---
# ADR-009: Error Handling Strategy

**Status:** Proposed
**Date:** 2026-07-29
**Category:** Architecture

## Context
Unhandled exceptions across the monolithic boundaries can leak implementation details or fail silently, violating the EAF Explainability principle.

## Decision
We will use a centralized **Spring `@RestControllerAdvice`** in `echo-bootstrap` to catch all exceptions globally. Domain modules will throw standard domain-specific RuntimeExceptions defined in `echo-common`, which the advice will map to standard RFC 7807 ProblemDetail JSON responses.

## Consequences
- **Positive:** Uniform API responses for front-end consumers.
- **Negative:** Requires strict discipline to not throw raw `Exception` or `RuntimeException` from domain modules.
