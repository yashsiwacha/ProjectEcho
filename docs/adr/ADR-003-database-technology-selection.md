---
Document ID: ADR-003
Title: Adr 003 Database Technology Selection
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
# ADR-003: Database Technology Selection

**Status:** Proposed
**Date:** 2026-07-29
**Category:** Persistence

## Context
The Engineering Architecture Document (EAD) mandates strict data ownership per module within the Modular Monolith, along with immutable audit trails. A persistence technology is required that can enforce relational integrity within schema-bound modules.

## Decision
We will use **PostgreSQL** as the primary relational database for the ProjectEcho monolith. Each Bounded Context (Module) will have its own logical schema within the single physical database to enforce the EAD's "Shared Nothing" data rule.

## Consequences
- **Positive:** Mature, transactional, supports JSONB for flexible event payloads, natively supported by Spring Data JPA.
- **Negative:** Schema migrations require careful synchronization across modules via Flyway.
