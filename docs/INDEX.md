# Documentation Index

**Document ID:** NAV-0004
**Document Type:** Navigation Document
**Status:** Active
**Version:** 1.0
**Classification:** Internal
**Owner:** Documentation & Architecture Engineering
**Date:** 2026-07-28
**Review Cadence:** On every document added, removed, or re-statused

---

## Purpose

The complete list of documents in this repository, what each one is, and whether it may be relied upon.

## Authority

None. Statuses shown are derived from each artifact's own metadata block and link to it ([Documentation Standard §2](reference/standards/DOCUMENTATION_STANDARD.md)).

---

## Reliance key

| Marker | Meaning |
|---|---|
| ✅ | Approved or Active. May be relied upon. |
| ⚠️ | Exists but is not ratified, not approved, or subject to an open conflict. |
| ⛔ | Referenced by other documents but **does not exist**. |

---

## Governance

| Document | Status | Reliance |
|---|---|---|
| [ADR-001 — Career Intelligence Framework Foundations](adr/ADR-001-career-intelligence-framework-foundations.md) | Approved — Frozen | ✅ |
| [ADR-0002 — Modular Monolith Foundational Architecture](adr/ADR-0002-modular-monolith-foundational-architecture.md) | Approved — Frozen | ✅ |
| [ARBR-0001 — Architecture Review Board Report](arbr/ARBR-0001.md) | Proposed for Founder Approval | ⚠️ [CR-008](reports/engineering/CONFLICT_REGISTER.md#cr-008) |
| **Framework Governance Model (FGM)** | *Not yet available* | ⛔ [CR-002](reports/engineering/CONFLICT_REGISTER.md#cr-002) |
| **Career Intelligence Framework (CIF)** | *Not yet available* | ⛔ [CR-003](reports/engineering/CONFLICT_REGISTER.md#cr-003) |
| **Product Impact Report 001** | *Not yet available* | ⛔ [CR-011](reports/engineering/CONFLICT_REGISTER.md#cr-011) |
| **RAR — Repository Architecture Reports** | *Not yet available* | ⛔ |
| **GAR — Governance Audit Reports** | *Not yet available* | ⛔ |

`decisions/founders/`, `decisions/engineering/` and `decisions/product/` are empty. No founder decision is recorded as an artifact anywhere in the repository.

## Architecture

| Document | Status | Reliance |
|---|---|---|
| [EAF v1.0 Revision 2](eaf/EAF-v1.0-revision-2.md) | Draft | ⚠️ [CR-009](reports/engineering/CONFLICT_REGISTER.md#cr-009) |
| **EAD — Engineering Architecture Document** | *Not yet available* | ⛔ blocked |
| **EDF — Engineering Design Framework** | *Not yet available* | ⛔ blocked |
| **PRD — Product Requirements Documents** | *Not yet available* | ⛔ blocked by [CR-001](reports/engineering/CONFLICT_REGISTER.md#cr-001) |

## Standards and reports

| Document | Status | Reliance |
|---|---|---|
| [Documentation Standard](reference/standards/DOCUMENTATION_STANDARD.md) | Active | ✅ (precedence disputed — [CR-014](reports/engineering/CONFLICT_REGISTER.md#cr-014)) |
| [DAR-0001 — Documentation & Repository Audit](reports/engineering/DAR-0001-documentation-audit.md) | Active, v1.1 | ✅ advisory |
| [Conflict Register](reports/engineering/CONFLICT_REGISTER.md) | Active | ✅ advisory |

## Navigation

| Document | Status | Reliance |
|---|---|---|
| [README.md](../README.md) | Active | ✅ |
| [PROJECT_MANIFEST.md](../PROJECT_MANIFEST.md) | Active | ⚠️ [CR-014](reports/engineering/CONFLICT_REGISTER.md#cr-014) |
| [PROJECT_STATUS.md](../PROJECT_STATUS.md) | Active | ✅ |
| [docs/README.md](README.md) | Active | ✅ |
| This index | Active | ✅ |

## Templates

All seven are populated and current: [ADR](templates/ADR_TEMPLATE.md) · [ARBR](templates/ARBR_TEMPLATE.md) · [EAF](templates/EAF_TEMPLATE.md) · [EAD](templates/EAD_TEMPLATE.md) · [EDF](templates/EDF_TEMPLATE.md) · [PRD](templates/PRD_TEMPLATE.md) · [RAR](templates/RAR_TEMPLATE.md)

## Subject to CR-001 — product identity unresolved

These describe an **event delivery / webhook backbone**, not the Career Intelligence Platform. They carry no supersession marker, so their status is genuinely undetermined rather than merely stale. Do not treat them as current, and do not delete them.

| Document | Note |
|---|---|
| [PROJECT_VISION.md](PROJECT_VISION.md) | The repository's only statement of product vision, target users, MVP scope and success metrics — all describing the event backbone. |
| [ENGINEERING_GUIDE.md](ENGINEERING_GUIDE.md) | Names services `ingestion`, `routing`, `delivery`. Also the source of the `common` package prohibition ([CR-012](reports/engineering/CONFLICT_REGISTER.md#cr-012)). |
| [archive/architecture/SYSTEM_OVERVIEW.md](archive/architecture/SYSTEM_OVERVIEW.md) | Archived. |
| [archive/architecture/MICROSERVICES.md](archive/architecture/MICROSERVICES.md) | Archived. May or may not be the text superseded by ADR-0002 — unresolved, [CR-004](reports/engineering/CONFLICT_REGISTER.md#cr-004). |
| [archive/architecture/DATABASE.md](archive/architecture/DATABASE.md) | Archived. |
| [archive/architecture/EVENT_FLOW.md](archive/architecture/EVENT_FLOW.md) | Archived. Kafka topic design — relevant to [CR-006](reports/engineering/CONFLICT_REGISTER.md#cr-006). |
| `archive/architecture/SECURITY.md` | **0 bytes.** Part of the archived set but empty. |

## AI workspace

See [`.ai/README.md`](../.ai/README.md) for the workspace map. The [Review Protocol](../.ai/employees/REVIEW_PROTOCOL.md) is binding on all AI work.

## Empty directories

The following exist with no documents: `api/`, `business/`, `cif/`, `decisions/`, `diagrams/`, `ead/`, `edf/`, `fgm/`, `gar/`, `prd/`, `rar/`, `research/`, `reference/glossary/`, `reference/third-party/`, `reports/product/`, `reports/research/`, `arbr/reviews/`, `arbr/templates/`, `assets/`.

Their presence reflects intended structure, not planned near-term work. Most are blocked behind [CR-001](reports/engineering/CONFLICT_REGISTER.md#cr-001), [CR-002](reports/engineering/CONFLICT_REGISTER.md#cr-002) and [CR-003](reports/engineering/CONFLICT_REGISTER.md#cr-003).
