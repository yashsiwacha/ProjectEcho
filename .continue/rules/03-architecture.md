# Architecture Rules

## Purpose

This rule defines how AI assistants should reason about architecture while
working in ProjectEcho.

It does not define the system architecture.

The repository's Architecture Decision Records (ADRs), Engineering
Architecture Framework (EAF), Engineering Architecture Documents (EAD),
and related governance artifacts are the authoritative sources.

---

# Architecture Philosophy

ProjectEcho follows Governance-First Engineering.

Architecture exists before implementation.

Implementation should realize architecture—not redefine it.

AI assistants assist implementation.

They do not invent architectural direction.

---

# Architectural Authority

Before making architectural changes:

1. Read the relevant ADRs.
2. Read the relevant architecture documents.
3. Understand the affected module.
4. Confirm the requested change is compatible with repository governance.

Never override documented architecture.

---

# Module Boundaries

Respect module boundaries at all times.

Do not:

- merge unrelated responsibilities
- introduce circular dependencies
- bypass public interfaces
- create hidden coupling
- duplicate existing capabilities

Prefer extending existing modules over creating new ones.

---

# Architectural Changes

When a requested change affects:

- module structure
- service boundaries
- shared libraries
- persistence
- messaging
- security
- deployment
- public interfaces

Determine whether governance already covers the change.

If not:

Recommend updating the appropriate governance documentation before implementation.

---

# Design Principles

Prefer:

- high cohesion
- low coupling
- explicit dependencies
- composition over inheritance
- deterministic behaviour
- maintainable abstractions

Avoid:

- unnecessary abstraction
- speculative design
- framework-driven architecture
- premature optimization

---

# Implementation Strategy

Before writing code:

- understand the existing implementation
- reuse existing abstractions
- preserve architectural consistency
- minimize change scope
- keep modules independent

Implementation should fit naturally into the existing design.

---

# AI Responsibilities

When discussing architecture:

Clearly distinguish:

- documented repository facts
- observations
- assumptions
- recommendations

Never present recommendations as established architecture.

---

# Review Checklist

Before completing architectural work, verify:

- Module boundaries preserved.
- Existing architecture respected.
- No unnecessary abstractions introduced.
- Existing patterns followed.
- Dependencies remain explicit.
- Coupling has not increased.
- Architectural documentation remains valid.
- Governance has not been violated.

