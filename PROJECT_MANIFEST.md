# Project Manifest — ProjectEcho

**Document ID:** NAV-0001
**Document Type:** Navigation Document
**Status:** Active
**Version:** 1.1
**Classification:** Internal
**Owner:** Documentation & Architecture Engineering
**Date:** 2026-07-28
**Review Cadence:** On every change to the governance corpus



---

Executive Summary

ProjectEcho is a governance-first Career Intelligence Platform that models careers as evolving, evidence-backed, explainable profiles. The repository contains governance artifacts, architectural frameworks, templates, and initial scaffolding for a Java/Spring Boot Modular Monolith.

Company Vision

Replace static resumes with continuously evolving, evidence-driven Career Intelligence.

Mission

Provide measurable competency-based career representations, explainable recommendations, and infrastructure that enforces governance and traceability for every decision.

Product Vision

Deliver a Career Passport that captures Skills, Evidence, Missions, Recommendations, and Readiness — with explainability and deterministic decisioning governed by a Rule Engine.

Engineering Philosophy

- Domain Driven Design
- Clean Architecture
- Modular Monolith
- Event-driven internals
- Business logic before infrastructure

Governance Philosophy

Governance precedes implementation. Every architectural and product decision must be traceable via ADRs, ARBRs, and framework documents. Implementation cannot contradict approved governance documents.

Repository Philosophy

The repo is source-of-record for governance, architecture, and design. Documentation is authoritative; code follows documented decisions. Archive rather than delete historic artifacts.

Architecture Philosophy

Design for extraction readiness (modular boundaries), traceable computation, immutable facts, and explicit interfaces. Rule Engine is the single decision authority; AI personalizes and explains only.

Technology Stack

- Java 21
- Spring Boot 3
- Spring AI
- Maven (multi-module; no Gradle build exists, and no Maven wrapper — see DAR-0001 F-09, F-17)
- PostgreSQL, Redis
- Flyway
- Docker Compose
- OrbStack
- REST, WebSocket, JWT



Documentation Hierarchy

1. PROJECT_MANIFEST.md (this file) — canonical single source of truth
2. README.md — developer landing page
3. docs/INDEX.md — document index and links
4. governance (docs/adr/, docs/arbr/, docs/rar/)
5. architecture (docs/eaf/, docs/ead/)
6. engineering (docs/ENGINEERING_GUIDE.md, docs/templates/)

Governance Hierarchy

- ADRs (docs/adr/) — approved architectural decisions
- ARBR (docs/arbr/) — architecture review board reports
- RAR (docs/rar/) — repository architecture reports
- FGM, CIF, EAF, EAD, EDF — framework documents in docs/

Repository Structure

- /backend — multi-module Java backend (auth, common, gateway, memory, notification, user, workflow)
- /frontend — frontend placeholder
- /docs — governance, architecture, templates, reports
- /infrastructure — deployment and infra artifacts
- /.ai — AI workspace (context, employees, prompts, templates, sessions)
- /shared — shared libraries
- /scripts — utilities and bootstrap scripts

Folder Explanations

- `backend/`: multi-module Maven projects; each submodule contains standard `src/main` and `src/test` trees and `pom.xml`.
- `docs/`: Governance and architecture documents, ADRs, templates, and engineering guides.
- `.ai/`: AI workspace and project memory for assistants; contains `context`, `employees`, `prompts`, `templates`, `sessions`.
- `infrastructure/`: deployment artifacts (docker, database scripts) and environment configs.

AI Workspace

Location: `.ai/`
Contains: context, employees, prompts, templates, sessions, agents.
Purpose: project memory and controlled assistant behavior. See `.ai/context/*` and `.ai/employees/*`.

AI Employees

Predefined roles: Founder, Chief Product Officer, Chief Intelligence Officer, Principal Software Architect, Principal Research Scientist, Software Engineer, Code Reviewer. See `.ai/employees/`.

Development Workflow

1. Read `PROJECT_MANIFEST.md` → `README.md` → `docs/INDEX.md` → relevant ADRs.
2. Follow Review Protocol in `.ai/employees/REVIEW_PROTOCOL.md` for design changes.
3. Open an ADR/ARBR for decisions that change architecture or governance.
4. Implement via feature branches; require PR with templates and review per repository policy.

Documentation Workflow

- All significant design changes produce/modify ADRs and ARBRs.
- Use templates in `docs/templates/` for ADR, ARBR, EAD, EDF, PRD.
- Update `.ai/context/DECISIONS.md` and `.ai/context/CHANGELOG.md` when a decision is approved.

Repository Workflow

- Follow `CONTRIBUTING.md` and PR templates in `.github/`.
- Use feature branches and link PRs to ADRs/tasks.

Current Project Status

- Phase: Engineering Initialization. Governance conflicts are resolved.
- Implementation: Backend reset complete. Awaiting Phase 1 & 2 ADRs.

Current Phase

Architecture definition completed. Engineering Initialization.

Current Milestones

Statuses below are derived. The authoritative status of any document is
in that document's own metadata block (Documentation Standard §2).

- ADR-001 — Approved, Frozen
- ADR-0002 — Approved, Frozen
- ARBR-0001 — **Approved** (FD-001 to FD-004 resolved via FD-005)
- EAF Rev 2 — Draft, unratified

Completed Work

- Repository layout and governance scaffolding
- ADR-001 (Career Intelligence Framework Foundations)
- ADR-0002 (Modular Monolith)
- ARBR-0001 issued (review complete; approval not recorded)
- Documentation Standard, DAR-0001 audit, Conflict Register
- Founder Decision FD-005 resolving Sprint 5 governance blocks
- ADR-0003 Ratification of Kafka Event Backbone

Pending Work

- Ratification of Phase 1 and Phase 2 foundational ADRs.
- Implementation of the Core Backend Monolith.
- Frontend scaffold.

Roadmap (high level)

- Phase 0 — Repository & Governance (**not complete** — the FGM, the
  governance model this phase was meant to establish, does not exist;
  CR-002)
- Phase 1 — Architecture (current, blocked)
- Phase 2 — Engineering Design
- Phase 3 — Backend Scaffold
- Phase 4 — Frontend
- Phase 5 — AI Integration
- Phase 6 — Testing & Validation
- Phase 7 — Deployment & Production

Known Risks

- None currently blocking. Foundational ADRs must be completed prior to code generation.

Important Documents

- [Documentation Index](docs/INDEX.md)
- [Project Status](PROJECT_STATUS.md)
- [Conflict Register](docs/reports/engineering/CONFLICT_REGISTER.md)
- [DAR-0001 — Documentation & Repository Audit](docs/reports/engineering/DAR-0001-documentation-audit.md)
- [Documentation Standard](docs/reference/standards/DOCUMENTATION_STANDARD.md)
- [ADR-001 — Career Intelligence Framework Foundations](docs/adr/ADR-001-career-intelligence-framework-foundations.md)
- [ADR-0002 — Modular Monolith](docs/adr/ADR-0002-modular-monolith-foundational-architecture.md)
- [ARBR-0001](docs/arbr/ARBR-0001.md)
- [EAF v1.0 Revision 2](docs/eaf/EAF-v1.0-revision-2.md)
- `.ai/context/*` (roadmap, decisions, changelog)

Framework documents established: **FDR-001**, **FGM**, **CIF**, **EAF**, **EAD**.

Repository Standards

- All decisions must be recorded in ADRs.
- Use templates in `docs/templates/`.
- Never allow implementation to contradict ADRs.

Maintenance Rules

- Archive rather than delete obsolete documents or artifacts.
- Update `.ai/context/CHANGELOG.md` for major governance events.

Cross-references

Where referenced documents exist, links point to them in `docs/` and `.ai/context/`.

If information is unavailable, this manifest uses the canonical phrase: "Not yet available."

Canonical reading order for AI assistants

1. `PROJECT_MANIFEST.md`
2. `README.md`
3. `docs/INDEX.md`
4. Governance documents (ADRs, ARBRs)

Contact

See `.ai/employees/*` for role ownerships and contact metadata.

— ProjectEcho Governance Team
