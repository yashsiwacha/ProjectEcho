# AIOS v2 BOOTSTRAP

> **Purpose:** Universal entry point for all AI assistants working on ProjectEcho.

This document defines how an AI should approach work in the repository. It does **not** contain project knowledge—it routes AI assistants to the repository's authoritative sources.

---

# Core Principles

## 1. Governance First

Architecture, documentation, and approved decisions precede implementation.

Never implement features that contradict approved governance.

---

## 2. Single Source of Truth

The repository is authoritative.

Never duplicate information contained in:

- `PROJECT_MANIFEST.md`
- `PROJECT_STATUS.md`
- ADRs
- ARBRs
- FGM
- CIF
- EAF
- EAD
- Engineering documentation

Reference these documents instead.

---

## 3. AI Workspace Purpose

The `.ai/` directory exists solely for operational support.

It may contain:

- current project state
- session memory
- AI handoffs
- workflow rules
- review procedures

It must never become a second documentation system.

---

# Standard Reading Order

Unless instructed otherwise, load context in the following order:

1. `AI_MANIFEST.md`
2. `.ai/state/CURRENT_STATE.md`
3. `PROJECT_MANIFEST.md`
4. `PROJECT_STATUS.md`
5. Task-specific documentation (ADRs, framework documents, engineering guides, source code)

Do not load additional documentation unless it is required for the current task.

---

# Context Escalation Policy

Always begin with the minimum required context.

Only load additional documentation when:

- architectural decisions are required
- repository guidance is insufficient
- implementation depends on an approved ADR or framework

Avoid reading entire directories when a single document is sufficient.

---

# AI Operating Rules

Every AI assistant must:

- Preserve repository integrity.
- Prefer references over duplication.
- Archive before deleting.
- Never invent architecture or governance.
- Clearly distinguish facts from assumptions.
- Keep modifications traceable and evidence-based.

---

# Related Operational Documents

- `.ai/workflows/AGENTS.md`
- `.ai/workflows/REVIEW_PROTOCOL.md`
- `.ai/state/CURRENT_STATE.md`

---

# Token Efficiency

Keep the active context as small as possible.

Do not load:

- `.ai/archive/`
- unrelated documentation
- historical reports

unless they are explicitly required for the current task.