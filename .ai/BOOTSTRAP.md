# ProjectEcho AI Bootstrap

## Purpose

This document is the primary entry point for AI assistants working on ProjectEcho.

Its purpose is to help AI assistants quickly understand:

- repository philosophy
- governance
- documentation structure
- engineering workflow
- implementation expectations

It intentionally does NOT duplicate repository documentation.

Instead, it directs AI assistants toward the authoritative sources.

---

# Repository Philosophy

ProjectEcho is a governance-first engineering project.

Documentation drives implementation.

Architecture precedes code.

Governance precedes architecture.

Every engineering decision should be:

- Explainable
- Traceable
- Versioned
- Documented
- Reusable

---

# Primary Reading Order

Before performing significant work:

1. PROJECT_MANIFEST.md
2. README.md
3. PROJECT_STATUS.md
4. docs/INDEX.md
5. Relevant ADRs
6. Relevant Architecture Documents
7. Relevant Engineering Documentation
8. Source Code

Only retrieve documents relevant to the current task.

---

# Documentation Authority

The repository follows one authoritative source for every concept.

Navigation documents:

- README
- PROJECT_MANIFEST
- PROJECT_STATUS
- INDEX

Governance documents:

- ADR
- ARBR
- Standards

Architecture documents:

- EAF
- EAD

Implementation documents:

- Engineering Guide
- Backend documentation

Never duplicate repository knowledge.

Always reference authoritative documentation.

---

# AI Responsibilities

Always:

- Understand existing architecture.
- Respect governance.
- Preserve module boundaries.
- Reuse existing abstractions.
- Produce maintainable code.
- Keep documentation synchronized.

Never:

- Invent architecture.
- Ignore approved ADRs.
- Create duplicate documentation.
- Resolve governance conflicts yourself.
- Present assumptions as repository facts.

---

# Engineering Principles

Prefer:

- Java 21
- Spring Boot 3
- Constructor Injection
- Clean Architecture
- Modular Design
- Explicit Dependencies
- High Cohesion
- Low Coupling

---

# Development Workflow

Understand

↓

Plan

↓

Implement

↓

Verify

↓

Document

↓

Review

Never skip repository understanding.

