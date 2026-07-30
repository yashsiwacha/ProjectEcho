# ADR-005: API Protocol Selection

**Status:** Proposed
**Date:** 2026-07-29
**Category:** API

## Context
The `echo-bootstrap` layer must expose domain capabilities to the frontend and external API consumers.

## Decision
We will implement a **RESTful API over HTTP/2** returning JSON payloads. OpenAPI (Swagger) will be generated automatically at runtime.

## Consequences
- **Positive:** Ubiquitous, standard integration pattern, supported by Spring WebMvc out-of-the-box.
- **Negative:** Over-fetching compared to GraphQL, requiring carefully designed DTOs in `echo-bootstrap`.
