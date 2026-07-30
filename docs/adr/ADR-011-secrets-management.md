# ADR-011: Secrets and Configuration Management

**Status:** Proposed
**Date:** 2026-07-29
**Category:** Security

## Context
The application requires sensitive credentials (DB passwords, LLM API Keys, JWT signing keys) to boot.

## Decision
We will use **12-Factor App Environment Variables** combined with Spring Profiles for externalized configuration. No secrets will be stored in `application.yml` or source control. Local development will use an uncommitted `.env` file via Docker Compose or IDE run configurations.

## Consequences
- **Positive:** Secure, infrastructure-agnostic.
- **Negative:** Developers must manually configure their local `.env` files.
