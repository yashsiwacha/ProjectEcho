# ProjectEcho Bootstrap

## Purpose

This is the primary rule for all AI-assisted development in ProjectEcho.

Its purpose is to:

- establish repository navigation
- define authoritative document order
- enforce governance
- prevent architectural drift
- direct the AI to the correct documentation

This file intentionally does NOT duplicate repository documentation.

---

# Repository Philosophy

ProjectEcho is a governance-first engineering project.

The repository is designed so that:

- Documentation drives implementation.
- Architecture precedes code.
- Decisions are traceable.
- Every concept has one authoritative source.
- AI assistants assist engineering—they do not invent architecture.

---

# Working Principles

Always:

- Read before modifying.
- Reuse before creating.
- Reference before duplicating.
- Explain before implementing.
- Follow governance before writing code.

Never:

- Invent architecture.
- Ignore approved ADRs.
- Create duplicate documentation.
- Modify frozen governance artifacts without an approved governance process.
- Assume a conflict is resolved unless documented.

---

# Repository Reading Order

When beginning work, use this order:

1. PROJECT_MANIFEST.md
2. README.md
3. PROJECT_STATUS.md
4. docs/INDEX.md
5. Relevant ADRs
6. Relevant architecture documents
7. Relevant engineering documentation
8. Source code

Only read documents relevant to the current task.

---

# Documentation Authority

The authoritative source for a concept is the document that owns it.

Navigation documents summarize.

Governance documents decide.

Architecture documents design.

Engineering documents implement.

If multiple documents appear to conflict:

- Do not choose one yourself.
- Consult the Conflict Register.
- Surface the conflict to the developer.

---

# AI Responsibilities

You are an engineering assistant.

Your responsibilities are to:

- understand repository context
- follow governance
- implement requested changes
- preserve architecture
- improve maintainability
- avoid unnecessary complexity

You are NOT responsible for:

- redefining project goals
- inventing architectural direction
- resolving governance disputes
- replacing documented decisions

---

# Documentation Rules

Before creating documentation:

- Check whether the concept already exists.
- Extend authoritative documentation instead of duplicating it.
- Prefer references over summaries.
- Archive rather than delete obsolete documents.

---

# Implementation Rules

Before writing code:

- Understand the affected module.
- Reuse existing abstractions.
- Respect module boundaries.
- Prefer composition over duplication.
- Keep implementations deterministic.

---

# Review Checklist

Before completing any task, verify:

- Governance respected.
- Architecture unchanged unless requested.
- No duplicate documentation created.
- Existing patterns followed.
- Repository consistency maintained.

