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

## What ProjectEcho is

ProjectEcho is a **Career Intelligence Platform** replacing static resumes with evidence-driven, explainable career profiles. This product identity was formally established and resolved by the founders via [FDR-001](docs/decisions/founders/FDR-001.md).

## Quick links

- Project manifest: [PROJECT_MANIFEST.md](PROJECT_MANIFEST.md) — note [CR-014](docs/reports/engineering/CONFLICT_REGISTER.md#cr-014) on its claimed precedence
- Documentation index: [docs/INDEX.md](docs/INDEX.md)
- Documentation standard: [docs/reference/standards/DOCUMENTATION_STANDARD.md](docs/reference/standards/DOCUMENTATION_STANDARD.md)
- ADR-001 — [Career Intelligence Framework Foundations](docs/adr/ADR-001-career-intelligence-framework-foundations.md) (Frozen)
- ADR-0002 — [Modular Monolith](docs/adr/ADR-0002-modular-monolith-foundational-architecture.md) (Frozen)
- ARBR-0001 — [Architecture Review Board Report](docs/arbr/ARBR-0001.md) (Proposed for Founder Approval)
- EAF — [v1.0 Revision 2](docs/eaf/EAF-v1.0-revision-2.md) (Draft, unratified)
- AI workspace: [.ai/](.ai/)

Historical/Archived event-delivery visions: [docs/archive/PROJECT_VISION.md](docs/archive/PROJECT_VISION.md)

## Get started

1. Read [PROJECT_MANIFEST.md](PROJECT_MANIFEST.md) for repository orientation.
2. Read [docs/INDEX.md](docs/INDEX.md) to find governance, architecture and design artifacts.
3. Read the [Conflict Register](docs/reports/engineering/CONFLICT_REGISTER.md) before acting on anything in either of the above.
4. Follow the [Review Protocol](.ai/employees/REVIEW_PROTOCOL.md) for any design or documentation change.
5. Verify the backend build: `mvn -f backend/pom.xml test`

## Current state

- **Phase:** Engineering Initialization.
- **Implementation:** Governance frameworks are formally ratified. Backend module skeleton is established per EAD. Foundational ADRs (Phase 1 & 2) are pending.
- **Frontend:** empty.
- **Governance:** FGM, CIF, EAF, and EAD are formally ratified and located in `docs/reference/frameworks/`.

## Build

```
mvn -f backend/pom.xml test
```

Maven only; there is no Gradle build and no Maven wrapper, so a local Maven install is required ([F-17](docs/reports/engineering/DAR-0001-documentation-audit.md#f-17)).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) and the [Review Protocol](.ai/employees/REVIEW_PROTOCOL.md).

## Licence

**Not yet available.** `LICENSE` is present but empty; no licence has been selected. Licence choice is a founder decision — [CR-013](docs/reports/engineering/CONFLICT_REGISTER.md#cr-013). Until one is recorded, no grant of rights should be assumed.
