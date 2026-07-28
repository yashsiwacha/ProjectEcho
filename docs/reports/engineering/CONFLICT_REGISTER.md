# Governance Conflict Register

**Document ID:** REG-CONF-0001
**Document Type:** Conflict Register (advisory, non-governing)
**Status:** Active
**Version:** 1.0
**Classification:** Internal
**Owner:** Documentation & Architecture Engineering
**Date:** 2026-07-27
**Review Cadence:** On every material change to the governance corpus, and on every founder decision
**Governed By:** Framework Governance Model (FGM) — *Not yet available*

---

## Purpose

This register is the single running list of open governance conflicts in ProjectEcho. It exists so that a conflict, once found, cannot be lost: it stays open here until a named authority closes it.

[DAR-0001](DAR-0001-documentation-audit.md) is a point-in-time audit. This register is its living counterpart — findings enter here and remain until resolved.

## Authority

**None.** This register records and tracks. It resolves nothing, assigns nothing, and closes nothing on its own. Every entry names the authority that can close it. A conflict is closed here only after the closing authority has recorded the decision on the owning artifact.

Adding an entry is documentation work. Closing an entry is a governance act.

## Scope

Conflicts between two or more documents in the repository, and conflicts between a document and the repository's actual state.

## Out of Scope

- Resolving any conflict listed here.
- Defects with a single owner and no disagreement — those are corrected directly and recorded in DAR-0001 only.

## Dependencies

- [Documentation Standard §8](../../reference/standards/DOCUMENTATION_STANDARD.md) — the procedure that requires this register.
- [DAR-0001](DAR-0001-documentation-audit.md) — the source of the seed entries.
- [Review Protocol](../../../.ai/employees/REVIEW_PROTOCOL.md) — evidence tags.

## Related Documents

- [Project Status](../../../PROJECT_STATUS.md)
- [Documentation Index](../../INDEX.md)

---

## How To Use This Register

| Field | Meaning |
|---|---|
| **ID** | `CR-NNN`. Never reused. |
| **Conflict** | The two positions that cannot both be true. |
| **Type** | `Doc↔Doc` (two documents disagree) or `Doc↔Repo` (a document disagrees with the repository). |
| **Severity** | S1–S4, per the [DAR-0001 severity scale](DAR-0001-documentation-audit.md#severity-scale). |
| **Authority** | Who can close it. |
| **Status** | `Open`, `Awaiting Founder`, `Awaiting Authoring`, `Resolved`. |
| **Blocks** | What cannot proceed while it is open. |

A conflict moves to `Resolved` only with a link to the artifact recording the resolution.

---

## Open Conflicts

| ID | Conflict | Type | Severity | Authority | Status | Blocks |
|---|---|---|---|---|---|---|
| [CR-001](#cr-001) | Product identity: Career Intelligence Platform vs event delivery backbone | Doc↔Doc | S1 | Founder + ADR | Awaiting Founder | PRD, EAD, EDF, all implementation |
| [CR-002](#cr-002) | ADR-0002 is governed by the FGM, which does not exist | Doc↔Doc | S1 | Founder + FGM authoring | Awaiting Authoring | Every future ADR, EAF ratification, ARBR formalisation |
| [CR-003](#cr-003) | ADR-001 bounds a CIF that does not exist | Doc↔Doc | S1 | Founder + CIF authoring | Awaiting Authoring | EAF ratification, all business terminology |
| [CR-004](#cr-004) | ADR-0002 declares it supersedes ADR-0002 | Doc↔Doc | S2 | New ADR | Open | Traceability of the architecture change |
| [CR-005](#cr-005) | Backend is 7 deployables; ADR-0002 Decision 001 mandates one | Doc↔Repo | S2 | Founder + ADR | Awaiting Founder | Backend direction |
| [CR-006](#cr-006) | Kafka is built and deployed but absent from the frozen stack | Doc↔Repo | S2 | Founder | Awaiting Founder | Infrastructure, event design |
| [CR-007](#cr-007) | Spring AI is in the frozen stack but in no POM | Doc↔Repo | S3 | Engineering | Open | AI integration phase |
| [CR-008](#cr-008) | ARBR-0001 reported Approved; artifact says Proposed | Doc↔Doc | S3 | Founders | Awaiting Founder | Module governance rules FD-001..FD-004 |
| [CR-009](#cr-009) | Four EAF/CIF conflicts declared open and unowned | Doc↔Doc | S2 | Founder + ARB | Awaiting Founder | EAF ratification, therefore EAD |
| [CR-010](#cr-010) | ADR identifier width inconsistent; ADR-0001 deleted while cited | Doc↔Doc | S3 | Governance | Open | Citation integrity |
| [CR-011](#cr-011) | Product Impact Report 001 cited by a Frozen ADR but absent | Doc↔Doc | S3 | Founders | Awaiting Founder | ADR-0002 traceability, tenancy constraint |
| [CR-012](#cr-012) | `backend/common` module contravenes the Engineering Guide | Doc↔Repo | S3 | Engineering | Open | Nothing; corrected at module reshape |
| [CR-013](#cr-013) | Licence unchosen while README directs readers to it | Doc↔Repo | S4 | Founder | Awaiting Founder | Public release |
| [CR-014](#cr-014) | Manifest claims rank above ADRs; Documentation Standard ranks it below | Doc↔Doc | S2 | Founder + FGM | Awaiting Founder | Which document wins any future disagreement |

No conflict in this register has been resolved by documentation work.

---

## Conflict Detail

### CR-001 — Product identity {#cr-001}

**Positions.** (A) ProjectEcho is a Career Intelligence Platform — `README.md`, `PROJECT_MANIFEST.md`, ADR-001, ADR-0002, ARBR-0001, EAF Rev 2, `.ai/core/PROJECT_CONTEXT.md`. (B) ProjectEcho is an event delivery / outbound webhook backbone — `docs/PROJECT_VISION.md`, `docs/ENGINEERING_GUIDE.md`, `docs/archive/architecture/*`, and the current `docker-compose.yml`.

**Why it cannot self-resolve.** Both sets are internally coherent and neither carries a supersession marker. The only document stating a product vision, target users, MVP scope and success metrics describes (B); the entire approved governance chain describes (A).

**Authority.** Founder decision, recorded as an ADR. See [DAR-0001 F-01](DAR-0001-documentation-audit.md#f-01) for the three candidate resolutions.

**Status.** Awaiting Founder.

---

### CR-002 — Missing FGM {#cr-002}

**Positions.** ADR-0002 cites FGM Parts III and IV, Part I-A, Section 41 and Section 15 as binding. No FGM exists.

**Why it matters.** The procedure to supersede ADR-0002 is defined only in the missing document — and CR-001 and CR-005 may both require exactly that supersession. The register cannot advise a route around this.

**Authority.** Founder decision on the governance model, then FGM authoring. Not draftable as documentation work: authoring it would be inventing governance.

**Status.** Awaiting Authoring.

---

### CR-003 — Missing CIF {#cr-003}

**Positions.** ADR-001's stated purpose is to bound the CIF; 19 frozen decisions constrain it. `docs/cif/` holds only an empty `diagrams/`.

**Consequence.** The CIF owns all business terminology per the precedence chain, so every business term in the repository is currently ungoverned in practice. Five ADR-001 open items (F, G, H, M, N) are addressed to it.

**Authority.** Founder decision on ownership and commissioning.

**Status.** Awaiting Authoring.

---

### CR-004 — Self-supersession {#cr-004}

**Positions.** ADR-0002 states `Supersedes: ADR-0002`. Two distinct architectures share one identifier; the superseded text is not in the repository.

**Constraint.** ADR-0002 is Frozen and must not be edited. The correction is a new ADR that assigns a distinct identifier to the superseded decision.

**Authority.** New ADR. Identifier assignment is a governance act.

**Status.** Open.

---

### CR-005 — Backend shape vs Decision 001 {#cr-005}

**Positions.** ADR-0002 Decision 001 mandates "one deployment unit". `backend/` is seven independently-versioned Maven artifacts; `gateway` depends on Spring Cloud Gateway and Eureka client; every module sets `spring-boot-maven-plugin` `<skip>true</skip>`, so the repository produces no deployable at all.

**Aggravating factor.** ADR-0002's own Self-Review assumes no substantial implementation exists under the superseded direction. That assumption is false, which means the migration-cost framing in a Frozen document understates reality. The migration-scope assessment ADR-0002 deferred has no owner and no date.

**Authority.** Founder decision naming the migration owner and date, then engineering execution.

**Status.** Awaiting Founder.

---

### CR-006 — Kafka {#cr-006}

**Positions.** The frozen founder stack in ADR-0002 neither permits nor forbids Kafka. Kafka is managed in `backend/pom.xml`, depended on by `backend/notification`, run by `docker-compose.yml` as `apache/kafka:4.0.0`, and configured in `.env.example`.

**Note.** Kafka's presence corroborates CR-001 position (B), where Kafka is the documented event backbone. Resolving CR-001 may resolve this.

**Authority.** Founder decision. If Kafka is in, ADR-0002's stack list needs a superseding amendment — it cannot be edited.

**Status.** Awaiting Founder.

---

### CR-007 — Spring AI absent from the build {#cr-007}

**Positions.** Spring AI is named in the frozen stack and in `.ai/core/PROJECT_CONTEXT.md`. It appears in no POM.

**Assessment** `[INFERENCE]` — this is a not-yet-done rather than a disagreement, since AI integration is a later phase. Recorded because a frozen-stack element that is absent from the build is indistinguishable from drift until someone checks.

**Authority.** Engineering, at the AI integration phase.

**Status.** Open.

---

### CR-008 — ARBR-0001 status {#cr-008}

**Positions.** `ARBR-0001` metadata reads "Proposed for Founder Approval" and lists FD-001..FD-004 unresolved. `PROJECT_MANIFEST.md` and `.ai/context/CHANGELOG.md` reported it Approved.

**Action taken.** The two navigation documents have been corrected to report the status the artifact carries and to link to it, per Documentation Standard §2. **The underlying question — whether the founders approved it — is untouched and remains open.** FD-002, FD-003 and FD-004 are governance rules whose binding force is currently undetermined.

**Authority.** Founders, recorded on `ARBR-0001` itself.

**Status.** Awaiting Founder.

---

### CR-009 — EAF/CIF conflicts {#cr-009}

**Positions.** ADR-0002's Self-Review declares three conflicts open and out of scope: Career DNA as a data-owning structure, non-contextual Readiness, and Engines consuming raw Signals. Each is directly contradicted by a Frozen ADR-001 decision (005, 012, 008 respectively). The EAF additionally lists five founder decisions still required.

**Assessment** `[INFERENCE]` — these should resolve in favour of ADR-001, which is Frozen and higher in the precedence chain. That determination is still a governance act and is not made here.

**Authority.** Founder decision on the five EAF questions, then ARB ratification.

**Status.** Awaiting Founder.

---

### CR-010 — Identifier width and the deleted ADR-0001 {#cr-010}

**Positions.** Both `ADR-001` and `ADR-0002` widths are in use. A prior `docs/adr/ADR-0001.md` was committed empty and no longer exists on disk. `.ai/context/CURRENT_STATE.md` credited "ADR-0001" as complete, and the EAF Self-Review cites "ADR-0001" repeatedly.

**Unresolvable here.** Whether `ADR-001` and `ADR-0001` are one artifact or two, one of which was lost, requires knowledge this audit does not have. Renaming is not an option: `ADR-001` is Frozen and cited.

**Authority.** Governance decision, recorded as an errata note.

**Status.** Open.

---

### CR-011 — Product Impact Report 001 {#cr-011}

**Positions.** ADR-0002 cites "Product Impact Report 001 Founder Decisions A–E", and attributes the future-only Tenant constraint to Founder Decision A. No such report exists.

**Consequence.** Founder decisions load-bearing for a Frozen ADR are unreadable. The tenancy constraint is independently listed by the EAF as a founder decision still required — so the repository both relies on it and asks for it.

**Authority.** Founders — locate and file under `docs/decisions/founders/`, or re-issue A–E.

**Status.** Awaiting Founder.

---

### CR-012 — `backend/common` {#cr-012}

**Positions.** `docs/ENGINEERING_GUIDE.md` prohibits generic catch-all packages, naming `common` explicitly. `backend/common` exists and holds the exception hierarchy and error codes.

**Caveat.** `ENGINEERING_GUIDE.md` is a CR-001 position (B) document. If (B) is superseded, the authority for this prohibition needs re-establishing under position (A).

**Authority.** Engineering, at module reshape under CR-005.

**Status.** Open.

---

### CR-013 — Licence {#cr-013}

**Positions.** `README.md` directs readers to `LICENSE`. `LICENSE` is 0 bytes.

**Authority.** Founder. Licence selection carries legal consequence and is not a documentation default.

**Status.** Awaiting Founder.

---

### CR-014 — Precedence inversion between the manifest and the Documentation Standard {#cr-014}

**Positions.**

(A) `PROJECT_MANIFEST.md` "Documentation Hierarchy" ranks itself **first** — "PROJECT_MANIFEST.md (this file) — canonical single source of truth" — with governance (ADRs, ARBRs, RARs) fourth and architecture fifth. Its "Repository Philosophy" adds "Documentation is authoritative".

(B) [Documentation Standard §4](../../reference/standards/DOCUMENTATION_STANDARD.md) places Repository Documents **second from bottom**, beneath Founder Decisions, FGM, domain frameworks, ADRs, RARs, ARBRs, EAF, EAD, EDF and PRDs. §5 further forbids navigation documents from introducing any decision, principle, constraint or status not already in a governing document.

Under (A) the manifest overrides ADR-001 and ADR-0002. Under (B) the manifest may not contradict them at all. Both cannot hold.

**Second limb — ungoverned content.** The manifest states Company Vision, Mission, Product Vision, Engineering Philosophy, Architecture Philosophy and Repository Standards with no citation to any governing document. There is no PRD and no CIF ([CR-003](#cr-003)) for these to derive from. Under (B) §5 this is a governance defect by definition: a navigation document is the sole source of load-bearing product statements. Under (A) it is correct and intended.

**Why it cannot self-resolve.** The Documentation Standard is explicitly non-governing and concedes that an approved ADR, framework or founder decision beats it. The manifest is none of those, so the standard does not automatically lose — but neither document has authority to fix document rank. Ranking documents is precisely the FGM's job, and the FGM does not exist ([CR-002](#cr-002)).

**Interaction with CR-001.** If (A) holds, the manifest's Product Vision is a governing statement of product identity and would settle [CR-001](#cr-001) in favour of the Career Intelligence Platform without a founder decision. This register does not accept that route: a navigation document settling the repository's central open question by self-declared rank is exactly the outcome the escalation rules exist to prevent.

**Action taken.** None beyond adding a cross-reference to this entry in `PROJECT_MANIFEST.md`, as permitted by [Documentation Standard §8.3](../../reference/standards/DOCUMENTATION_STANDARD.md). The hierarchy text is unchanged.

**Authority.** Founder decision on document precedence, then codification in the FGM.

**Status.** Awaiting Founder.

---

## Resolved Conflicts

| ID | Conflict | Resolved By | Date |
|---|---|---|---|

None. No entry has been closed.

---

## Corrected Without Conflict

Recorded for completeness. These were single-owner factual defects with no disagreement between authorities, corrected as documentation work and logged in DAR-0001 rather than tracked here.

| Defect | Correction | Status | Finding |
|---|---|---|---|
| Build tool reported as Gradle in three places | Corrected to Maven | `.ai` done 2026-07-28; `PROJECT_MANIFEST.md` pending | [F-09](DAR-0001-documentation-audit.md#f-09) |
| "Implementation not started" contradicted by committed code | Corrected to describe actual state | `.ai` done 2026-07-28; `README.md` + manifest pending | [F-08](DAR-0001-documentation-audit.md#f-08) |
| Governance corpus untracked in git | Committed in `b79ec82` | Resolved; signing/tagging residual open | [F-16](DAR-0001-documentation-audit.md#f-16) |
| No Maven wrapper; `.gitignore` would exclude one | — | Open, engineering | [F-17](DAR-0001-documentation-audit.md#f-17) |

The governance question of whether the committed backend code *should* exist is **not** corrected. It is [CR-005](#cr-005).

**Withdrawn finding.** DAR-0001 v1.0 F-14 asserted that build output was committed. That was false — verified via `git ls-files` and `git check-ignore` on 2026-07-28. No conflict was ever raised from it, so nothing in this register depended on it. See the [DAR-0001 Errata](DAR-0001-documentation-audit.md#errata).
