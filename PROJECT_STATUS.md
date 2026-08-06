# Project Status — ProjectEcho

**Document ID:** NAV-0003
**Document Type:** Navigation Document
**Status:** Active
**Version:** 1.0
**Classification:** Internal
**Owner:** Documentation & Architecture Engineering
**Date:** 2026-07-28
**Review Cadence:** On every founder decision, and on every change to the governance corpus

---

## Purpose

One page answering: what is the repository's real state, what is blocked, and who must decide what next.

## Authority

None. Every status here is derived from the artifact that owns it, and links to it. Where this page and an artifact disagree, the artifact is correct ([Documentation Standard §2](docs/reference/standards/DOCUMENTATION_STANDARD.md)).

---

## Headline

**Phase:** Architecture — **blocked.**

The repository cannot proceed to the EAD, EDF or PRD layers. The EAF cannot be ratified while its conflicts with a Frozen ADR are open, and founder decisions are required for ARBR-0001.

Work that *can* proceed without a founder decision: documentation standardisation, the `.ai` workspace, and the contribution process. That is the current workstream.

---

## Governance artifacts

| Document | Status per artifact | Notes |
|---|---|---|
| [ADR-001](docs/adr/ADR-001-career-intelligence-framework-foundations.md) | Approved — Frozen | 19 decisions. Bounds the CIF. |
| [ADR-0002](docs/adr/ADR-0002-modular-monolith-foundational-architecture.md) | Approved — Frozen | Governed by the FGM. Declares it supersedes itself ([CR-004](docs/reports/engineering/CONFLICT_REGISTER.md#cr-004)). |
| [ARBR-0001](docs/arbr/ARBR-0001.md) | Proposed for Founder Approval | Not approved. FD-001..FD-004 unresolved ([CR-008](docs/reports/engineering/CONFLICT_REGISTER.md#cr-008)). |
| [EAF v1.0 Rev 2](docs/eaf/EAF-v1.0-revision-2.md) | Draft | Unratified; never checked against the approved corpus ([CR-009](docs/reports/engineering/CONFLICT_REGISTER.md#cr-009)). |
| [Documentation Standard](docs/reference/standards/DOCUMENTATION_STANDARD.md) | Active | Non-governing. Precedence disputed by the manifest ([CR-014](docs/reports/engineering/CONFLICT_REGISTER.md#cr-014)). |
| [DAR-0001](docs/reports/engineering/DAR-0001-documentation-audit.md) | Active, v1.1 | Advisory. |
| [Conflict Register](docs/reports/engineering/CONFLICT_REGISTER.md) | Active | 11 open, 3 resolved. |
| FGM | **Proposed** | Awaiting Ratification. |
| CIF | **Proposed** | Awaiting Ratification. |
| EAD, EDF, PRD, RAR, GAR | **Draft / Not yet available** | Blocked downstream. |

---

## Repository state

`[FACT]`, verified 2026-07-28.

| Area | State |
|---|---|
| `backend/` | 7 Maven modules. 15 Java files, all in `common` (14 exception/error classes + a 532-line `ExceptionTest`). Every module sets `spring-boot-maven-plugin <skip>true</skip>` — **no deployable is produced.** `gateway` depends on Spring Cloud Gateway and Eureka, which have no role in a monolith ([CR-005](docs/reports/engineering/CONFLICT_REGISTER.md#cr-005)). |
| `frontend/` | Empty. |
| `infrastructure/`, `shared/`, `tools/`, `scripts/`, `config/` | Empty or unpopulated. |
| `docker-compose.yml` | Runs PostgreSQL, Redis, and **Kafka** (`apache/kafka:4.0.0`) plus a Kafka UI. Kafka appears in no approved stack list ([CR-006](docs/reports/engineering/CONFLICT_REGISTER.md#cr-006)). |
| Build | Maven only. No wrapper ([F-17](docs/reports/engineering/DAR-0001-documentation-audit.md#f-17)). |
| Git | Governance corpus committed as of `b79ec82`. No build output is tracked. No tag marks either Frozen ADR. |
| `LICENSE` | Empty — no licence selected ([CR-013](docs/reports/engineering/CONFLICT_REGISTER.md#cr-013)). |

---

## Blocked on founder decision

Ordered by how much each unblocks.

| # | Decision | Blocks | Conflict |
|---|---|---|---|
| 1 | **Record the ARBR-0001 outcome**, incl. FD-001..FD-004. | Module governance rules | [CR-008](docs/reports/engineering/CONFLICT_REGISTER.md#cr-008) |
| 2 | **Is Kafka in the frozen stack?** | Infrastructure, event design | [CR-006](docs/reports/engineering/CONFLICT_REGISTER.md#cr-006) |
| 3 | **Resolve the five EAF questions.** | EAF ratification | [CR-009](docs/reports/engineering/CONFLICT_REGISTER.md#cr-009) |
| 4 | **Locate or re-issue Product Impact Report 001.** | ADR-0002 traceability, tenancy | [CR-011](docs/reports/engineering/CONFLICT_REGISTER.md#cr-011) |
| 5 | **Document precedence:** manifest vs Documentation Standard. | Which document wins any dispute | [CR-014](docs/reports/engineering/CONFLICT_REGISTER.md#cr-014) |
| 6 | **Choose a licence.** | Public release | [CR-013](docs/reports/engineering/CONFLICT_REGISTER.md#cr-013) |

New ADRs proposed by DAR-0001: identifier correction for the superseded microservice ADR; disposition of the backend scaffold; event transport. Identifiers are **not** assigned — issuing one is a governance act under the FGM.

---

## Documentation workstream

| Item | State |
|---|---|
| Documentation Standard | Done |
| DAR-0001 audit | Done, v1.1 (v1.0 remediation reporting corrected) |
| Conflict Register | Done |
| `docs/templates/*` (7) | Done |
| `.ai/context/*`, `.ai/core/*` corrections | Done |
| `PROJECT_MANIFEST.md`, `README.md` | Done |
| `PROJECT_STATUS.md` | This document |
| `docs/INDEX.md`, `docs/README.md` | In progress |
| `.ai/employees/*`, `.ai/prompts/*`, `.ai/knowledge/*`, `.ai/templates/*` | In progress — 26 files currently 0 bytes |
| `CONTRIBUTING.md`, PR template | In progress |
| CI workflows | **Not started, deliberately.** CI design is EAD-level; the EAD is blocked. |
| `PROJECT_PLAN.md` | **Not created, deliberately.** A plan requires owners and dates. Neither exists to be recorded, and inventing them would manufacture commitments no founder made. The phase roadmap in `PROJECT_MANIFEST.md` is the nearest governed artifact. |

---

## What this document deliberately does not do

It does not resolve any conflict, assign any owner, set any date, or infer a product direction. Every such act belongs to a founder or to a governance document, and both are recorded above as outstanding.
