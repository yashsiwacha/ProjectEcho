# ADR-013: Architectural Fitness Functions

**Status:** Proposed
**Date:** 2026-07-29
**Category:** Governance

## Context
The Dependency Matrix and Module Catalog defined in the EAD must be strictly enforced. As seen by the previous `backend/` codebase drift, manual code review is insufficient to prevent architectural decay.

## Decision
We will use **ArchUnit** in the `echo-bootstrap` test suite to programmatically enforce the EAD's module boundaries. The build will fail if any module violates the allowed DAG dependencies (e.g., if `echo-passport` attempts to import `echo-bootstrap`).

## Consequences
- **Positive:** Automated, mechanical enforcement of the architecture. Zero trust required.
- **Negative:** Initial setup effort to map the EAD rules into ArchUnit Java syntax.
