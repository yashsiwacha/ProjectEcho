---
Document ID: ADR-006
Title: Adr 006 Identity Authentication Mechanism
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
# ADR-006: Identity Authentication Mechanism

**Status:** Proposed
**Date:** 2026-07-29
**Category:** Security

## Context
The system must establish a security context for a `Person` or internal system actor to authorize actions across bounded contexts.

## Decision
We will use **Stateless JSON Web Tokens (JWT)**. The `echo-identity` module will issue the tokens upon successful login, and the `echo-bootstrap` layer will validate them on incoming requests via Spring Security filters.

## Consequences
- **Positive:** Stateless architecture avoids session replication across nodes if scaled horizontally.
- **Negative:** Token revocation requires maintaining a stateful blocklist (e.g., in Redis) until expiration.
