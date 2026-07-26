# ADR-0002

## Foundational System Architecture: Modular Monolith

**Status:** Approved — Frozen
**Date:** 2026-07-25
**Supersedes:** ADR-0002 (Microservice Architecture) — the prior record is not modified; this is a complete replacement
**Superseded By:** None
**Related ADRs:** ADR-001 (Career Intelligence Framework Foundations)
**Related Frameworks:** Career Intelligence Framework (CIF), Engineering Architecture Framework (EAF), Engineering Architecture Document (EAD, downstream), Framework Governance Model (FGM)
**Governed By:** Framework Governance Model — this ADR's lifecycle, amendment process, and conflict resolution follow FGM Parts III and IV

---

### Amendment Policy

This document is frozen upon approval, consistent with ADR-001's precedent (FGM Part I-A, Frozen stage). Any future change to a decision recorded here requires a new ADR that explicitly references and supersedes the relevant Decision ID, following the FGM's Decision Impact Assessment process (FGM Section 41).

---

## Decision 001 — Modular Monolith as the Foundational System Architecture

**Status:** Approved

**Decision:** The system is built as a single deployable Modular Monolith: one deployment unit, internally composed of independently-bounded Modules with explicit interfaces and strong boundaries, rather than a distributed system of independently deployed services.

**Context:** The system's earlier architectural direction (the now-superseded ADR-0002) specified a Microservice Architecture. The founders have determined this conflicts with several approved priorities: lowest operational complexity, highest developer velocity, and lowest recurring cost, evaluated against the company's current stage and team size. This decision replaces that direction entirely.

**Alternatives Considered:**
- *Distributed Microservices* — independently deployed, independently scaled services communicating over the network.
- *Service-Oriented Architecture (SOA)* — coarser-grained services than microservices, typically coordinated through an enterprise service bus or similar integration layer.
- *Shared Database Microservices* — independently deployed services that share a single database rather than owning their own storage.
- *Serverless-First Architecture* — function-based compute with managed, provider-specific infrastructure.

**Why Alternatives Were Rejected:**

Distributed Microservices impose operational overhead — service discovery, inter-service network reliability, distributed tracing, independent deployment pipelines, and distributed data consistency — that is justified by independent scaling and independent team ownership at a scale this system has not reached. At the current team size, this overhead directly opposes the founders' stated priorities of lowest operational complexity and highest developer velocity, and introduces recurring infrastructure cost with no corresponding present benefit.

Service-Oriented Architecture was rejected for substantially the same reasons as Microservices — it retains network-boundary complexity between components while adding integration-layer complexity on top, without offering meaningfully lower operational burden than either Microservices or a Modular Monolith.

Shared Database Microservices was rejected because it combines the worst of both worlds: the network and deployment complexity of a distributed system, without the data-ownership isolation that is typically the actual justification for choosing microservices in the first place. It offers no advantage over a Modular Monolith while introducing real cost.

Serverless-First Architecture was rejected because it conflicts directly with multiple frozen founder decisions: it is difficult to reconcile with a single-VPS deployment model, tends to bind the system to provider-specific services (contradicting the "No AWS-specific services" constraint), and complicates the Rule-Engine-owns-decisions / LLM-never-writes-to-storage pipeline by fragmenting a request across independently-invoked functions rather than a coherent in-process flow.

**Final Decision:** Modular Monolith, deployed as a single unit, internally composed of independently-bounded Modules.

**Consequences:**

*Positive:* Single deployment pipeline and single operational surface to monitor, secure, and reason about. In-process communication between Modules avoids network-boundary failure modes (partial failure, distributed transactions, eventual-consistency defaults) for the large majority of interactions. Lower recurring infrastructure cost, consistent with a single-VPS deployment. Higher developer velocity, since most changes do not require coordinating a deployment across multiple independently-versioned services.

*Negative:* All Modules currently share deployment lifecycle — a deploy of one Module's change deploys the whole system. Runtime resource contention between Modules is possible until and unless specific Modules are extracted. Horizontal scaling currently applies to the whole system rather than to a specific bottlenecked Module.

*Risks:* Without disciplined enforcement of Module boundaries (Decision 002), a Modular Monolith can degrade into an undifferentiated codebase where the benefits of modularity are lost while none of the benefits of true service independence are gained. This risk is mitigated by Decision 002 and Decision 006, not eliminated by Decision 001 alone.

*Trade-offs:* This decision explicitly trades theoretical future scalability for present operational simplicity and velocity, consistent with the founders' stated priority ordering. This is treated as the correct trade-off for the company's current stage, not as a permanent ceiling — see Decision 007 (Evolutionary Architecture).

**Founder Rationale:** Lowest operational complexity, highest developer velocity, and lowest recurring cost were explicitly prioritized over theoretical scalability at this stage of the company.

**Long-Term Implications:** This decision is expected to hold for the MVP and for a meaningful period beyond it. It is not expected to hold indefinitely without revisiting — see Review Trigger Conditions.

---

## Decision 002 — Strong Module Boundaries with Explicit Interfaces

**Status:** Approved

**Decision:** Every internal Module MUST expose an explicit interface as its only point of contact with other Modules. No Module may access another Module's internal domain objects, persistence layer, or internal logic directly.

**Context:** A Modular Monolith derives its benefits from modularity, not merely from being a single deployment unit. Without enforced boundaries, the codebase reduces to an undifferentiated monolith with none of the isolation properties that justify the "modular" qualifier.

**Alternatives Considered:**
- *No enforced boundaries* — Modules organized by convention/folder structure only, with no access restriction.
- *Boundaries enforced only by code review discipline*, with no structural enforcement.

**Why Alternatives Were Rejected:** Both alternatives rely entirely on continued team discipline to prevent boundary erosion, which is a known failure mode in monolithic codebases as they grow and as team composition changes over time. Structural enforcement is available at comparable cost to convention-only approaches and removes the dependency on sustained discipline.

**Final Decision:** Module boundaries are enforced structurally, not by convention alone. A Module's internals are accessible only through its declared interface.

**Consequences:**

*Positive:* Preserves the option of future extraction (Decision 007) by ensuring a Module's true dependency surface is always accurately known and small. Makes it possible to reason about one Module's correctness largely in isolation.

*Negative:* Requires upfront and ongoing discipline in interface design; poorly designed interfaces can become a persistent constraint if not revisited as understanding improves.

*Risks:* Interfaces designed too early, before domain understanding stabilizes, may require rework. This is a normal and expected cost of modular design, not a reason to defer boundary enforcement.

**Founder Rationale:** Directly required by the founders' explicit principle of "Explicit Interfaces between modules" and "Independent Internal Modules."

---

## Decision 003 — Business Logic Precedes and Is Independent of Infrastructure

**Status:** Approved

**Decision:** Business logic MUST be expressible and testable independent of any specific infrastructure choice (database technology, caching layer, deployment platform). Infrastructure is an implementation detail that business logic depends on through abstraction, never the reverse.

**Context:** The founders have specified a particular infrastructure stack (PostgreSQL, Redis, Docker Compose, single VPS) for present operational reasons. This ADR must establish a principle stable enough to outlast those specific choices without requiring architectural revision if any one of them changes.

**Alternatives Considered:**
- *Business logic written directly against specific infrastructure APIs*, optimizing for present development speed over future flexibility.

**Why Alternatives Were Rejected:** Coupling business logic directly to infrastructure would make the present infrastructure choices (Decision-relevant, but not architectural) load-bearing for the architecture itself — meaning a future infrastructure change (e.g., a caching layer swap, or a future database migration) would require business logic changes, not just infrastructure changes. This directly conflicts with the requirement that this ADR remain valid independent of implementation specifics.

**Final Decision:** Business logic depends only on abstractions (see Decision 005, Repository Abstraction); infrastructure fulfills those abstractions and can change without changing business logic.

**Consequences:**

*Positive:* Infrastructure choices documented as frozen founder decisions (PostgreSQL, Redis, etc.) remain implementation choices, not architectural ones — they can evolve through normal engineering decisions without requiring a new ADR, provided the abstractions this ADR establishes are respected.

*Negative:* Requires disciplined use of abstraction layers even when a direct infrastructure call would be faster to write in the moment.

**Founder Rationale:** Directly required by "Business Logic before Infrastructure" and "Infrastructure is an implementation detail."

---

## Decision 004 — Replaceability of Infrastructure

**Status:** Approved

**Decision:** Every infrastructure dependency (persistence, cache, messaging) MUST be accessed through an abstraction that would allow its underlying implementation to be replaced without requiring changes to the business logic that depends on it.

**Context:** Direct consequence of Decision 003. Independence of business logic from infrastructure is only real if replacement is actually possible, not merely theoretical.

**Alternatives Considered:**
- *Treat replaceability as an aspiration rather than a requirement*, allowing convenience-driven direct infrastructure coupling where it seems low-risk.

**Why Alternatives Were Rejected:** "Low-risk" direct coupling accumulates over time in exactly the way strong boundaries (Decision 002) are meant to prevent at the Module level; the same discipline must apply at the infrastructure boundary or the principle in Decision 003 is nominal rather than real.

**Final Decision:** Replaceability is a requirement, enforced through the same abstraction mechanism as Decision 005.

**Consequences:**

*Positive:* Directly supports the Migration Philosophy (Decision 007/008) and reduces long-term technology lock-in risk.

*Negative:* Marginal upfront cost of writing to an abstraction rather than directly to a concrete infrastructure API.

**Founder Rationale:** Directly required by "Replaceability of Infrastructure."

---

## Decision 005 — Repository Abstraction for Persistence

**Status:** Approved

**Decision:** All persistence access occurs through a Repository abstraction operating at the Aggregate level (per EAF Part I, Section 7), never through direct, ad hoc storage queries embedded in business logic.

**Context:** This ADR does not redefine EAF primitives (per governance constraint); it adopts the Repository primitive already defined in the EAF and establishes it as the required persistence-access pattern at the architectural level.

**Alternatives Considered:**
- *Direct data-access calls embedded in Modules or Engines*, without a Repository abstraction layer.

**Why Alternatives Were Rejected:** Direct data-access calls would violate Decision 003 and Decision 004 by coupling business logic directly to a specific persistence technology, and would violate the EAF's own stated anti-pattern for the Repository primitive (arbitrary field querying, bypassing semantic methods).

**Final Decision:** Repository abstraction is architecturally mandatory, not optional guidance.

**Consequences:**

*Positive:* Consistent, predictable persistence-access pattern across all Modules; supports Decision 004's replaceability requirement directly.

*Negative:* Requires Repository interfaces to be thoughtfully designed per Aggregate rather than allowing convenience queries.

**Founder Rationale:** Directly required by "Repository abstraction for future infrastructure evolution," and consistent with the EAF's existing Repository primitive definition (EAF Part I, Section 7), which this ADR references rather than redefines.

---

## Decision 006 — Event-Driven Internal Communication Where Appropriate

**Status:** Approved

**Decision:** Communication between Modules MAY occur through direct interface calls (Decision 002) for synchronous, request-response interactions, and SHOULD occur through internal Domain Events (per EAF Part IV, Section 10) for interactions representing a completed fact that other Modules may need to react to asynchronously.

**Context:** A Modular Monolith needs an internal communication pattern between Modules that preserves loose coupling without requiring network boundaries. The EAF already defines Domain Events for this purpose; this ADR establishes when each communication style applies at the architectural level.

**Alternatives Considered:**
- *Synchronous interface calls only*, with no internal eventing.
- *Event-driven communication as the default for all inter-Module communication*, including request-response interactions.

**Why Alternatives Were Rejected:** Synchronous-only communication would force every Module reacting to another Module's state change to be directly and explicitly called by the originating Module, coupling Modules more tightly than necessary and making future extraction (Decision 007) harder, since the originating Module would need to know about every downstream consumer. Event-driven-only communication was rejected because it would introduce unnecessary indirection and eventual-consistency complexity for genuinely synchronous, request-response interactions (e.g., a validation check that must return an answer before proceeding), which is complexity this ADR's operational-simplicity priority does not justify paying everywhere.

**Final Decision:** Synchronous calls for request-response interactions; Domain Events for fact-propagation between Modules that don't require an immediate response.

**Consequences:**

*Positive:* Keeps Modules loosely coupled for fact-propagation (supporting future extraction) while keeping genuinely synchronous flows simple and easy to reason about.

*Negative:* Requires engineers to correctly classify each inter-Module interaction as one pattern or the other; misclassification in either direction reintroduces the problems the rejected alternatives would have caused.

**Founder Rationale:** Directly required by "Event-driven internal communication where appropriate" — the qualifier "where appropriate" is treated as intentional and is reflected in this decision's conditional structure rather than an unconditional mandate.

---

## Decision 007 — Evolutionary Architecture: Designed for Extraction, Not Extracted Now

**Status:** Approved

**Decision:** The architecture MUST be designed so that any given Module could be extracted into an independently deployed service in the future without requiring changes to that Module's business logic. This capability is a long-term architectural property the system is built to support, not an MVP-stage objective, and no Module is extracted as part of this decision.

**Context:** The founders have explicitly distinguished between designing for future extractability and actually performing extraction. This ADR must establish the former as a binding architectural requirement while explicitly not mandating or scheduling the latter.

**Alternatives Considered:**
- *Design for present needs only*, without regard to future extraction, deferring any such consideration entirely until a future architectural decision.

**Why Alternatives Were Rejected:** Deferring all consideration of extractability would risk exactly the kind of boundary erosion Decision 002 is meant to prevent — by the time extraction is actually needed, tightly coupled Modules would require substantial rework rather than a comparatively mechanical extraction. Designing for extraction now, as a discipline, costs little beyond what Decisions 002–005 already require, since strong boundaries, explicit interfaces, and infrastructure abstraction are the same properties extraction would need.

**Final Decision:** Extraction-readiness is a standing architectural property, achieved as a consequence of Decisions 002–006 being followed, not as separate additional work. No extraction occurs now.

**Consequences:**

*Positive:* Preserves a credible, low-cost path to distributed scaling if and when it becomes necessary, without paying distributed-systems operational cost before it's needed.

*Negative:* Requires ongoing discipline (Decisions 002–006) to actually preserve this property; it is not a one-time design decision that guarantees itself.

*Long-Term Implications:* This decision is the architectural basis for treating the Modular Monolith as a stage, not a permanent ceiling — see Review Trigger Conditions.

**Founder Rationale:** Directly required by "Evolutionary Architecture (designed for extraction if future scale requires it)" and the explicit statement that this is a long-term capability, not an MVP objective.

---

## Decision 008 — Deployment Topology Independence of Business Logic

**Status:** Approved

**Decision:** Business logic MUST NOT encode assumptions about deployment topology (single process vs. distributed, single database vs. multiple, synchronous vs. asynchronous network calls) beyond what is expressed through the Module interfaces and Repository/infrastructure abstractions already established in Decisions 002–005.

**Context:** Directly extends Decision 007 — extraction-readiness requires that business logic hasn't silently assumed it's running in a single process (e.g., relying on shared in-memory state between Modules outside their declared interfaces).

**Alternatives Considered:**
- *Allow incidental in-process assumptions where convenient*, treating strict topology independence as aspirational.

**Why Alternatives Were Rejected:** Incidental in-process assumptions are precisely what makes monolith-to-service extraction expensive and risky in practice; allowing them as a matter of convenience would make Decision 007 nominal rather than real, echoing the same reasoning rejected in Decision 004.

**Final Decision:** Topology independence is required, not aspirational, and is enforced through the same boundary and abstraction mechanisms as Decisions 002 and 004–005.

**Consequences:**

*Positive:* Makes Decision 007's extraction-readiness property genuinely achievable rather than nominal.

*Negative:* Prohibits certain convenient shortcuts (e.g., shared in-memory caches accessed directly across Module boundaries) that would otherwise be tempting in a single-process deployment.

**Founder Rationale:** Directly required by "The architecture must allow future extraction of modules into microservices without requiring changes to business logic" and "Business logic should remain independent of deployment topology."

---

## Decision 009 — Rule Engine as Sole Decision Authority; AI as Personalization and Explanation Layer

**Status:** Approved

**Decision:** All business decisions (eligibility, readiness determinations, recommendation selection) are made exclusively by deterministic Rule Engine logic. AI/LLM components MAY personalize the presentation of a decision and MAY generate explanatory language, but MUST NOT originate, alter, or substitute for the decision itself.

**Context:** This is an architectural expression of a frozen founder Product Constraint (Rule Engine owns decisions; LLM personalizes and explains; AI is never the source of truth), and directly supports the CIF's Explainability Contract (referenced, not redefined, per governance constraint) and First Principle 7.1 (Evidence Outranks Assertion).

**Alternatives Considered:**
- *Allow the LLM to directly determine outcomes* in cases judged low-risk or ambiguous, with the Rule Engine as a fallback rather than sole authority.

**Why Alternatives Were Rejected:** Any carve-out allowing the LLM to originate a decision reintroduces non-determinism and non-traceability into a path this system's trust guarantees depend on being fully accountable to Evidence and Rules (CIF First Principle 7.1). A partial exception is also difficult to bound reliably in practice — "low-risk or ambiguous" is not a stable, enforceable boundary.

**Final Decision:** No exceptions. Rule Engine is the sole decision authority; AI is confined to personalization and explanation.

**Consequences:**

*Positive:* Preserves the CIF's Explainability Contract and trust guarantees at the architectural level, not only as a stated policy. Keeps AI component failures (hallucination, drift) from being able to corrupt a business decision, only its presentation.

*Negative:* Limits how "smart" or adaptive certain decisions can feel in the product, since the underlying decision logic is deterministic and rule-based rather than model-driven.

**Founder Rationale:** Directly required by the frozen Product Constraint: "Rule Engine owns decisions. LLM personalizes and explains decisions. AI is never the source of truth."

---

## Decision 010 — Mandatory AI Response Pipeline: Structured Output → Validation → Business Rules → Persistence

**Status:** Approved

**Decision:** Every AI-generated response MUST pass through, in order: structured JSON output enforcement, a Validation Layer, Business Rules evaluation, and only then Persistence. No AI output reaches persistent storage without traversing all four stages in sequence.

**Context:** Direct architectural expression of the frozen founder constraint that LLMs never write directly to persistent storage, and of the EAF's existing Validation Pipeline and AI Gateway primitives (referenced, not redefined).

**Alternatives Considered:**
- *Allow validated AI output to skip Business Rules evaluation* for cases where validation alone seems sufficient (e.g., purely explanatory text with no decision content).

**Why Alternatives Were Rejected:** Distinguishing "purely explanatory" from "decision-bearing" AI output at the pipeline level is not reliably enforceable — explanatory text can smuggle in decision-relevant claims (e.g., an explanation that implies a readiness judgment not actually produced by the Rule Engine). Requiring the full pipeline unconditionally is a simpler and more robust guarantee than attempting to classify output type as a basis for skipping stages.

**Final Decision:** The four-stage pipeline is mandatory and unconditional for all AI-generated output reaching persistence.

**Consequences:**

*Positive:* Makes "LLMs never write directly to storage" a structurally enforced guarantee rather than a convention, directly supporting CIF First Principle 7.3 and Ethical Constraint 8.1.

*Negative:* Adds latency and engineering overhead to every AI-involving flow, even ones that might have been safe to shortcut.

**Founder Rationale:** Directly required by the frozen Product Constraint describing this exact four-stage pipeline.

---

## Decision 011 — Feature Flags as First-Class Architectural Citizens

**Status:** Approved

**Decision:** Feature Flags (per EAF Part VII, Section 19) are a required mechanism for controlling the rollout of Module-level and cross-Module behavior, not an optional operational nicety layered on afterward.

**Context:** Founders have specified Feature Flags as first-class citizens. This ADR establishes their architectural standing rather than leaving them as a pure implementation detail belonging only to the EAD.

**Alternatives Considered:**
- *Treat Feature Flags as a purely operational/EAD-level concern* with no architectural standing in this ADR.

**Why Alternatives Were Rejected:** Given the founders' explicit "first-class citizens" framing, omitting Feature Flags from the architectural record would understate their intended role and could result in them being treated as an afterthought during Module design, rather than a capability every Module is expected to support from the outset.

**Final Decision:** Every Module SHOULD expose its significant behavioral branches as flag-controllable where reasonable, consistent with the EAF's existing Feature Flag lifecycle (Draft → Preview → Production-Stable → Deprecated).

**Consequences:**

*Positive:* Decouples deployment from release across the whole system, supporting safer rollout of change within a single-deployment-unit architecture where a full redeploy is otherwise the only lever available.

*Negative:* Requires Feature Flag hygiene (auditability, eventual removal) to avoid the anti-pattern the EAF already names — indefinite parallel code paths.

**Founder Rationale:** Directly required by "Feature Flags are first-class citizens."

---

## Governance Metadata

**Related Founder Decisions:** Architecture stack decisions (Java 21, Spring Boot 3, Spring AI, PostgreSQL, Redis, Docker Compose, OrbStack, single VPS, no Kubernetes/Microservices/Service Mesh/GraphQL/Terraform/Vault/AWS-specific services); Product Constraints (Feature Flags, LLM pipeline, Rule Engine authority); prior Product Impact Report 001 Founder Decisions A–E.

**Decision Impact (per FGM Section 41):**
- *Affected Frameworks:* EAF (must conform its primitive usage to this ADR's principles), EAD (to be written under this ADR's constraints), CIF (referenced only, not altered)
- *Affected Concepts:* Module, Engine, Service, Repository, Domain Event, Feature Flag, AI Gateway, Validation Pipeline, Rule Engine (all EAF primitives; none redefined, all constrained by this ADR's principles)
- *Affected Teams:* Engineering (primary), Product (must design features compatible with single-deployment-unit release cadence and Feature-Flag-gated rollout)
- *Affected APIs:* Not addressed at this architectural level; deferred to EAD
- *Affected Metrics:* Deployment frequency, operational cost, incident surface area (expected to improve under this architecture relative to the superseded microservice direction)
- *Migration Required:* Yes, from whatever was already built under the superseded ADR-0002, if anything — scope to be assessed separately, not defined by this ADR
- *Backward Compatibility:* Not applicable; no prior implementation is assumed complete under the superseded ADR-0002
- *ADR Required:* This document is that ADR

**Review Trigger Conditions:** This ADR should be revisited if any of the following occur: sustained evidence that a specific Module has become a genuine scaling bottleneck that the single-deployment-unit model cannot address through vertical scaling alone; team growth to a scale where independent deployability of Modules materially improves velocity rather than harming it; a founder decision to change the frozen technology stack in a way that conflicts with a decision recorded here; or discovery that a Module's boundaries (Decision 002) have eroded to the point that extraction-readiness (Decision 007) is no longer actually true and cannot be restored without disproportionate cost.

---

## Self-Review

**Remaining Ambiguity:**

- Decision 006 ("event-driven communication where appropriate") intentionally leaves the classification of synchronous-vs-event-driven interactions to engineering judgment rather than a strict rule. This is a deliberate choice, but it is genuinely ambiguous at the ADR level and will need either an EAD-level decision tree or accumulated precedent to apply consistently.
- This ADR does not define what "significant behavioral branch" means for Decision 011's Feature Flag guidance (SHOULD, not MUST) — left to EAD or team judgment.
- The Migration Required field is marked "yes, scope to be assessed separately" without specifying who assesses it or by when. This is a genuine gap.

**Hidden Assumptions:**

- This ADR assumes the prior, superseded ADR-0002 has not yet resulted in substantial production implementation. If meaningful Microservice-architecture code already exists, the "no backward compatibility" framing above understates real migration cost, and Decision Impact should be revisited with that information.
- This ADR assumes Product (per Founder Decision A, Product Impact Report 001) will treat "Tenant" as future-only and not request Tenant-aware business logic before Module boundaries are established — if Product requests Tenant-related features before Decision 002's boundaries are mature, this ADR's extraction-readiness property (Decision 007) could be compromised by tenant-isolation logic bolted on without proper boundary design.
- This ADR assumes "single VPS deployment" and "Modular Monolith" remain compatible as data volume and traffic grow within the MVP-to-early-growth range; it does not establish at what point that assumption would need re-examination beyond the general Review Trigger Conditions.

**Founder Decisions Still Required:**

- Who owns and when to perform the migration-scope assessment referenced in Decision Impact (Migration Required)?
- Should the EAD be required to explicitly cite which Decision (001–011) justifies each major structural choice, to keep the ADR-to-implementation trace auditable? This wasn't specified and would materially help long-term traceability (FGM Section 15) but adds authoring overhead to the EAD.
- Is there a target team-size or traffic threshold the founders already have in mind for revisiting this ADR, or should the qualitative Review Trigger Conditions stated above be treated as sufficient for now?

**Conflicts with ADR-001, CIF, EAF, or the Approved Governance Model:**

- No conflicts identified with ADR-001 or the CIF. This ADR was deliberately written to reference CIF concepts (Career DNA, Readiness, Signals, Evidence, Passport, Explainability Contract, Trust Model, Layered Intelligence Pipeline) without redefining any of them, per the governance constraint given.
- This ADR does not resolve the previously identified EAF/CIF conflicts from Product Impact Report 001 (Career DNA as data-owning structure; non-contextual Readiness; Engines consuming raw Signals directly). Those conflicts remain open and are outside this ADR's scope — this ADR governs deployment topology and module structure, not the EAF's data-model primitive definitions. Flagging explicitly so this ADR is not mistaken for having resolved them.
- No conflict with the FGM as approved; this ADR's Governance Metadata section is structured to satisfy FGM Section 41's Decision Impact Assessment fields directly.

---

*This ADR is frozen upon approval. Any future change to a decision recorded here requires a new ADR that explicitly references and supersedes the relevant Decision ID above.*
