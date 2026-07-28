# Governance Rules

## Purpose

This rule defines how AI assistants must behave when working inside the
ProjectEcho repository.

It does not replace governance documents.

It explains how to interact with them.

---

# Governance First

ProjectEcho follows Governance-First Engineering.

Implementation follows approved governance.

Documentation drives implementation.

Architecture precedes code.

Business decisions precede architecture.

---

# Decision Authority

Never make architectural decisions independently.

Authority belongs to the repository's governance documents.

Examples include:

- ADRs
- ARBRs
- EAF
- EAD
- Documentation Standard
- Repository Governance

If a decision is already documented:

Follow it.

Do not reinterpret it.

---

# Frozen Documents

Some governance documents may be marked:

- Frozen
- Approved
- Ratified

Treat these as immutable.

Never modify them unless the requested task explicitly includes governance updates.

---

# Conflict Handling

If conflicting documentation is discovered:

Do NOT guess.

Do NOT merge conflicting guidance.

Do NOT silently choose one interpretation.

Instead:

- Identify the conflict.
- Reference the relevant documents.
- Explain why the conflict affects implementation.
- Ask for clarification if the conflict blocks progress.

---

# Documentation Ownership

Every concept should have one authoritative source.

Before creating new documentation:

- Search the repository.
- Extend existing documentation where appropriate.
- Prefer references over duplication.
- Avoid introducing competing explanations.

---

# Architectural Changes

When a requested change affects:

- module boundaries
- public APIs
- persistence
- messaging
- security
- deployment
- repository structure

First determine whether governance documentation already covers the change.

If not:

Recommend creating or updating the appropriate governance artifact before implementation.

---

# Implementation Discipline

Do not introduce:

- hidden architectural assumptions
- undocumented dependencies
- framework-specific shortcuts that violate repository principles
- unnecessary abstractions
- speculative infrastructure

Prefer simple, documented, deterministic implementations.

---

# Repository Consistency

Every change should preserve:

- traceability
- maintainability
- readability
- consistency
- explicit reasoning

Repository consistency is more important than short-term convenience.

---

# AI Behaviour

Always explain important architectural reasoning.

State assumptions clearly.

Differentiate:

- documented facts
- repository observations
- recommendations
- opinions

Never present assumptions as repository facts.

