# ProjectEcho

**Document ID:** NAV-0002
**Document Type:** Navigation Document
**Status:** Active
**Version:** 1.1
**Classification:** Internal
**Owner:** Documentation & Architecture Engineering
**Date:** 2026-07-28
**Review Cadence:** On every change to the governance corpus

---

## Purpose

Landing page for the repository. It points at governing documents and states nothing that those documents do not already state.

## Authority

None. This is a navigation document ([Documentation Standard §5](docs/reference/standards/DOCUMENTATION_STANDARD.md)). If it disagrees with a governing document, the governing document is correct and this file is defective.

---

## Read this first

The repository has **four unresolved S1/S2 governance conflicts** and cannot proceed to design or implementation until founders resolve them. The most consequential is that ProjectEcho is currently documented as two different products under one name.

- [Conflict Register](docs/reports/engineering/CONFLICT_REGISTER.md) — 14 open conflicts, each with its closing authority
- [Project Status](PROJECT_STATUS.md) — what is blocked and what is waiting on whom
- [DAR-0001](docs/reports/engineering/DAR-0001-documentation-audit.md) — the full audit

## What ProjectEcho is

**Unresolved.** Two product definitions exist in the repository and neither is marked superseded:

- a **Career Intelligence Platform** replacing static resumes with evidence-driven, explainable career profiles — the subject of both Frozen ADRs, ARBR-0001, and the EAF;
- an **event delivery / outbound webhook backbone** — the subject of `docs/PROJECT_VISION.md`, `docs/ENGINEERING_GUIDE.md`, the archived architecture set, and the current `docker-compose.yml`.

Choosing between them is a founder decision, tracked as [CR-001](docs/reports/engineering/CONFLICT_REGISTER.md#cr-001). This README does not assert an answer, because doing so would settle the question by navigation document.

## Quick links

- Project manifest: [PROJECT_MANIFEST.md](PROJECT_MANIFEST.md) — note [CR-014](docs/reports/engineering/CONFLICT_REGISTER.md#cr-014) on its claimed precedence
- Documentation index: [docs/INDEX.md](docs/INDEX.md)
- Documentation standard: [docs/reference/standards/DOCUMENTATION_STANDARD.md](docs/reference/standards/DOCUMENTATION_STANDARD.md)
- ADR-001 — [Career Intelligence Framework Foundations](docs/adr/ADR-001-career-intelligence-framework-foundations.md) (Frozen)
- ADR-0002 — [Modular Monolith](docs/adr/ADR-0002-modular-monolith-foundational-architecture.md) (Frozen)
- ARBR-0001 — [Architecture Review Board Report](docs/arbr/ARBR-0001.md) (Proposed for Founder Approval)
- EAF — [v1.0 Revision 2](docs/eaf/EAF-v1.0-revision-2.md) (Draft, unratified)
- AI workspace: [.ai/](.ai/)

Subject to [CR-001](docs/reports/engineering/CONFLICT_REGISTER.md#cr-001), and not current until it is resolved: [docs/ENGINEERING_GUIDE.md](docs/ENGINEERING_GUIDE.md), [docs/PROJECT_VISION.md](docs/PROJECT_VISION.md).

## Get started

1. Read [PROJECT_MANIFEST.md](PROJECT_MANIFEST.md) for repository orientation.
2. Read [docs/INDEX.md](docs/INDEX.md) to find governance, architecture and design artifacts.
3. Read the [Conflict Register](docs/reports/engineering/CONFLICT_REGISTER.md) before acting on anything in either of the above.
4. Follow the [Review Protocol](.ai/employees/REVIEW_PROTOCOL.md) for any design or documentation change.

## Current state

- **Phase:** Architecture, blocked on founder decisions.
- **Implementation:** started, contrary to the plan recorded elsewhere. `backend/` holds 7 Maven modules, 15 Java files and a 532-line test class, all committed. No module produces a bootable jar, so the repository currently builds **no deployable**. Whether this code should exist under ADR-0002 Decision 001 is open — [CR-005](docs/reports/engineering/CONFLICT_REGISTER.md#cr-005).
- **Frontend:** empty.
- **Governance:** the FGM and CIF, on which both Frozen ADRs depend, do not exist.

## Build

```
mvn -f backend/pom.xml test
```

Maven only; there is no Gradle build and no Maven wrapper, so a local Maven install is required ([F-17](docs/reports/engineering/DAR-0001-documentation-audit.md#f-17)).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) and the [Review Protocol](.ai/employees/REVIEW_PROTOCOL.md).

## Licence

**Not yet available.** `LICENSE` is present but empty; no licence has been selected. Licence choice is a founder decision — [CR-013](docs/reports/engineering/CONFLICT_REGISTER.md#cr-013). Until one is recorded, no grant of rights should be assumed.
