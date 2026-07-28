# docs/

**Document ID:** NAV-0005
**Document Type:** Navigation Document
**Status:** Active
**Version:** 1.0
**Classification:** Internal
**Owner:** Documentation & Architecture Engineering
**Date:** 2026-07-28
**Review Cadence:** On change to the `docs/` tree structure

---

## Purpose

Explains the layout of `docs/` — what belongs in each directory and which parts are populated.

## Authority

None. For the document list and statuses, see [INDEX.md](INDEX.md). For conventions, see the [Documentation Standard](reference/standards/DOCUMENTATION_STANDARD.md).

---

## Where to start

[INDEX.md](INDEX.md) lists every document with its status. [../PROJECT_STATUS.md](../PROJECT_STATUS.md) says what is blocked. [reports/engineering/CONFLICT_REGISTER.md](reports/engineering/CONFLICT_REGISTER.md) says why.

## Layout

Directories are named for the artifact type they hold. A directory exists even when empty, so that the intended structure is visible and an artifact has an unambiguous home when it is written.

| Directory | Holds | Populated |
|---|---|---|
| `adr/` | Architecture Decision Records | Yes — 2 |
| `arbr/` | Architecture Review Board Reports | Yes — 1 |
| `eaf/` | Engineering Architecture Framework | Yes — 1, Draft |
| `templates/` | Document templates | Yes — 7 |
| `reference/standards/` | Repository standards | Yes — 1 |
| `reports/engineering/` | Engineering reports and audits | Yes — 2 |
| `archive/` | Superseded and historical material | Yes — see caution below |
| `fgm/` | Framework Governance Model | No — blocking gap |
| `cif/` | Career Intelligence Framework | No — blocking gap |
| `ead/`, `edf/`, `prd/` | Downstream architecture and product | No — blocked |
| `rar/`, `gar/` | Repository and governance audit reports | No |
| `decisions/founders/`, `decisions/engineering/`, `decisions/product/` | Recorded decisions | No — see below |
| `api/`, `business/`, `diagrams/`, `research/`, `assets/`, `reference/glossary/`, `reference/third-party/` | As named | No |

## Two things worth knowing before you read anything here

**`decisions/` is empty.** No founder decision exists as an artifact anywhere in this repository, yet ADR-0002 cites "Product Impact Report 001 Founder Decisions A–E" as binding. Decisions referenced but never filed are tracked as [CR-011](reports/engineering/CONFLICT_REGISTER.md#cr-011).

**`archive/` is not simply history.** The archived architecture set and two live documents at this level — `PROJECT_VISION.md` and `ENGINEERING_GUIDE.md` — describe a *different product* from the one the approved ADRs govern. Neither set carries a supersession marker, so which is current is an open founder decision ([CR-001](reports/engineering/CONFLICT_REGISTER.md#cr-001)). Nothing in `archive/` should be deleted while that is unresolved.

## Adding a document

1. Read the [Documentation Standard](reference/standards/DOCUMENTATION_STANDARD.md) — metadata block (§1), status values (§2), required sections (§3), naming (§6), linking (§7).
2. Start from the matching file in [`templates/`](templates/).
3. Place it in the directory for its type.
4. Add it to [INDEX.md](INDEX.md).
5. If you find a contradiction while writing, record it in the [Conflict Register](reports/engineering/CONFLICT_REGISTER.md) and escalate. Do not resolve it in your document — discovering a conflict is not authority to settle it (§8).

## Diagrams

Mermaid, inline in the document that uses them, so they version with their text. Diagrams are derived and never authoritative; where a diagram and its text disagree, the text wins (§11).
