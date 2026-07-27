## Project Manifest — ProjectEcho

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
- Gradle / Maven modules (backend uses Maven)
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

- Phase: Architecture
- Implementation: Not started (backend scaffolding planned)

Current Phase

Architecture — EAF and ADRs in progress. See `docs/eaf/` and `docs/adr/`.

Current Milestones

- ADR-0002 Approved
- ARBR-0001 Approved
- EAF Rev 2 drafted

Completed Work

- Repository layout and governance scaffolding
- ADR-0002 (Modular Monolith)
- ARBR-0001

Pending Work

- EAD (Engineering Architecture Document)
- Backend scaffold (Maven modules) — not implemented
- Frontend scaffold
- AI Gateway and Rule Engine design

Roadmap (high level)

- Phase 0 — Repository & Governance (complete)
- Phase 1 — Architecture (current)
- Phase 2 — Engineering Design
- Phase 3 — Backend Scaffold
- Phase 4 — Frontend
- Phase 5 — AI Integration
- Phase 6 — Testing & Validation
- Phase 7 — Deployment & Production

Known Risks

- Observability remains a documented gap in EAF (see EAF Self-Review).
- Implementation not started; migration risk if code is written before EAD.
- Single-VPS deployment may require re-evaluation for scale.

Important Documents

- docs/eaf/EAF-v1.0-revision-2.md
- docs/adr/ADR-0002-modular-monolith-foundational-architecture.md
- docs/ENGINEERING_GUIDE.md
- docs/PROJECT_VISION.md
- .ai/context/* (roadmap, decisions, changelog)

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
