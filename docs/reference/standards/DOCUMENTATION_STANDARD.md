# Documentation Standard

**Document ID:** STD-DOC-0001
**Document Type:** Repository Standard (non-governing)
**Status:** Active
**Version:** 1.0
**Classification:** Internal
**Owner:** Documentation & Architecture Engineering
**Date:** 2026-07-27
**Review Cadence:** On change to the governance document set; otherwise quarterly
**Governed By:** Framework Governance Model (FGM) — *Not yet available*. Until the FGM exists, this standard is advisory and describes conventions already evidenced in the approved corpus.

---

## Purpose

This document defines the structural conventions every document in this repository follows: the metadata block, the required sections, the naming rules, the linking rules, and the terminology rules.

It exists so that a document's authority, ownership, and lifecycle can be determined from the document itself, without external context, by a human or by an AI assistant.

## Authority

This is a **repository standard**, not a governance document. It constrains the *form* of documents. It has no authority over their *content*, and it may not be used to justify a change to any approved decision.

Where this standard conflicts with an approved ADR, framework, or founder decision, the approved document wins and this standard is defective and must be corrected.

## Scope

Applies to every Markdown document in the repository, including `docs/`, `.ai/`, and root-level documents.

## Out of Scope

- The content, correctness, or approval of any decision.
- Source code comments, Javadoc, and generated API documentation.
- The governance lifecycle itself (Draft → Review → Approved → Frozen → Superseded), which is the FGM's responsibility and is only *referenced* here.

## Dependencies

This standard derives its conventions from the structures already used by [ADR-001](../../adr/ADR-001-career-intelligence-framework-foundations.md), [ADR-0002](../../adr/ADR-0002-modular-monolith-foundational-architecture.md), [ARBR-0001](../../arbr/ARBR-0001.md), and [EAF v1.0 Rev 2](../../eaf/EAF-v1.0-revision-2.md). It codifies observed practice; it does not introduce new practice.

## Related Documents

- [Documentation Index](../../INDEX.md)
- [Documentation Audit — DAR-0001](../../reports/engineering/DAR-0001-documentation-audit.md)
- [Writing Style](../../../.ai/archive/core/STYLE_GUIDE.md)

---

## 1. Metadata Block

Every document begins with a level-1 heading, followed immediately by a metadata block of bold key/value lines, followed by a horizontal rule.

The bold key/value form is used rather than YAML front matter because it is the form already established across the approved corpus, and because it renders legibly in every Markdown viewer. It remains machine-parseable with a trivial line regex.

### Required on every document

| Field | Meaning |
|---|---|
| **Document ID** | Stable identifier. Never reused, never renumbered after approval. |
| **Document Type** | The class of document (ADR, ARBR, Framework, Standard, Navigation, Report, Template). |
| **Status** | Lifecycle stage. See §2. |
| **Version** | Semantic version of the document, not of the system. |
| **Classification** | `Public`, `Internal`, or `Restricted`. |
| **Owner** | The accountable role, not a person's name. Roles are defined in [`.ai/archive/employees/`](../../../.ai/archive/employees/). |
| **Date** | ISO-8601 date of the current version. |
| **Review Cadence** | When this document must be re-examined even if nothing changed. |

### Required on governing documents

Governing documents are those that appear in the Authoritative Precedence chain (§4): ADR, ARBR, RAR, GAR, FGM, CIF, EAF, EAD, EDF, PRD.

| Field | Meaning |
|---|---|
| **Governed By** | The document one level above in the precedence chain. |
| **Supersedes** | Document IDs this replaces, or `None`. |
| **Superseded By** | Document ID that replaced this, or `None`. |
| **Related Documents** | Documents that constrain or are constrained by this one. |
| **Amendment Policy** | How this document may be changed, especially once Frozen. |

### Prohibited

- A document must not claim a **Status** its governing process has not granted it. A document is `Approved` only when the approving authority has recorded the approval.
- A **Governed By** value must not point at a document that does not exist without the marker `— Not yet available`.

---

## 2. Status Values

| Status | Meaning | May implementation rely on it? |
|---|---|---|
| `Draft` | Being written. Content may change without notice. | No |
| `In Review` | Submitted to a review authority. | No |
| `Proposed for Founder Approval` | Review complete; awaiting founder decision. | No |
| `Approved` | Accepted by the approving authority. | Yes |
| `Approved — Frozen` | Accepted and closed to edits. Changes require a superseding document. | Yes |
| `Superseded` | Replaced. Retained for traceability. | No |
| `Active` | Applies to non-governing standards and navigation documents that have no approval gate. | N/A |

A document's status is stated in exactly one place: its own metadata block. Any other document that reports a status is reporting a *derived* value and must link to the source rather than restate it as fact.

---

## 3. Required Sections

Every **major** document (governing documents, frameworks, standards) includes these sections, in this order, before its body:

1. **Purpose** — why this document exists.
2. **Authority** — what this document may and may not decide.
3. **Scope** — what it covers.
4. **Out of Scope** — what it deliberately does not cover, and where that lives instead.
5. **Dependencies** — what must be true or must exist for this document to be valid.
6. **Related Documents** — the local neighbourhood of the document graph.

Navigation documents (§5) require only **Purpose** and **Authority**.

Records with a fixed established shape — ADRs and ARBRs — keep their established shape. Their per-decision structure (Decision / Context / Alternatives Considered / Why Alternatives Were Rejected / Final Decision / Consequences / Future Revisit Conditions) is normative and is reproduced in [`ADR_TEMPLATE.md`](../../templates/ADR_TEMPLATE.md).

---

## 4. Authoritative Precedence

Documents are ordered. When two documents conflict, the higher one wins, and the lower one is defective.

```
Founder Decisions
  → Framework Governance Model (FGM)
    → Domain Framework Owners (CIF · Business Framework · Research Framework)
      → Architecture Decision Records (ADR)
        → Repository Architecture Reports (RAR)
          → Architecture Review Board Reports (ARBR — advisory)
            → Engineering Architecture Framework (EAF)
              → Engineering Architecture Document (EAD)
                → Engineering Design Framework (EDF)
                  → Product Requirements Documents (PRD)
                    → Engineering Guides
                      → Repository Documents
                        → Implementation
```

A lower document may **elaborate** a higher one. It may never **contradict**, **redefine**, or **relax** one.

Discovering a conflict is not authority to resolve it. Conflicts are recorded in a Documentation Audit Report and escalated per §8.

---

## 5. Navigation Documents

`README.md`, `PROJECT_MANIFEST.md`, `docs/INDEX.md`, `PROJECT_PLAN.md`, and `PROJECT_STATUS.md` are **navigation documents**.

They:

- point at governing documents,
- summarise only what those documents already state,
- and link to the source of every claim.

They must never introduce a decision, a principle, a constraint, or a status that does not already exist in a governing document. A navigation document that is the *only* source for a fact is a governance defect, because it means an ungoverned decision has been made.

---

## 6. Naming and Identifiers

| Artifact | Pattern | Example |
|---|---|---|
| ADR | `ADR-NNNN-kebab-case-title.md` | `ADR-0003-tenancy-model.md` |
| ARBR | `ARBR-NNNN.md` | `ARBR-0002.md` |
| RAR | `RAR-NNNN-kebab-case-title.md` | `RAR-0001-repository-structure.md` |
| Framework revision | `<ABBR>-vMAJOR.MINOR-revision-N.md` | `EAF-v1.0-revision-2.md` |
| Report | `<TYPE>-NNNN-kebab-case-title.md` | `DAR-0001-documentation-audit.md` |
| Template | `<TYPE>_TEMPLATE.md` | `EAD_TEMPLATE.md` |
| Standard | `SCREAMING_SNAKE.md` | `DOCUMENTATION_STANDARD.md` |

Identifiers use **four digits** (`ADR-0001`). Three-digit identifiers exist in the current corpus and are a known defect recorded in [DAR-0001](../../reports/engineering/DAR-0001-documentation-audit.md); frozen documents are not renamed, because their IDs are cited elsewhere.

An identifier, once issued, is never reused — including for a document that was deleted before approval.

---

## 7. Linking

- Links between repository documents are **relative** and resolve from the linking file's directory.
- Every reference to another document is a link, not a bare name. `the EAF` is prose; `[EAF](../eaf/EAF-v1.0-revision-2.md)` is a reference.
- A reference to a document that does not yet exist is written as: **Document Name** — *Not yet available*. It is never written as a link to a non-existent path, because a broken link is indistinguishable from a mistake.
- Prefer one authoritative statement plus links over repeating content. Duplicated content diverges; links do not.

---

## 8. Recording Problems

Documentation work does not resolve governance conflicts. When a conflict, contradiction, or gap is found:

1. Record it in a **Documentation Audit Report** (`docs/reports/engineering/DAR-NNNN-*.md`).
2. State, for each finding: **Observation**, **Evidence** (file and line), **Impact**, **Affected Documents**, **Recommendation**, and **Resolution Authority** — the specific founder decision, ADR, or framework update required.
3. Change nothing in the conflicting documents beyond adding a link to the finding.

Findings are classified using the evidence tags from the [Review Protocol](../../../.ai/workflows/REVIEW_PROTOCOL.md): `[FACT]`, `[OBSERVATION]`, `[INFERENCE]`, `[SPECULATION]`.

---

## 9. Terminology

Business terminology is owned by the **CIF**. Structural terminology is owned by the **EAF**. Neither may be redefined elsewhere.

Until the CIF exists, business terms used in this repository are those fixed by [ADR-001](../../adr/ADR-001-career-intelligence-framework-foundations.md): Signal, Evidence, Career DNA, Career Knowledge Graph, Skill Graph, Confidence, Readiness, Recommendation, Mission, Capability, Explainability Contract.

Structural terms are those catalogued in [EAF Parts I–IX](../../eaf/EAF-v1.0-revision-2.md), noting that the EAF is `Draft` and its terms are not yet ratified.

When a document uses a term from either set, it uses it with exactly that meaning, and does not restate the definition.

---

## 10. Writing Rules

Derived from [`.ai/archive/core/STYLE_GUIDE.md`](../../../.ai/archive/core/STYLE_GUIDE.md):

- Precise engineering language. No marketing language.
- Deterministic statements. State assumptions explicitly.
- Separate `FACT` / `OBSERVATION` / `INFERENCE` / `SPECULATION` where a claim is not directly evidenced.
- Never invent a founder decision.
- Where information is genuinely unavailable, use the canonical phrase **"Not yet available."** Do not guess, and do not omit silently.

---

## 11. Diagrams

Diagrams are written in Mermaid, inline in the document that uses them, so they version with their text.

Diagrams are **derived**, never authoritative. A diagram may only depict relationships stated in text elsewhere. Where a diagram and its text disagree, the text wins and the diagram is defective.

Shared diagrams that several documents reference live under `docs/diagrams/` and are linked, not copied.
