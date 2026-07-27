# DAR-0001 — Documentation & Repository Audit

**Document ID:** DAR-0001
**Document Type:** Documentation Audit Report (advisory, non-governing)
**Status:** Active
**Version:** 1.0
**Classification:** Internal
**Owner:** Documentation & Architecture Engineering
**Date:** 2026-07-27
**Review Cadence:** Re-run on every material change to the governance corpus
**Governed By:** Framework Governance Model (FGM) — *Not yet available*

---

## Purpose

This report records every contradiction, gap, and unsupported claim found during a full inspection of the ProjectEcho repository on 2026-07-27.

## Authority

**None.** This report decides nothing. It observes, evidences, and escalates. Every finding below states the authority required to resolve it. No finding has been resolved unilaterally, and no governing document has been edited as a result of this audit.

## Scope

Every Markdown document in the repository, plus the build files, container definitions, and repository configuration required to test whether the documentation matches the repository's actual state.

## Out of Scope

- Product decisions, business decisions, and architectural decisions.
- Correcting any finding classified as requiring founder authority.
- Source code quality.

## Dependencies

- [Documentation Standard](../../reference/standards/DOCUMENTATION_STANDARD.md) — supplies the precedence chain and the classification scheme used here.
- [Review Protocol](../../../.ai/employees/REVIEW_PROTOCOL.md) — supplies the evidence tags.

## Related Documents

- [Documentation Index](../../INDEX.md)
- [Project Status](../../../PROJECT_STATUS.md)

---

## Severity Scale

| Severity | Meaning |
|---|---|
| **S1 — Blocking** | The repository cannot proceed to the next phase until a founder resolves this. |
| **S2 — Structural** | A governing document is unsound or unreachable. Downstream work built on it inherits the defect. |
| **S3 — Consistency** | Documents disagree on a fact that a named authority can settle without a new decision. |
| **S4 — Hygiene** | Correctable by documentation work alone. Resolved or scheduled within this workstream. |

---

## Summary

| ID | Finding | Severity | Resolution Authority |
|---|---|---|---|
| [F-01](#f-01) | Two different products documented under one name | **S1** | Founder Decision |
| [F-02](#f-02) | ADR-0002 is governed by a document that does not exist (FGM) | **S1** | Founder Decision + FGM authoring |
| [F-03](#f-03) | CIF does not exist, yet ADR-001 exists solely to bound it | **S1** | Founder Decision + CIF authoring |
| [F-04](#f-04) | ADR-0002 declares it supersedes ADR-0002 | **S2** | New ADR |
| [F-05](#f-05) | Backend implementation contradicts ADR-0002 Decision 001 | **S2** | Founder Decision + ADR |
| [F-06](#f-06) | Kafka is deployed and built but is not in the frozen stack | **S2** | Founder Decision |
| [F-07](#f-07) | ARBR-0001 status reported as Approved; document says Proposed | **S3** | Founders (record the approval) |
| [F-08](#f-08) | "Implementation not started" is false | **S3** | Documentation (corrected — see §Actions) |
| [F-09](#f-09) | Build tool reported as Gradle in three places; repository uses Maven | **S3** | Documentation (corrected) |
| [F-10](#f-10) | ADR identifier scheme is inconsistent; ADR-0001 deleted while cited | **S3** | Governance decision on renumbering |
| [F-11](#f-11) | Product Impact Report 001 cited by ADR-0002 but absent | **S3** | Locate or re-issue |
| [F-12](#f-12) | Four EAF/CIF conflicts declared open and unowned | **S2** | Founder Decision |
| [F-13](#f-13) | 60+ referenced or structurally implied documents are empty or absent | **S4/S1 mixed** | Mixed — see finding |
| [F-14](#f-14) | Build output committed to version control | **S4** | Engineering |
| [F-15](#f-15) | No CI, no PR template, no contribution process, despite being referenced | **S4** | Documentation (partly corrected) |
| [F-16](#f-16) | Governance corpus is untracked in git | **S2** | Engineering — commit immediately |

---

## Findings

### F-01 — Two different products are documented under one name {#f-01}

**Severity:** S1 — Blocking

**Observation** `[FACT]`

The repository contains two mutually exclusive product definitions.

*Product A — Career Intelligence Platform.* Stated by [`README.md`](../../../README.md), [`PROJECT_MANIFEST.md`](../../../PROJECT_MANIFEST.md), [`ADR-001`](../../adr/ADR-001-career-intelligence-framework-foundations.md), [`ADR-0002`](../../adr/ADR-0002-modular-monolith-foundational-architecture.md), [`ARBR-0001`](../../arbr/ARBR-0001.md), [`EAF v1.0 Rev 2`](../../eaf/EAF-v1.0-revision-2.md), and `.ai/core/PROJECT_CONTEXT.md`. Domain vocabulary: Career DNA, Skill Graph, Evidence, Readiness, Mission, Career Passport.

*Product B — Event delivery / outbound webhook backbone.* Stated by [`docs/PROJECT_VISION.md`](../../PROJECT_VISION.md), [`docs/ENGINEERING_GUIDE.md`](../../ENGINEERING_GUIDE.md), and all four populated files in [`docs/archive/architecture/`](../../archive/architecture/). Domain vocabulary: event envelope, routing rule, destination, webhook signing, dead-letter, replay, organization/environment.

The word "career" appears nowhere in Product B's documents. The words "event envelope", "webhook", and "destination" appear nowhere in Product A's documents. These are not two views of one system; they are two systems.

**Evidence**

- `docs/PROJECT_VISION.md:33` — "ProjectEcho will be the trusted event backbone for product and operations teams".
- `PROJECT_MANIFEST.md:9` — "Replace static resumes with continuously evolving, evidence-driven Career Intelligence."
- `docs/ENGINEERING_GUIDE.md:47` — names the services as `ingestion`, `routing`, `delivery`.
- `docs/archive/architecture/EVENT_FLOW.md` — Kafka topics `echo.events.accepted.v1`, `echo.delivery.dead-lettered.v1`.

**Impact**

`PROJECT_VISION.md` is the only document in the repository that states a product vision, target users, MVP scope, and success metrics. Every one of those statements describes Product B. Meanwhile the entire approved governance chain — two frozen ADRs and a board review — describes Product A.

This means ProjectEcho currently has **no governed product definition for the product its architecture was approved for**. The PRD layer of the precedence chain is not merely empty; the nearest thing to it contradicts everything above it.

Any PRD, EAD, or EDF authored before this is resolved will inherit the ambiguity.

**Affected Documents**

`docs/PROJECT_VISION.md`, `docs/ENGINEERING_GUIDE.md`, `docs/archive/architecture/*` (4 files), `backend/` (see [F-05](#f-05)), `docker-compose.yml`, `.env.example`.

**Recommendation**

Do not merge, reconcile, or rewrite either document set. Founders should determine which of the following is true, and the answer should be recorded as an ADR:

1. Product B is superseded history. If so, `PROJECT_VISION.md` and `ENGINEERING_GUIDE.md` move to `docs/archive/`, every archived file receives a `Superseded By` marker, and a Product A vision document is commissioned.
2. Product B is the actual product and Product A's governance chain was authored against a misunderstanding. If so, ADR-001 and ADR-0002 must be superseded, not edited — both are Frozen.
3. Product A and Product B are separate initiatives that were merged into one repository in error. If so, the repository must be split.

**Resolution Authority:** Founder Decision, recorded as a new ADR. This audit cannot infer the answer, because both document sets are internally coherent and neither carries a supersession marker.

---

### F-02 — ADR-0002 is governed by a document that does not exist {#f-02}

**Severity:** S1 — Blocking

**Observation** `[FACT]`

`ADR-0002` names the Framework Governance Model as its governing authority and cites specific provisions of it: FGM Parts III and IV, FGM Part I-A ("Frozen stage"), FGM Section 41 ("Decision Impact Assessment"), and FGM Section 15 ("traceability").

No FGM exists. `docs/fgm/` contains only an empty `diagrams/` subdirectory.

**Evidence**

- `docs/adr/ADR-0002-modular-monolith-foundational-architecture.md:10` — "**Governed By:** Framework Governance Model — this ADR's lifecycle, amendment process, and conflict resolution follow FGM Parts III and IV".
- `docs/adr/ADR-0002-modular-monolith-foundational-architecture.md:17` — cites "FGM Part I-A, Frozen stage" and "FGM Section 41".
- `find docs/fgm -type f` returns nothing.

**Impact**

This is the most structurally severe finding in the repository. `ADR-0002` is `Approved — Frozen`, and its amendment policy, its freeze semantics, and its conflict-resolution procedure are all defined by reference to a document nobody can read.

Consequently:

- No one can determine the correct procedure to supersede `ADR-0002` — which [F-01](#f-01) and [F-05](#f-05) may both require.
- The "Decision Impact Assessment" section of `ADR-0002` is formatted to satisfy FGM Section 41's fields, but whether it actually satisfies them is unverifiable.
- `ARBR-0001` recommends formalising the Architecture Review Board *in the FGM* (`ARBR-0001:681`), which cannot be actioned.
- `EAF v1.0 Rev 2` explicitly flags that it could not be checked against the FGM because the FGM "was not available at drafting time".

The governance chain is anchored to a missing link at its second position.

**Recommendation**

Author the FGM as the highest-priority governance work item, before any further downstream document. Its minimum contents are already implicitly specified by its citations: a document lifecycle including a Frozen stage (Part I-A), an amendment process (Part III), a conflict-resolution procedure (Part IV), traceability requirements (Section 15), and a Decision Impact Assessment schema (Section 41).

`ARBR-0001:681–704` additionally proposes the ARB review workflow be codified there.

**Resolution Authority:** Founder Decision on governance model, then FGM authoring. **This audit did not draft an FGM, because doing so would constitute inventing governance.**

---

### F-03 — The CIF does not exist, yet ADR-001 exists solely to bound it {#f-03}

**Severity:** S1 — Blocking

**Observation** `[FACT]`

`ADR-001` is titled "Career Intelligence Framework Foundations". Its stated purpose is that "the CIF's boundaries are never re-litigated from scratch" and that "Implementation lives in the Career Intelligence Framework (CIF)". Nineteen approved decisions constrain a document that has not been written. `docs/cif/` contains only an empty `diagrams/` directory.

**Evidence**

- `docs/adr/ADR-001-career-intelligence-framework-foundations.md:16` — "Implementation lives in the Career Intelligence Framework (CIF); this document exists so the CIF's boundaries are never re-litigated from scratch."
- `docs/adr/ADR-001-...:36` — the CIF is designated "the canonical source of intelligence-model truth".
- `find docs/cif -type f` returns nothing.

**Impact**

The CIF is the owner of all business terminology per the precedence chain. Its absence means:

- `EAF v1.0 Rev 2` records as a hidden assumption that "the CIF owns 'Career Passport', 'Skill', and other business-facing terms… This has not been confirmed against ratified CIF text, since it was not available at drafting time."
- Four EAF/CIF conflicts are recorded as open in `ADR-0002` (see [F-12](#f-12)) and cannot be closed against a non-existent document.
- Five Open Items (F, G, H, M, N) carried forward by `ADR-001` are addressed to the CIF.

**Recommendation**

Commission the CIF under `ADR-001`'s nineteen frozen decisions. Note that CIF authorship is a **domain framework** activity, not a documentation activity.

**Resolution Authority:** Founder Decision on ownership and commissioning. **This audit did not draft a CIF.**

---

### F-04 — ADR-0002 declares that it supersedes ADR-0002 {#f-04}

**Severity:** S2 — Structural

**Observation** `[FACT]`

`ADR-0002` states: "**Supersedes:** ADR-0002 (Microservice Architecture) — the prior record is not modified; this is a complete replacement".

A document cannot supersede itself. Two distinct decisions currently share the identifier `ADR-0002`: the superseded microservice decision, and the modular monolith decision that replaced it. The superseded document is not present in the repository under any filename.

**Evidence**

- `docs/adr/ADR-0002-modular-monolith-foundational-architecture.md:7`.
- No file in `docs/adr/` or `docs/archive/` contains the superseded microservice ADR.

**Impact**

The citation `ADR-0002` is ambiguous — it resolves to two different architectures depending on the reader's assumption. `ADR-0002`'s own Decision 001 refers to "the now-superseded ADR-0002", which is unresolvable. Traceability from the modular monolith decision back to what it replaced is broken, which matters because `ADR-0002`'s hidden assumptions section turns on how much was built under the superseded direction — a question [F-05](#f-05) shows is live.

**Recommendation**

Issue a corrective ADR that assigns a distinct identifier to the superseded microservice decision, records it as `Superseded By: ADR-0002`, and files the original text (or a reconstruction marked as such) under that identifier. `docs/archive/architecture/MICROSERVICES.md` may be the surviving text, but this audit cannot assert that, because that file describes Product B ([F-01](#f-01)) rather than Product A.

Do not edit `ADR-0002` — it is Frozen.

**Resolution Authority:** New ADR. Identifier assignment is a governance act.

---

### F-05 — The backend contradicts ADR-0002 Decision 001 {#f-05}

**Severity:** S2 — Structural

**Observation** `[FACT]`

`ADR-0002` Decision 001 specifies "a single deployable Modular Monolith: one deployment unit". The `backend/` tree is seven independently-versioned Maven artifacts, one of which (`gateway`) depends on `spring-cloud-starter-gateway` **and** `spring-cloud-starter-netflix-eureka-client`.

An API gateway and a service-discovery client have no function inside a single-process deployment unit. They are distributed-system infrastructure.

**Evidence**

- `docs/adr/ADR-0002-...:23` — Decision 001 text.
- `backend/pom.xml` — modules `common`, `gateway`, `auth`, `user`, `notification`, `workflow`, `memory`.
- `backend/gateway/pom.xml` — `spring-cloud-starter-gateway`, `spring-cloud-starter-netflix-eureka-client`.
- Every module sets `spring-boot-maven-plugin` `<skip>true</skip>`, so no module produces a bootable jar. There is no aggregator application module. The repository currently produces **no deployable at all**.
- `backend/common/` — the module name directly contravenes `docs/ENGINEERING_GUIDE.md:56`: "Avoid generic catch-all packages such as `util`, `common`, or `manager`."

**Impact**

`ADR-0002`'s own Self-Review states: "This ADR assumes the prior, superseded ADR-0002 has not yet resulted in substantial production implementation. If meaningful Microservice-architecture code already exists, the 'no backward compatibility' framing above understates real migration cost."

**That assumption is false.** Microservice-shaped scaffolding exists and is committed. `ADR-0002` also marks *Migration Required: Yes, scope to be assessed separately* and its own Self-Review flags that "who assesses it or by when" is "a genuine gap".

That gap is now the live blocker. The migration assessment `ADR-0002` deferred has an unassigned owner and no date, and the thing to be migrated demonstrably exists.

**Affected Documents**

`backend/pom.xml` and seven module POMs, `PROJECT_MANIFEST.md` (Repository Structure section), `.ai/context/IMPLEMENTATION_STATUS.md`.

**Recommendation**

Founders should (a) name the owner and date for the migration-scope assessment `ADR-0002` deferred, and (b) decide whether the seven Maven modules are re-shaped into Modules-within-one-deployable per `ADR-0002` Decision 002, or discarded. Note `ARBR-0001` AR-001 defines a Module as "a bounded context with explicit interface, owned aggregates, owned business rules, owned persistence access" — a definition none of the seven current modules satisfies.

**Resolution Authority:** Founder Decision (migration owner), then engineering execution under `ADR-0002`.

---

### F-06 — Kafka is built and deployed but is not in the frozen stack {#f-06}

**Severity:** S2 — Structural

**Observation** `[FACT]`

`ADR-0002`'s Governance Metadata enumerates the frozen founder stack: "Java 21, Spring Boot 3, Spring AI, PostgreSQL, Redis, Docker Compose, OrbStack, single VPS, no Kubernetes/Microservices/Service Mesh/GraphQL/Terraform/Vault/AWS-specific services".

Kafka is not in that list — neither permitted nor forbidden. Yet:

- `backend/pom.xml` manages `org.apache.kafka:kafka-clients:3.9.0`,
- `backend/notification/pom.xml` depends on `spring-kafka`,
- `docker-compose.yml` runs `apache/kafka:4.0.0` in KRaft mode plus a Kafka UI,
- `.env.example` defines `KAFKA_EXTERNAL_PORT` and `KAFKA_NUM_PARTITIONS`.

Conversely, **Spring AI is in the frozen stack and appears in no POM.**

**Evidence**

- `docs/adr/ADR-0002-...:321`; `backend/pom.xml`; `docker-compose.yml`; `.env.example`.

**Impact**

`ADR-0002` Decision 006 permits "event-driven internal communication where appropriate" and Decision 004 requires infrastructure replaceability — so an in-process event mechanism is clearly governed, but an external broker is a material infrastructure commitment that no approved document authorises. Kafka's presence is strong corroborating evidence that the current build was authored for Product B ([F-01](#f-01)), where Kafka is the documented event backbone.

**Recommendation**

Founders should state explicitly whether Kafka is in or out of the frozen stack. If out, `docker-compose.yml`, `.env.example`, and the `notification` POM require correction. If in, `ADR-0002`'s stack list requires a superseding amendment — it cannot be edited, as it is Frozen.

**Resolution Authority:** Founder Decision.

---

### F-07 — ARBR-0001 is reported as Approved; the document says Proposed {#f-07}

**Severity:** S3 — Consistency

**Observation** `[FACT]`

`ARBR-0001`'s own metadata reads **"Status: Proposed for Founder Approval"**. Its §10 lists four unresolved Founder Decisions (FD-001 to FD-004), and its Action Items list "P0 — Founder review of ARBR-0001" and "P0 — Approve/Reject Founder Decisions (FD-001 to FD-004)" as outstanding.

Two navigation documents report it as approved: `PROJECT_MANIFEST.md:124` ("ARBR-0001 Approved") and `.ai/context/CHANGELOG.md` ("Approved ARBR-0001").

**Impact**

Per the Documentation Standard §2, a document's status lives in one place — its own metadata. If the founders did approve ARBR-0001, the approval was never recorded on the artifact, and FD-001 to FD-004 are silently unresolved. If they did not, two navigation documents assert an approval that never happened.

FD-002, FD-003, and FD-004 (mandatory module ownership metadata; Shared Kernel changes requiring ADR approval; versioned module interfaces) are governance rules. Whether they are binding is currently undetermined.

**Recommendation**

Founders record the decision on the artifact itself. Navigation documents then link to it rather than restating it. This audit has changed the navigation documents to report the status the artifact actually carries, and to link to it.

**Resolution Authority:** Founders — record the approval (or non-approval) and the FD-001..FD-004 outcomes on `ARBR-0001`.

---

### F-08 — "Implementation not started" is false {#f-08}

**Severity:** S3 — Consistency

**Observation** `[FACT]`

`README.md`, `PROJECT_MANIFEST.md`, and `.ai/context/IMPLEMENTATION_STATUS.md` all state implementation has not started. `PROJECT_MANIFEST.md:136` specifically lists "Backend scaffold (Maven modules) — not implemented".

The repository contains seven Maven modules, fifteen Java source files (fourteen exception classes and `ErrorCodes`/`ErrorResponse` in `backend/common`), a 532-line JUnit 5 test class with fourteen `@Nested` suites, passing Surefire reports, and built `.jar` artifacts — all committed.

**Impact**

Every AI assistant and every new engineer is told by three separate documents that no code exists. This directly caused `ADR-0002`'s hidden assumption in [F-05](#f-05) to go unchallenged.

**Recommendation**

Corrected by this workstream — the navigation documents now describe the actual state. Note the *governance* question of whether that code should exist under `ADR-0002` remains open as [F-05](#f-05); this finding concerns only the accuracy of the reporting.

**Resolution Authority:** Documentation. **Corrected.**

---

### F-09 — Build tool reported as Gradle; repository uses Maven {#f-09}

**Severity:** S3 — Consistency

**Observation** `[FACT]`

`.ai/context/TECH_STACK.md` states "Build: Gradle". `.ai/context/TODO.md` lists "[ ] Gradle Scaffold". `.ai/core/COMMANDS.md` instructs `./gradlew test`. `PROJECT_MANIFEST.md:44` hedges: "Gradle / Maven modules (backend uses Maven)".

The repository is Maven-only. No Gradle build file and no `gradlew` wrapper exist. The documented validation command cannot execute.

**Recommendation** Corrected by this workstream across the `.ai` workspace and the manifest.

**Resolution Authority:** Documentation. **Corrected.**

---

### F-10 — ADR identifier scheme is inconsistent, and ADR-0001 is deleted while still cited {#f-10}

**Severity:** S3 — Consistency

**Observation** `[FACT]`

Two identifier widths are in use: `ADR-001` (three digits) and `ADR-0002` (four digits). `git status` shows `D docs/adr/ADR-0001.md` — a file deleted from the working tree. `.ai/context/CURRENT_STATE.md` lists "✓ ADR-0001" as completed, and `EAF v1.0 Rev 2`'s Self-Review repeatedly refers to "ADR-0001".

It is therefore unclear whether `ADR-0001` and `ADR-001` are the same document, or two documents one of which was deleted.

**Impact** Citations in a Frozen document (`ADR-0002` cites "ADR-001"; the EAF cites "ADR-0001") may not resolve. Renaming a Frozen document to fix the width would break existing citations, so this cannot be fixed by file rename alone.

**Recommendation** Governance decides: adopt four digits going forward (per Documentation Standard §6), leave `ADR-001` at its existing filename because it is Frozen and cited, and record `ADR-001` and `ADR-0001` as the same artifact in an errata note — or, if they are genuinely different documents, recover the deleted one.

**Resolution Authority:** Governance decision on renumbering; requires knowledge this audit does not have about the deleted file.

---

### F-11 — Product Impact Report 001 is cited but absent {#f-11}

**Severity:** S3 — Consistency

**Observation** `[FACT]`

`ADR-0002`'s Governance Metadata cites "prior Product Impact Report 001 Founder Decisions A–E", and its Self-Review refers to "Founder Decision A, Product Impact Report 001" as the source of the constraint that Tenant is future-only.

No such report exists in the repository.

**Impact** Founder Decisions A–E are load-bearing for a Frozen ADR and are unreadable. The tenancy constraint in particular is cross-referenced by the EAF, which independently lists tenancy as Founder Decision Still Required #1.

**Recommendation** Locate and file the report under `docs/decisions/founders/`, or re-issue Founder Decisions A–E as a formal record.

**Resolution Authority:** Founders.

---

### F-12 — Four EAF/CIF conflicts are declared open and unowned {#f-12}

**Severity:** S2 — Structural

**Observation** `[FACT]`

`ADR-0002`'s Self-Review states: "This ADR does not resolve the previously identified EAF/CIF conflicts from Product Impact Report 001 (Career DNA as data-owning structure; non-contextual Readiness; Engines consuming raw Signals directly). Those conflicts remain open and are outside this ADR's scope."

Each conflict is directly contradicted by a Frozen `ADR-001` decision: Decision 005 (Career DNA is an aggregation interface, **not** data-owning), Decision 012 (Readiness is **contextual**), Decision 008 (Signal Model is the ingestion boundary).

`EAF v1.0 Rev 2` separately lists five Founder Decisions Still Required and states its entire governance-conflict section "remains provisional" because ADR-001, ADR-0002, ARBR-0001, the FGM and the CIF "were not available at drafting time".

**Impact** The EAF cannot be ratified. It is `Draft`, it has never been checked against the approved corpus, and it is the designated recipient of seven `ARBR-0001` recommendations (EAF-001 to EAF-007) that cannot be actioned into an unratified document.

**Recommendation** Route `EAF v1.0 Rev 2` through governance review with ADR-001, ADR-0002, and ARBR-0001 attached — exactly as the EAF's own closing recommendation requests. The three named conflicts should resolve in favour of `ADR-001`, which is Frozen and higher in the precedence chain, but that determination is a governance act, not a documentation act.

**Resolution Authority:** Founder Decision (the five EAF questions) + Architecture Review Board (ratification).

---

### F-13 — Referenced and structurally implied documents are empty or absent {#f-13}

**Severity:** Mixed

**Observation** `[FACT]`

Documents that are **referenced by name** in existing documents but are empty or missing:

| Document | Referenced by | Size |
|---|---|---|
| `docs/INDEX.md` | README (step 2 of getting started), MANIFEST (canonical reading order step 3) | 0 bytes → **created** |
| `docs/templates/*` (7 files) | MANIFEST: "Use templates in `docs/templates/`" | 0 bytes → **created** |
| `CONTRIBUTING.md` | README, MANIFEST | 0 bytes → **created** |
| `docs/README.md` | directory convention | 0 bytes → **created** |
| `.ai/employees/*` (8 files) | MANIFEST, README, `.ai/core/AGENTS.md` (mandatory reading) | 0 bytes → **created** |
| `.ai/prompts/*` (9 files) | MANIFEST | 0 bytes → **created** |
| `.ai/knowledge/*` (4 files) | — | 0 bytes → **created** |
| `.ai/templates/*` (4 files) | — | 0 bytes → **created** |
| `PROJECT_PLAN.md`, `PROJECT_STATUS.md` | repository role definition | absent → **created** |
| FGM | ADR-0002 (Governed By) | absent → **[F-02](#f-02)** |
| CIF | ADR-001 (entire purpose) | absent → **[F-03](#f-03)** |
| EAD | ARBR-0001 (12 assigned items), ADR-0002, EAF | absent → **blocked by F-02/F-12** |
| EDF, PRD, RAR, GAR | precedence chain | absent → **blocked** |
| `LICENSE` | README | 0 bytes → **not created; see below** |
| `CODE_OF_CONDUCT.md` | — | 0 bytes → **not created; see below** |

Empty directories with no documents: `docs/api/`, `docs/business/`, `docs/decisions/`, `docs/diagrams/`, `docs/research/`, `docs/reference/glossary/`, `docs/reference/third-party/`, `docs/gar/`, `docs/prd/`, `docs/rar/`, `infrastructure/`, `shared/`, `tools/`, `frontend/`, `.github/workflows/`, `config/*.yml`, `scripts/*.sh`.

**Actions taken and deliberately not taken**

Created: navigation documents, templates, standards, AI workspace, contribution process. These are structural and derive entirely from documents that already exist.

**Not created — and the reason:**

- **FGM, CIF, EAD, EDF, PRD, RAR** — authoring these would be inventing governance and architecture. They are commissioned, not written by documentation work.
- **`LICENSE`** — licence selection is a business decision with legal consequence. Founders must choose.
- **`CODE_OF_CONDUCT.md`** — adopting a code of conduct is a founder/organisational decision, not a documentation default.
- **`config/application-*.yml`, `scripts/*.sh`, `.github/workflows/`** — these are implementation, and implementation is explicitly gated behind the EAD.

**Resolution Authority:** Mixed; per-row above.

---

### F-14 — Build output is committed to version control {#f-14}

**Severity:** S4 — Hygiene

**Observation** `[FACT]` `backend/*/target/` is committed, including `.jar` files, `.class` files, Surefire XML reports, and Maven status files — roughly 120 tracked files of build output. `.github/modernize/java-upgrade/` contains tooling exhaust from a VS Code Java-upgrade agent, including logs probing for Java 25 against a repository pinned to Java 21.

**Impact** Build output in history inflates the repository, produces meaningless diffs, and — as here — creates the misleading impression that more has been implemented than has been. It also makes `git status` unreliable as a signal of real change.

**Recommendation** Add `target/` and `.github/modernize/` to `.gitignore` and remove them from tracking. This is an engineering change to tracked files rather than a documentation change, so this audit records it rather than performing it.

**Resolution Authority:** Engineering.

---

### F-15 — Referenced process infrastructure does not exist {#f-15}

**Severity:** S4 — Hygiene

**Observation** `[FACT]` `README.md` and `PROJECT_MANIFEST.md` both direct contributors to `CONTRIBUTING.md` (0 bytes) and "PR templates in `.github/`" (`.github/PULL_REQUEST_TEMPLATE.md` is 0 bytes; `.github/ISSUE_TEMPLATE/` and `.github/workflows/` are empty). `.gitmessage`, `.editorconfig`, and `.gitattributes` are all 0 bytes.

`PROJECT_MANIFEST.md:99` requires "PR with templates and review per repository policy" — a policy with no artifact.

**Recommendation** `CONTRIBUTING.md` and the PR template have been created from the process already described in `REVIEW_PROTOCOL.md` and the manifest, introducing no new policy. CI workflows are deliberately not created — CI design is an EAD-level concern.

**Resolution Authority:** Documentation (partly corrected); Engineering for CI.

---

### F-16 — The governance corpus is untracked in git {#f-16}

**Severity:** S2 — Structural

**Observation** `[FACT]` `git status` reports as **untracked**: `PROJECT_MANIFEST.md`, `docs/adr/ADR-0002-modular-monolith-foundational-architecture.md`, `docs/adr/ADR-001-career-intelligence-framework-foundations.md`, and the whole of `docs/eaf/`.

Both Frozen ADRs, the manifest, and the EAF exist only in the working tree. They are in no commit.

**Impact** The entire approved governance corpus is one `git clean` from destruction, is invisible to every collaborator, and has no history. A "Frozen" document with no commit has no integrity guarantee — nothing distinguishes the approved text from an edit made afterwards.

**Recommendation** Commit immediately, ahead of any other work in this repository. Consider signed commits or tags for documents entering the Frozen stage, so freezing has a verifiable meaning.

**Resolution Authority:** Engineering — immediate.

---

## Escalation Summary — What Founders Must Decide

Ordered by what unblocks the most downstream work.

| # | Decision Required | Blocks | Finding |
|---|---|---|---|
| 1 | **Which product is ProjectEcho?** Career Intelligence Platform or event delivery backbone. | PRD, EAD, EDF, all implementation | [F-01](#f-01) |
| 2 | **Commission the FGM.** Governance model, lifecycle, amendment process, conflict resolution. | Every future ADR, EAF ratification, ARBR formalisation | [F-02](#f-02) |
| 3 | **Commission the CIF.** Under ADR-001's nineteen frozen decisions. | EAF ratification, all business terminology | [F-03](#f-03) |
| 4 | **Record the ARBR-0001 outcome,** including FD-001 to FD-004. | ADR-0002 revision, module governance rules | [F-07](#f-07) |
| 5 | **Name the owner and date** for the migration-scope assessment ADR-0002 deferred. | Backend direction | [F-05](#f-05) |
| 6 | **Is Kafka in the frozen stack?** | Infrastructure, event design | [F-06](#f-06) |
| 7 | **Resolve the five EAF Founder Decisions** (tenancy, data classification, tenant isolation point, provenance scope, context map). | EAF ratification | [F-12](#f-12) |
| 8 | **Locate or re-issue Product Impact Report 001** (Founder Decisions A–E). | ADR-0002 traceability | [F-11](#f-11) |
| 9 | **Choose a licence.** | Public release | [F-13](#f-13) |

## Escalation Summary — New ADRs Required

| Proposed | Subject | Arising from |
|---|---|---|
| ADR-0003 | Product definition of record | [F-01](#f-01) |
| ADR-0004 | Identifier correction for the superseded microservice ADR | [F-04](#f-04) |
| ADR-0005 | Disposition of the existing backend scaffold under ADR-0002 | [F-05](#f-05) |
| ADR-0006 | Event transport: whether Kafka is in the frozen stack | [F-06](#f-06) |

Identifiers are proposals only. Issuing an identifier is a governance act under the FGM, which does not yet exist ([F-02](#f-02)).

## Repository Health Assessment

Scored against the dimensions in the engineering charter. `[INFERENCE]` — these are judgements, not measurements.

| Dimension | Score | Basis |
|---|---|---|
| Governance Maturity | **4/10** | Two excellent Frozen ADRs and a rigorous board review; but the governing model (FGM) they depend on does not exist, and the corpus is uncommitted. |
| Architecture Maturity | **6/10** | ADR-0002 and EAF Rev 2 are unusually rigorous and self-critical. Blocked at EAD by unratified EAF and unresolved conflicts. |
| Engineering Maturity | **2/10** | No CI, no deployable, no config, empty scripts, build output committed, version drift in POMs. |
| Documentation Coverage | **5/10** | Strong at ADR/EAF/standards layers after this pass; entirely absent at CIF, FGM, EAD, EDF, PRD, RAR. |
| Traceability | **3/10** | Excellent intent — Provenance and Traceable Computation are foundational laws — undermined by four broken citation targets (FGM, CIF, PIR-001, superseded ADR-0002). |
| Repository Consistency | **3/10** | Two products, two architectures, two build tools documented simultaneously. |
| AI Readability | **7/10** | Materially improved by this pass; `.ai` workspace previously contained eight mandatory-reading files that were 0 bytes. |
| Cross-reference Coverage | **6/10** | Every document created in this pass is fully linked; the pre-existing corpus links by name rather than by path. |

The pattern is consistent: **the thinking is well above the median for a repository at this stage, and the scaffolding around it is well below.** The approved ADRs are genuinely strong documents. What is missing is not rigour — it is the connective tissue that makes rigour durable.
