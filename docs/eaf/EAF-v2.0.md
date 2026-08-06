---
Document ID: EAF-0001
Title: Engineering Architecture Framework
Version: 2.0
Status: Approved
Classification: Architecture
Owner: Principal Architect
Authority Level: 3
Primary Audience: Engineers
Governed By: FGM-0001, CIF-0001
Review Cadence: Bi-Annually
Last Updated: 2026-08-04
---
# ENGINEERING ARCHITECTURE FRAMEWORK (EAF-v2.0)
**Project:** ProjectEcho
**Version:** 1.0 — Revision 2
**Status:** Draft for Architecture Review (not ratified)
**Document Type:** Engineering Constitution — technology-agnostic, permanent conceptual language
**Explicitly NOT:** An Engineering Architecture Document (EAD), an ADR, an API spec, a package layout, a library choice, or code.
**Authority Note:** The EAF defers all business logic to CIF-0001 and all governance to FGM-0001. Any domain definitions herein are structural placeholders mapping to CIF-0001.

---

## HOW TO READ THIS DOCUMENT

This EAF defines **what things are and what they may never do**, not how they are stored, transported, or coded. Every primitive below is written so that a Java service, a Python ML pipeline, and a product manager reading a spec can all use the same noun to mean the same thing. Where this document could not resolve a question without technology, product, or founder input, it says so explicitly rather than guessing — those points are collected in the Self-Review at the end.

This revision (Rev 2) restructures the primitive catalogue into named categories, splits Service into Application Service and Domain Service, adds nine new primitives requested by architecture review (Factory, Context Map, Use Case, Command, Query, Specification, Computation, Policy, and the Part 0 foundational layer), and completes Appendix A as a full relationship matrix. No existing primitive's definition, rationale, or constraint has been removed or weakened in this revision — only reorganized and, where explicitly noted, extended.

---

## PART 0 — ENGINEERING FOUNDATIONS

**Purpose of Part 0:** Establish the permanent architectural objectives that every primitive, every future EAD, and every implementation decision must be traceable back to. Part 0 sits above the Preamble's Laws: the Laws constrain *how* things behave; Part 0 states *why* the architecture exists at all and what it is optimizing for over the platform's lifetime.

### Engineering Goals

- Preserve the ability to change storage technology, LLM provider, or deployment model without rewriting domain logic.
- Preserve the ability to reason about any derived value's correctness years after it was produced.
- Preserve the ability to onboard a new engineer, in any language or stack, using this document alone as the shared vocabulary.
- Preserve the ability to scale specific Modules independently as usage grows, without a full-system rewrite.
- Preserve the ability to prove, on demand, that a business rule or AI-generated output was correctly and traceably arrived at.

### Engineering Constraints

- No primitive may be defined in terms of a specific technology, library, or vendor.
- No primitive may assume a specific deployment topology (monolith, microservices, serverless) — the EAF must remain valid under any of them.
- No primitive may assume a specific programming language's idioms (this document does not discuss Java, Spring Boot, PostgreSQL, or any other implementation technology; those belong exclusively in the EAD).
- Every primitive that produces a derived value must be compatible with the Law of Traceable Computation (Preamble).
- Every cross-boundary interaction must be expressible as one of: an API contract, a Domain Event, or an Adapter translation — no informal cross-boundary access is architecturally valid.

### Engineering Quality Attributes

These are the permanent, non-functional objectives that every downstream Engineering Architecture Document (EAD) must explicitly trace its decisions back to. An EAD that cannot state which Quality Attribute a given technology choice serves is not aligned with this Framework.

| Quality Attribute | What it Means at the EAF Level |
|---|---|
| **Reliability** | The system produces correct results consistently, and degrades predictably rather than silently under failure. |
| **Maintainability** | A primitive's internal implementation can change without requiring changes to its consumers, as long as its Purpose, Responsibilities, and Constraints are honored. |
| **Scalability** | Any primitive's implementation can be scaled independently of others, provided its stated Boundaries and Relationships are respected. |
| **Security** | Data is accessible only to the actors and Tenants authorized to access it, enforced structurally, not by convention. |
| **Performance** | Read-heavy and write-heavy paths can be optimized independently (this is the architectural justification for the Read Model primitive). |
| **Auditability** | Every state transition and every derived value can be traced to its origin, actor, and computation version (this is the architectural justification for Domain Events and Provenance Records). |
| **Observability** | The internal state and behavior of the system can be inspected without modifying it, at any point in its operation (this remains a stated goal with an open structural gap — see Self-Review). |
| **Testability** | Every primitive can be exercised in isolation from its dependencies (this is the architectural justification for the Engine/Service split, and its further refinement into Application Service/Domain Service in this revision). |
| **Extensibility** | New capabilities can be added as new primitives, or new instances of existing primitives, without modifying the definitions of unrelated primitives. |

### Engineering Invariants

These are conditions that must hold true at every point in the system's operation, regardless of which Module, Bounded Context, or implementation is involved:

1. No Aggregate's invariants may be violated at any observable point, even momentarily.
2. No derived value may exist without an attributable computation version.
3. No cross-Module communication may occur outside a named contract (API, Domain Event, Adapter).
4. No Tenant's data may be reachable by another Tenant under any code path.
5. No LLM-generated content may be persisted without passing through the Validation Pipeline.

---

## PREAMBLE: ENGINEERING PHILOSOPHY

**Purpose:** Establish the non-negotiable laws that every primitive in this document, and every future primitive added to it, must obey.

**Definition:** A small set of invariant principles that constrain design at every layer, independent of technology.

**The Laws:**

1. **Law of Immutability** — State is never modified in place. Change is represented as a new fact (a Domain Event, a new Value Object, a new version of a computed value), not an in-place mutation of history.
2. **Law of Provenance** — Every derived insight (a Readiness score, a Recommendation, a Career DNA mutation) must be traceable to the specific inputs and computation version that produced it.
3. **Law of Traceable Computation** — Any function whose output can change behavior over time (scoring functions, rule sets, ranking algorithms) must be versioned, and every output must record which version produced it. This is the enforcement mechanism for Law 2; without it, provenance is a stated value with no structural guarantee.
4. **Law of Explicit Boundaries** — No component may reach across a stated boundary (Module, Aggregate, Bounded Context) informally. All cross-boundary communication happens through a named contract: an API, a Domain Event, or an explicitly designated Adapter.

**Non-goals of the Preamble:** Does not select a storage technology, event bus, or hosting model. Does not resolve *how* versioning is implemented — only that it must exist.

---

## PART I: STRUCTURAL PRIMITIVES

### 1. Entity

- **Purpose:** Represent a persistent, identifiable domain object with continuity over time.
- **Definition:** A stateful object with a unique identity (ID) that persists and is tracked across multiple interactions, independent of its attribute values at any point in time.
- **Responsibilities:** Hold identity; hold current lifecycle state; hold references to other Entities within the same consistency boundary.
- **Constraints:** Identity must never be reassigned or reused. An Entity may not contain orchestration logic — it is a state container, not an actor.
- **Relationships:** Entities are composed inside Aggregates, and are constructed exclusively via a Factory (§4) when construction requires invariant enforcement across multiple fields. Entities may reference other Entities only within the same Aggregate boundary directly; cross-Aggregate references must be by ID only, never by object reference.
- **Non-goals:** Does not define persistence mechanics, ORM mapping, or table structure.

### 2. Aggregate

- **Purpose:** Define a cluster of domain objects that must change together as a single transactional/consistency unit.
- **Definition:** A consistency boundary containing one or more Entities and Value Objects, with exactly one Aggregate Root as its sole entry point.
- **Responsibilities:** Enforce invariants that span more than one contained object; guarantee that no partial, inconsistent state is ever observable outside a completed transaction.
- **Constraints:** External code may reference an Aggregate only through its Root. The Root is the only object with an externally resolvable identity from outside the Aggregate. Cross-Aggregate consistency must be eventual (via Domain Events), never enforced by a single transaction spanning two Aggregates.
- **Relationships:** Repositories operate at Aggregate granularity only. An Aggregate lives inside exactly one Module. An Aggregate is never constructed directly by a consumer — see Factory (§4).
- **Non-goals:** Does not define transaction isolation level or database engine behavior.

### 3. Value Object

- **Purpose:** Represent immutable, descriptive characteristics of the domain that carry no independent identity.
- **Definition:** An object whose equality is determined entirely by its attribute values, not by an ID or lifecycle.
- **Responsibilities:** Encapsulate related attributes and any validation intrinsic to their combination (e.g., a `SkillProficiency` enforcing its scale is within bounds).
- **Constraints:** Must never be assigned a database-level identity or a mutable lifecycle. Once constructed, a Value Object's attributes cannot change — a "change" produces a new Value Object instance.
- **Relationships:** Composed inside Entities and Aggregates. May be shared by reference across boundaries, since immutability makes sharing safe.
- **Non-goals:** Does not define serialization format.

### 4. Factory *(new in Rev 2)*

- **Purpose:** Create Aggregates and complex Entities while guaranteeing that all invariants hold at the moment the object first comes into existence.
- **Definition:** A component whose sole responsibility is object construction — assembling Value Objects, Entities, and initial state into a valid Aggregate, and rejecting construction attempts that would produce an invalid one.
- **Responsibilities:** Own the rules for what constitutes a validly-constructed Aggregate; encapsulate construction complexity so that no consumer needs to know the full set of invariants required to build one correctly.
- **Constraints:** External consumers (Application Services, Use Cases) must never construct an Aggregate directly via its Entity constructors — they must go through the Aggregate's Factory. This is the architectural reason Aggregate construction is excluded from Entity's Relationships as a direct action: without a Factory, invariant-enforcement logic tends to leak into whichever Service happens to construct the object first, and duplicates inconsistently across every other Service that also constructs one.
- **Relationships:** Produces Aggregates (§2) and, where construction complexity warrants it, standalone Entities (§1). Invoked by Application Services (§9) and Use Cases (§11), never by Domain Services (§10) or Engines (§8), which must receive already-constructed objects as input.
- **Non-goals:** Does not decide *when* an Aggregate should be created (that is a Use Case/Application Service decision) — only *how* to construct it validly once that decision has been made.

---

## PART II: STRATEGIC & COORDINATION PRIMITIVES

### 5. Bounded Context

- **Purpose:** Define a boundary within which a specific ubiquitous language and domain model apply consistently, and outside of which the same term may mean something else.
- **Definition:** A strategic (not technical) boundary. Two Bounded Contexts may use the identical term (e.g., "Skill") to mean structurally different things, and this is expected, not an error.
- **Responsibilities:** Own its internal model and language; declare, via a Context Map (§6), how it translates concepts when communicating with another Bounded Context.
- **Constraints:** No component may assume a term means the same thing in another Bounded Context without going through an explicit translation (see Anti-Corruption Layer, Part VI).
- **Relationships:** A Bounded Context contains one or more Modules (§7). Multiple Bounded Contexts may exist even inside what will later become a single deployable service — this primitive is conceptual, not a deployment unit.
- **Non-goals:** Does not mandate that a Bounded Context maps 1:1 to a microservice, a repository, or a deployment artifact. That is an EAD decision.

### 6. Context Map *(new in Rev 2 — expands the Bounded Context section per architecture review)*

- **Purpose:** Describe, explicitly and conceptually, the relationship between any two Bounded Contexts that must communicate.
- **Definition:** A named, documented relationship pattern between two Bounded Contexts, drawn from a fixed vocabulary so that the *nature* of coupling between contexts is always stated, never implicit.
- **Responsibilities:** For every pair of communicating Bounded Contexts, declare which of the following relationship patterns applies:
  - **Published Language** — the two contexts communicate via a shared, well-documented schema that neither owns unilaterally (e.g., a Domain Event schema both contexts have agreed to).
  - **Shared Kernel** — the two contexts deliberately share a small subset of model and code (see Shared Kernel, Part IX) and accept the coupling this creates.
  - **Anti-Corruption Layer** — one context protects itself from another's model by translating at the boundary (see Part VI); used when the upstream context's model should not be allowed to influence the downstream context's design.
  - **Customer/Supplier** — the upstream context's team plans work with the downstream context's needs in mind; the downstream context has some influence but not veto power over the upstream context's roadmap.
  - **Partnership** — two contexts succeed or fail together and coordinate roadmap and integration jointly, with mutual veto power over breaking changes.
- **Constraints:** Every cross-Bounded-Context relationship must be assigned exactly one of the above patterns; "informal" or "undeclared" is not a valid state under the Law of Explicit Boundaries.
- **Relationships:** Documented per Bounded Context (§5) pair. Feeds directly into Appendix A. The Anti-Corruption Layer pattern named here is the conceptual justification for the Anti-Corruption Layer / Adapter primitive in Part VI.
- **Non-goals:** Does not specify the technical mechanism (shared library, event schema registry, generated client) used to implement any given relationship pattern.

### 7. Module

- **Purpose:** Provide the tactical, code-organizing container that implements part of a Bounded Context's model.
- **Definition:** A cohesive grouping of domain logic — Entities, Aggregates, Engines, Services, Repositories — that can evolve independently of other Modules.
- **Responsibilities:** Own its domain objects and Repositories; expose only well-defined interfaces (APIs, Domain Events) to the outside.
- **Constraints:** Dependencies flow inward — a Module may depend on the Shared Kernel, but never reach into another Module's internals. A Module belongs to exactly one Bounded Context.
- **Relationships:** Composed of Aggregates, Engines, Application Services, Domain Services, Repositories. Communicates with other Modules strictly via APIs or Domain Events.
- **Non-goals:** Does not define whether a Module is a package, a service, or a directory — that is an EAD/deployment decision. Migration note: a Module should remain structurally extractable into an independent service if scaling later demands it.

### 8. Process Manager / Saga

- **Purpose:** Own the coordination of a business process that spans multiple Modules and multiple Domain Events over time, without collapsing those Modules' boundaries.
- **Definition:** A stateful coordinator that listens for Domain Events, tracks the progress of a long-running process, and issues commands to Services in response, including compensating actions on failure.
- **Responsibilities:** Own the Mission lifecycle's cross-Module coordination (Draft → Assigned → In Progress → Submitted → Validated → Completed spans Mission, Evidence, and Career DNA Modules); ensure a partial failure mid-process results in a defined compensating action, not silent inconsistency.
- **Constraints:** Must not contain domain business rules itself — those remain in the Rule Engine and the owning Module's Domain Services. A Process Manager coordinates; it does not decide. Its outgoing instructions to Services are issued as Commands (§14).
- **Relationships:** Subscribes to Domain Events across Modules; issues Commands (§14) back into Module Application Services. Its own state transitions are themselves subject to the Law of Immutability (recorded as events, not overwritten).
- **Non-goals:** Does not define a workflow-engine product or orchestration technology.

---

## PART III: BEHAVIORAL PRIMITIVES

### 8a. Engine

- **Purpose:** Perform a stateless, algorithmic transformation of input to output with no side effects.
- **Definition:** A pure-logic component. Given the same input and the same declared version, it always produces the same output.
- **Responsibilities:** Execute deterministic or well-defined algorithmic computation (e.g., `RecommendationEngine`, `MissionEngine`, `ValidationEngine`, `RuleEngine`).
- **Constraints:** Must never perform I/O — no database calls, no network calls. Must receive fully hydrated input from an Application Service. Must declare a computation version per the Law of Traceable Computation. Where the output is specifically a versioned deterministic calculation (e.g., Readiness), the Engine's output should be understood as an instance of a Computation (§16) — Engine is the *component that performs* the work, Computation is the *versioned artifact it produces*.
- **Relationships:** Invoked by Application Services. Consumes Value Objects and Signals as input; produces Value Objects and Computations as output.
- **Non-goals:** Does not own data, does not decide when it is invoked, does not know about Repositories.

### 9. Application Service *(new in Rev 2 — split from the original "Service" primitive per architecture review)*

- **Purpose:** Orchestrate the flow of data between Repositories, Engines, Domain Services, and external systems, and manage the transactional/consistency envelope of a use case.
- **Definition:** A stateful (in terms of its dependencies — DB connections, caches, gateways) facade that coordinates a domain operation from start to finish, driven by an incoming Command or Query.
- **Responsibilities:** Fetch data via Repositories; hydrate input for Engines and Domain Services; invoke a Factory when new Aggregates must be created; persist results via Repositories; publish resulting Domain Events; manage cross-cutting orchestration concerns (transactions, retries at the orchestration level, Authorization Policy checks).
- **Constraints:** Must not contain business algorithms (those belong in an Engine) and must not contain pure domain behavior that operates without infrastructure dependencies (that belongs in a Domain Service, §10). An Application Service is orchestration only — if a piece of logic would behave identically with all infrastructure dependencies removed, it does not belong here.
- **Relationships:** Consumed by Use Cases (§11) and Controllers/API handlers (outside EAF scope). Coordinates Domain Services, Engines, Factories, and Repositories. This primitive carries forward the original "Service" primitive's orchestration responsibilities and constraints in full; nothing about the original orchestration role has been removed, only clarified against its new sibling, Domain Service.
- **Non-goals:** Does not define API contracts, request/response shapes, or transport protocol.

### 10. Domain Service *(new in Rev 2 — split from the original "Service" primitive per architecture review)*

- **Purpose:** Express domain behavior and business logic that does not naturally belong to a single Entity, Aggregate, or Value Object, but that is not purely algorithmic in the way an Engine is.
- **Definition:** A stateless component operating purely on domain objects already provided to it — no infrastructure dependencies, no I/O, no orchestration — but expressing meaningful *domain* concepts (e.g., "can this Mission be reassigned given these two Aggregates' current states") rather than mathematical computation.
- **Responsibilities:** Hold cross-Aggregate domain logic within a single Module that would otherwise have no natural home; keep such logic out of Application Services (which must remain orchestration-only) and out of Entities (which must remain state containers).
- **Constraints:** Must not perform I/O of any kind — this is the constraint it shares with Engine. Distinguished from Engine specifically in *kind* of logic: a Domain Service expresses domain policy and relationships between domain objects; an Engine expresses algorithmic/mathematical transformation. A Domain Service is distinguished from an Application Service specifically by statelessness with respect to infrastructure: a Domain Service never holds a DB connection, cache, or gateway reference.
- **Relationships:** Invoked by Application Services, which supply it with already-hydrated domain objects. May invoke a Specification (§13) to evaluate a business predicate as part of its logic.
- **Non-goals:** Does not decide transaction boundaries, does not publish Domain Events directly (an Application Service does that after a Domain Service's logic completes), does not perform algorithmic computation of the kind an Engine performs.

### 11. Use Case *(new in Rev 2)*

- **Purpose:** Represent one complete business operation, corresponding to a single user or system goal, from initiation to result.
- **Definition:** The outermost behavioral primitive — the entry point that represents "what the system is being asked to do" in business terms (e.g., "Submit Evidence for a Mission," "Request a Recommendation").
- **Responsibilities:** Coordinate one or more Application Services to fulfill one user goal; maintain the overall application flow for that goal, including which steps happen in what order.
- **Constraints:** Contains no infrastructure logic itself (that belongs to the Application Services it coordinates), no business algorithms (that belongs to Engines and Domain Services), and no persistence logic (that belongs to Repositories, invoked only indirectly through Application Services). A Use Case owns exactly one user goal — a component coordinating multiple unrelated goals is not a single Use Case and should be split.
- **Relationships:** Sits above Application Service in the call chain: a Use Case invokes one or more Application Services to accomplish its goal. Distinguished from Application Service in scope — a Use Case represents the whole business operation as the caller understands it; an Application Service represents one bounded orchestration step within a Module. Distinguished from Engine in kind entirely — a Use Case coordinates, it never computes.
- **Non-goals:** Does not define presentation logic, request validation at the transport level, or API routing — those are EAD/transport-layer concerns.

### 12. Command *(new in Rev 2)*

- **Purpose:** Represent an explicit intention to change system state.
- **Definition:** An immutable, named instruction carrying the data required to perform one state-changing operation (e.g., `SubmitEvidence`, `AssignMission`), distinct from the Domain Event that results from successfully carrying it out.
- **Responsibilities:** Express *intent* before it becomes *fact*. A Command may be rejected; a Domain Event, once published, records something that has already, irrevocably, happened.
- **Constraints:** A Command is not itself persisted as domain history — only its resulting Domain Event(s) are, if it succeeds. A Command must never be used interchangeably with a Domain Event; conflating the two would violate the Law of Immutability's distinction between "requested" and "happened."
- **Relationships:** Issued to a Use Case or Application Service, which validates and executes it (invoking Domain Services, Engines, and Repositories as needed), and which — on success — publishes the corresponding Domain Event(s). A Process Manager issues Commands to Application Services as part of coordinating a multi-Module process.
- **Non-goals:** Does not define transport (HTTP request body, message queue payload) — that is an EAD decision.

### 13. Query *(new in Rev 2)*

- **Purpose:** Represent a read-only retrieval request that does not, and must not, change system state.
- **Definition:** A named request for data, answerable either from a Read Model (§18) or, where no projection exists yet, from a Repository directly.
- **Responsibilities:** Express read intent separately from write intent, enabling the read and write paths to be optimized independently (this is the architectural basis of CQRS within this Framework, and the direct justification for the Performance Quality Attribute in Part 0).
- **Constraints:** Handling a Query must never produce a Domain Event or otherwise mutate state — a Query handler that has a side effect is, by definition, misclassified and should be a Command instead.
- **Relationships:** Preferentially served by a Read Model (§18), which exists specifically to answer Queries efficiently without requiring a full Aggregate/Graph traversal. Where no suitable Read Model exists, may be served directly by a Repository (§17), at the cost of coupling the read path to the write-side structure.
- **Non-goals:** Does not define query language, filtering syntax, or pagination mechanics — those are EAD/API-contract concerns.

### 13a. Specification *(new in Rev 2)*

- **Purpose:** Encapsulate a reusable business predicate — a yes/no (or matching) question about a domain object — as a named, composable object rather than inline conditional logic.
- **Definition:** A component representing a single business rule expressed as a predicate over one or more domain objects (e.g., `IsEligibleForMission`, `MeetsMinimumEvidenceQuality`, `MatchesSkillRequirement`).
- **Responsibilities:** Make individual business predicates independently namable, testable, and composable (specifications can be combined — e.g., "eligible AND has evidence") without duplicating the underlying conditional logic across multiple call sites.
- **Constraints:** A Specification must be a pure predicate — no I/O, no side effects, evaluated purely against objects already provided to it. This directly supports the Testability Quality Attribute (Part 0), since each business rule becomes independently unit-testable.
- **Relationships:** Used by the Rule Engine (Part VI) as the building blocks from which composite eligibility/business-rule decisions are assembled. Also used directly by Domain Services when a single predicate check is needed without invoking the full Rule Engine. Categories of use include eligibility (Mission assignment), validation (Evidence acceptance), and matching (Recommendation relevance).
- **Non-goals:** Does not decide what happens as a result of the predicate's outcome — that decision belongs to the Rule Engine or the Domain Service consuming it.

### 16. Computation *(new in Rev 2)*

- **Purpose:** Represent a versioned, deterministic calculation as a first-class artifact, distinct from the component (Engine) that performs it.
- **Definition:** A named, versioned function from a defined set of inputs to a defined output type, whose every execution is reproducible given the same inputs and the same declared version — the structural instrument through which the Law of Traceable Computation is satisfied.
- **Responsibilities:** Give calculations like Readiness, ranking, matching, and scoring a shared conceptual shape: declared inputs, a declared version, a declared output, and an attached Provenance Record (§20) for every execution.
- **Constraints:** Every Computation must declare its version at the point of execution, and that version must be immutable once used to produce a persisted output — changing the calculation logic produces a new version, never a silent change to an existing one. A Computation, like an Engine, performs no I/O.
- **Relationships:** Performed by an Engine (§8a); the Engine is the executing component, the Computation is the versioned specification of what is being executed and the record of what was produced. Examples explicitly covered by this primitive: Readiness (§21), Recommendation ranking, and Evidence-to-Mission matching. Every Computation's output attaches a Provenance Record (§20).
- **Non-goals:** Does not define the mathematical or machine-learning model behind any specific calculation — that is a Research/CIF-informed decision, not an architectural one.

---

## PART IV: PERSISTENCE PRIMITIVES

### 17. Repository

- **Purpose:** Provide the illusion of an in-memory collection of Aggregates, hiding all storage mechanics.
- **Definition:** A mediator interface between the domain model and its underlying persistence mechanism.
- **Responsibilities:** Load and save Aggregates as whole, consistent units; expose semantically meaningful methods (e.g., `findActiveByUserId`), never raw field/column queries.
- **Constraints:** Operates at Aggregate granularity only — never exposes sub-Entity or sub-Value-Object queries independent of their owning Aggregate.
- **Relationships:** Owned by exactly one Module. May have multiple technology-specific implementations behind one interface (migration lever). May directly serve a Query (§13) where no Read Model exists yet.
- **Non-goals:** Does not define the query language, ORM, or database engine.

### 18. Graph

- **Purpose:** Model relationships between domain concepts where the connections themselves carry meaning.
- **Definition:** A set of Nodes connected by Edges, where traversal paths encode semantic relationships (e.g., "Skill X is a prerequisite of Skill Y").
- **Responsibilities:** Store nodes, edges, and their properties; support traversal queries; encapsulate graph-specific logic (path-finding, similarity).
- **Constraints:** A Graph must be reachable only through a Repository — direct traversal-engine access from an Application Service bypasses the persistence boundary. Caching a Graph or its traversal results is a distinct concern from the Signal primitive (Part V) and must not be modeled as a Signal.
- **Relationships:** Two named instances exist: the Knowledge Graph (static, shared domain facts — skills, roles, prerequisites) and the Career DNA Graph (per-user, personalized). Persisted via a Repository.
- **Non-goals:** Does not choose a graph database, storage format, or traversal algorithm implementation.

### 19. Career DNA

- **Purpose:** Represent a user's professional profile as a dynamic, evolving structure — the platform's core domain-specific asset.
- **Definition:** A composition of Graph Nodes (skills, missions, evidence), Value Objects (scores, proficiencies), and time-weighted Signals. It is a structured composite, never a flat document.
- **Responsibilities:** Reflect the current state of a user's demonstrated and claimed capability; provide the substrate Engines read to compute Readiness and Recommendations.
- **Constraints:** Must never be represented or persisted as an unstructured blob — its structural and relational semantics are load-bearing, not incidental. All mutation happens via Domain Events (e.g., `SkillAdded`, `MissionCompleted`), never direct field writes, per the Law of Immutability.
- **Relationships:** Created on user signup via a Factory. Read by Engines (Recommendation, Readiness). Referenced, not duplicated, by Mission and Evidence.
- **Non-goals:** Does not define the UI representation of a user's profile — that is a Product/CIF concern.

### 20a. Read Model / Projection

- **Purpose:** Provide a denormalized, query-optimized view of one or more Aggregates for read-heavy consumers, without coupling those consumers to the full write-side structure.
- **Definition:** A derived, disposable data shape built by subscribing to Domain Events and/or Signals, rebuildable from source of truth at any time.
- **Responsibilities:** Serve high-frequency reads (e.g., "current Readiness for user X," "Career DNA summary for display") without requiring a full Graph traversal per request. Serves as the preferred handler for Queries (§13).
- **Constraints:** A Read Model is never a source of truth and must never be written to directly by a Service performing a business operation — it is populated only by reacting to Domain Events. It must be rebuildable from the write side at any time; if it cannot be rebuilt, it has accidentally become a second source of truth, which is a violation of this primitive.
- **Relationships:** Subscribes to Domain Events published by Modules. Consumed by Application Services on behalf of Queries.
- **Non-goals:** Does not define the specific technology used to build or serve projections (materialized view, cache, search index — all are EAD decisions).

---

## PART V: EVENT & SIGNAL PRIMITIVES

### 14a. Domain Event

- **Purpose:** Record something significant that happened in the domain that other Modules may need to react to.
- **Definition:** An immutable, timestamped, versioned fact representing a completed state transition — the result of a successfully executed Command (§12).
- **Responsibilities:** Notify other Modules of changes; enable eventual consistency across Module boundaries; provide an audit trail as a structural byproduct.
- **Constraints:** Self-contained — must carry all data a consumer needs without requiring a callback to the producer. Must carry a schema version (per the Law of Traceable Computation) so consumers can evolve independently of producers. Must never be used to synchronously return a response within the same transaction that produced it. Must never be conflated with the Command that preceded it — a Command may fail; a Domain Event, by definition, records something that already succeeded.
- **Relationships:** Published by Application Services after successfully executing a Command and completing an Aggregate change. Consumed by Read Models, other Modules' Application Services, and Process Managers.
- **Non-goals:** Does not define the transport mechanism (queue, log, bus) — that is an EAD/technology decision.

### 15. Signal

- **Purpose:** Capture a raw, high-volume, timestamped observation for analytics or real-time relevance computation.
- **Definition:** A lightweight event with a source and a value — numerous, unfiltered, and not individually business-significant.
- **Responsibilities:** Feed Engines with behavioral input (e.g., page views, interaction frequency) that contributes to but does not by itself constitute a Domain Event.
- **Constraints:** Distinct in kind from a Domain Event — a Signal is not semantically rich or business-relevant on its own; it must not be used as a substitute for a Domain Event when a real state transition has occurred. A Signal is not a caching mechanism.
- **Relationships:** Consumed by Engines (e.g., Recommendation, Readiness) as one class of input among several, alongside Computation inputs.
- **Non-goals:** Does not define retention policy, sampling strategy, or the analytics pipeline technology.

---

## PART VI: AI & INFRASTRUCTURE PRIMITIVES

### 22. Validation Pipeline

- **Purpose:** Provide a sequential, fail-fast chain of validation steps that data must pass before persistence.
- **Definition:** A composite pattern where each step validates one aspect — structure, semantics, business rules, consistency — independently.
- **Responsibilities:** Enforce the rule that an LLM output shall never write directly to storage; enforce that Evidence and other user-submitted data are structurally and semantically sound before an Aggregate is mutated.
- **Constraints:** Must not mix validation logic with business logic or persistence logic — a failed validation step halts the pipeline and returns a reason, it does not silently coerce or fix data.
- **Relationships:** Sits between the AI Gateway/user input and the Application Service layer. Receives raw input; outputs a validated domain object or a rejection. May invoke Specifications (§13a) as individual validation steps.
- **Non-goals:** Does not define which specific validation framework or library performs the checks.

### 23. Rule Engine

- **Purpose:** Encode explicit, deterministic business rules independently of application code.
- **Definition:** A component that evaluates user context against a configurable rule set, composed from Specifications (§13a), to produce deterministic decisions.
- **Responsibilities:** Generate the decision component of Recommendations; evaluate Mission eligibility; enforce readiness/eligibility gates.
- **Constraints:** Decides; does not explain. Explanation generation belongs exclusively to the AI Gateway. Rules should be externalizable (configuration/DB-driven) so they can be updated without a redeploy.
- **Relationships:** Consumed by Application Services; supplies the deterministic half of a Recommendation, paired with the AI Gateway's explanation half. Composes Specifications into higher-level eligibility decisions. Its decision-making logic is distinct from Policy (Part VIII), which governs operational, not domain-eligibility, behavior.
- **Non-goals:** Does not select a rules-engine product or DSL.

### 24. AI Gateway

- **Purpose:** Provide a controlled abstraction layer mediating all interaction with Large Language Models.
- **Definition:** A facade handling prompt construction, model routing, structured-output parsing, caching, and cost tracking.
- **Responsibilities:** Own prompt templates; manage retries and fallbacks; enforce token budgets; generate the explanatory/personalized half of a Recommendation.
- **Constraints:** Makes no business decisions — it transforms data and enforces output schemas, it does not decide eligibility or readiness. All output must pass through the Validation Pipeline before touching storage. Retries must be idempotency-safe (Part IX) to avoid duplicate downstream effects.
- **Relationships:** Consumed by Application Services requiring LLM output. Paired with the Rule Engine to jointly produce Recommendations. Its retry behavior is governed by a Retry Policy (Part VIII).
- **Non-goals:** Does not select a specific LLM provider, model, or hosting arrangement.

### 25. Anti-Corruption Layer / Adapter

- **Purpose:** Prevent the internal domain model from being shaped by the data formats or semantics of external systems.
- **Definition:** A translation boundary that converts an external system's model (a GitHub API response, a credentialing standard's schema, an LLM's raw output) into the platform's own domain vocabulary, and vice versa. This is the implementing primitive for the Anti-Corruption Layer relationship pattern named in the Context Map (§6).
- **Responsibilities:** Own all outbound calls to third-party systems (evidence verification APIs, credential issuers); translate their responses into Value Objects or validated input for the Validation Pipeline.
- **Constraints:** No external system's data shape may appear unmediated inside a Module's domain logic. An Adapter is the only component permitted to know about an external system's contract.
- **Relationships:** Sits at the boundary of a Module or Bounded Context, adjacent to the Validation Pipeline for anything that will eventually be persisted. Referenced directly by the Context Map (§6) as one of the five relationship patterns between Bounded Contexts.
- **Non-goals:** Does not select which third-party APIs are integrated — that is a product/EAD decision.

---

## PART VII: DOMAIN OUTCOME PRIMITIVES

### 26. Recommendation

- **Purpose:** Represent a suggested action, skill, or path derived from a user's Career DNA and external knowledge.
- **Definition:** A structured output containing a suggestion, a confidence score, an LLM-generated rationale, and an evidence trail linking back to the inputs that produced it.
- **Responsibilities:** Give the user (or downstream consumer) enough structure to both act on and audit the suggestion.
- **Constraints:** Must never be returned without a traceable rationale. Must carry the Rule Engine's decision-version and the AI Gateway's explanation-generation identifier separately, since one is deterministic and the other is not.
- **Relationships:** Produced by the Rule Engine (the decision) and the AI Gateway (the explanation). Its ranking/matching component is a Computation (§16). Attaches to a Provenance Record (§20).
- **Non-goals:** Does not define the UI presentation of a recommendation.

### 27. Mission

- **Purpose:** Represent a structured, actionable challenge assigned to a user to facilitate career growth.
- **Definition:** An actionable unit of work with a defined success condition, required Skills, and Evidence requirements.
- **Responsibilities:** Track its own lifecycle state; declare readiness prerequisites; declare what Evidence will satisfy it.
- **Constraints:** Lifecycle is strictly ordered: Draft → Assigned → In Progress → Submitted → Validated → Completed. No state may be skipped or reversed except through an explicitly modeled compensating transition owned by the Process Manager (§8).
- **Relationships:** Linked to Career DNA (readiness check on assignment, via a Specification) and Evidence (validation on submission). Its cross-Module lifecycle is coordinated by a Process Manager. Constructed via a Factory to guarantee lifecycle-start invariants.
- **Non-goals:** Does not define Mission content authoring tools or UI.

### 28. Evidence

- **Purpose:** Represent a verifiable artifact submitted by a user to prove proficiency or Mission completion.
- **Definition:** A claim coupled with supporting data (e.g., a repository link, a PR, a certificate).
- **Responsibilities:** Carry enough structured data for the Validation Pipeline to assess it; retain a link to the Mission or Skill it supports.
- **Constraints:** Lifecycle is strictly Pending Validation → Validated → Rejected. Evidence content may include personally identifying or third-party data (e.g., a real GitHub identity) and must be handled as classified data — this remains a placeholder pending an explicit data-classification decision from Founders/Security (see Self-Review).
- **Relationships:** Validated by the Evidence Engine (part of the Validation Pipeline), using Specifications for individual acceptance predicates. Stored within the Career Passport (a CIF-owned concept — the EAF does not redefine it, only references it as a consumer boundary).
- **Non-goals:** Does not define file storage, upload mechanics, or third-party API integration for evidence verification (e.g., GitHub API calls) — those are Anti-Corruption Layer/Adapter concerns at the EAD level.

### 21. Readiness

- **Purpose:** Provide a computed metric indicating a user's preparedness for a specific career level or Mission.
- **Definition:** A probability score in the range 0.0–1.0, derived from comparing a user's Career DNA against the requirements of a target Graph node. Readiness is the canonical example of a Computation (§16).
- **Responsibilities:** Give Missions and Recommendations a consistent, comparable readiness signal.
- **Constraints:** Must never be treated or exposed as a boolean. Computation is `f(Skills Matched, Evidence Quality, Mission Completion History)`; per the Law of Traceable Computation, the specific version of `f` used must be recorded alongside every Readiness value produced, via its attached Provenance Record.
- **Relationships:** Computed by an Engine from Career DNA and Graph data, as an instance of Computation. Attaches to a Provenance Record.
- **Non-goals:** Does not define the specific weighting or machine-learning model behind `f` — that is a Research/CIF-informed decision, not an architectural one.

### 20. Provenance Record

- **Purpose:** Give every derived value (Readiness, Recommendation, and future Computations) a structural, queryable link back to its inputs and computation version.
- **Definition:** An immutable record attached to a derived value, capturing: the computation version used, the specific input facts (or references to them) consumed, and the timestamp of computation.
- **Responsibilities:** Make "why did the system say this" answerable without reverse-engineering logs; make historical values reproducible even after the underlying scoring function changes. This is the primary structural instrument satisfying the Auditability Quality Attribute (Part 0).
- **Constraints:** Must be created at the same time as the derived value it describes — never backfilled or reconstructed after the fact. Is itself immutable once created (Law of Immutability applies).
- **Relationships:** Attached to Readiness and Recommendation at minimum; every Computation (§16) should attach one by default, not by exception.
- **Non-goals:** Does not define a general-purpose logging or observability system (see Self-Review — observability remains an open structural gap at the EAF level).

---

## PART VIII: SECURITY PRIMITIVES

### 29. Visibility Scope (Access Boundaries)

- **Purpose:** Represent a logical isolation boundary for data and configuration governed by the User.
- **Definition:** An access delegation granted by the User to an Organization (Employer). Organizations do not own Passports; they merely operate within granted Visibility Scopes (per FD-006).
- **Responsibilities:** Provide the scope within which data access, configuration overrides, and rate limiting apply.
- **Constraints:** Isolation must be enforced at a named layer (Repository-level guard or row-level security). No business component may bypass the Visibility Scope.
- **Relationships:** Replaces traditional B2B multi-tenancy.
- **Non-goals:** Does not select a specific row-level security implementation.

### 30. Authorization Policy

- **Purpose:** Govern who may act on, or view, a given Aggregate or Read Model.
- **Definition:** A declarative rule set evaluated before an Application Service performs an operation, independent of the Rule Engine's business-eligibility logic.
- **Responsibilities:** Enforce Tenant isolation at the point of access; enforce user-to-user boundaries (e.g., can User A view User B's Career DNA).
- **Constraints:** Must be evaluated before, not after, an Application Service touches a Repository. Must not be conflated with the Rule Engine — Authorization Policy answers "may this actor do this," the Rule Engine answers "is this business outcome eligible." Authorization Policy is one named category of the general Policy primitive (§31).
- **Relationships:** Consulted by every Application Service prior to Repository access. Tenant and Career DNA ownership are the primary boundaries it enforces.
- **Non-goals:** Does not select an authorization framework (RBAC/ABAC engine, policy language) — that is an EAD decision.

### 31. Policy *(new in Rev 2)*

- **Purpose:** Represent configurable operational behavior that is not domain logic — the "how the system behaves operationally" counterpart to the Rule Engine's "what the business decides."
- **Definition:** A named, externally configurable rule governing a cross-cutting operational concern, evaluated by infrastructure-facing components rather than domain components.
- **Responsibilities:** Cover categories including:
  - **Retry Policy** — governs how AI Gateway and other retryable operations back off and retry.
  - **Retention Policy** — governs how long Signals, Domain Events, and Read Models are kept before archival or deletion.
  - **Authorization Policy** (§30) — a specific, named instance of Policy governing access.
  - **Validation Policy** — governs which validation steps in the Validation Pipeline are mandatory versus advisory for a given data category.
- **Constraints:** A Policy must never encode domain-eligibility business rules (that remains the Rule Engine's exclusive responsibility) — the distinguishing test is whether the rule would still make sense if the business itself had never existed (e.g., "retry three times" makes sense independent of ProjectEcho's business; "eligible for Mission X" does not).
- **Relationships:** Authorization Policy (§30) is defined as a specific case of this general primitive. Retry Policy governs AI Gateway (Part VI) and Idempotency Key (§32) interactions. Retention Policy governs Signal and Domain Event lifecycle (Part V).
- **Non-goals:** Does not select a policy configuration mechanism or storage format.

---

## PART IX: OPERATIONAL PRIMITIVES

### 32. Idempotency Key

- **Purpose:** Guarantee that a retried operation (a re-delivered Domain Event, a retried AI Gateway call, a resubmitted Mission Command) has effect exactly once.
- **Definition:** A unique key, generated at the point an operation (typically a Command) is first requested, carried through every retry of that operation, and checked by the receiving Application Service before applying an effect.
- **Responsibilities:** Prevent duplicate Mission completions, duplicate Evidence validations, and duplicate billable LLM calls under retry or event-redelivery conditions.
- **Constraints:** Every Application Service that consumes a Domain Event or issues a retryable external call (notably via the AI Gateway) must check an idempotency key before applying a side effect. This is a mandatory cross-cutting constraint, not an optional pattern, and its retry behavior is governed by a Retry Policy (§31).
- **Relationships:** Applies wherever Domain Events are consumed, wherever Commands are re-issued, and wherever the AI Gateway manages retries.
- **Non-goals:** Does not define the storage mechanism for tracking used keys (cache, DB table) — an EAD decision.

### 33. Feature Flag

- **Purpose:** Toggle capabilities at runtime without redeployment.
- **Definition:** A runtime-evaluated condition controlling branching logic.
- **Responsibilities:** Decouple deployment from release; enable staged rollout and A/B testing; provide graceful degradation.
- **Constraints:** Lifecycle is Draft → Preview → Production-Stable → Deprecated. Must be auditable — every flag's current state and history of changes must be attributable to an actor and a time. Must not be used to maintain permanently parallel, mutually exclusive code paths — a flag that never reaches Deprecated is a signal of unresolved technical debt, not a permanent architecture pattern.
- **Relationships:** Evaluated by Application Services; should never be evaluated inside an Engine or Domain Service (which must remain purely a function of explicit input, not of ambient runtime configuration).
- **Non-goals:** Does not select a feature-flag platform or storage mechanism.

### 34. Shared Kernel

- **Purpose:** Hold the minimal set of shared types, exceptions, and utilities usable across multiple Modules without creating coupling.
- **Definition:** A controlled, intentionally small dependency every Module may take. This is also one of the five named Context Map (§6) relationship patterns, applied at the Bounded Context level rather than the Module level.
- **Responsibilities:** Prevent duplication of truly universal, domain-neutral types (`EmailAddress`, `UserRole`, `PaginationRequest`).
- **Constraints:** Must contain only pure, domain-neutral types — no database annotations, no framework-specific code, and no Module-specific domain concepts (e.g., `Skill` does not belong here).
- **Relationships:** Depended on by all Modules; depends on nothing else in the domain layer.
- **Non-goals:** Does not become a dumping ground for anything two Modules happen to both need — growth of this primitive should be treated as a standing architectural risk, not a convenience.

---

## APPENDICES

### Appendix A: Primitive Relationship Matrix

This replaces the Rev 1 placeholder. Tables are used in place of ASCII diagrams per architecture review direction.

**Table A.1 — Structural Composition (what contains what)**

| Primitive | Composed Of / Constructed By | Lives Inside |
|---|---|---|
| Aggregate | Entities, Value Objects; constructed by Factory | Module |
| Entity | Value Objects; constructed by Factory | Aggregate |
| Value Object | Primitive attributes | Entity, Aggregate |
| Career DNA | Graph Nodes, Value Objects, Signals; constructed by Factory | Module (Career DNA) |
| Module | Aggregates, Application Services, Domain Services, Engines, Repositories | Bounded Context |
| Bounded Context | One or more Modules | Platform |

**Table A.2 — Behavioral Call Chain (who invokes what)**

| Caller | Invokes | Constraint |
|---|---|---|
| Use Case | Application Service(s) | One Use Case may invoke multiple Application Services to fulfill one user goal |
| Application Service | Factory, Domain Service, Engine, Repository, Anti-Corruption Layer | Application Service never contains algorithms or pure domain logic itself |
| Domain Service | Specification | Domain Service performs no I/O |
| Engine | (none — receives fully hydrated input) | Engine performs no I/O |
| Rule Engine | Specification(s) | Composes Specifications into eligibility decisions |
| Process Manager | Application Service (via Command) | Process Manager never contains business rules |

**Table A.3 — Read/Write Path (CQRS)**

| Path | Primary Primitives | Notes |
|---|---|---|
| Write | Command → Application Service → Factory/Domain Service/Engine → Repository → Aggregate → Domain Event | Every successful write terminates in one or more Domain Events |
| Read | Query → Application Service → Read Model (preferred) or Repository (fallback) | Read Model is populated asynchronously from Domain Events |

**Table A.4 — Computation and Provenance Chain**

| Step | Primitive |
|---|---|
| 1 | Application Service hydrates input from Career DNA and Graph |
| 2 | Engine executes a Computation (e.g., Readiness) at a declared version |
| 3 | Computation output is wrapped with a Provenance Record |
| 4 | Rule Engine (using Specifications) produces a deterministic decision |
| 5 | AI Gateway produces an explanatory rationale |
| 6 | Decision + rationale + Provenance Record are assembled into a Recommendation |

**Table A.5 — Event and Coordination Chain**

| Step | Primitive |
|---|---|
| 1 | Command issued to Application Service |
| 2 | Application Service executes, mutating an Aggregate via a Repository |
| 3 | Application Service publishes a Domain Event |
| 4 | Read Model subscribes to Domain Event, updates its projection |
| 5 | Process Manager subscribes to Domain Event, advances a cross-Module process (e.g., Mission lifecycle) |
| 6 | Process Manager issues a new Command to the next Application Service in the process |

**Table A.6 — Security and Operational Overlay**

| Primitive | Applies To | Enforcement Point |
|---|---|---|
| Tenant | Every tenant-scoped Aggregate | Not yet decided — see Self-Review |
| Authorization Policy | Every Application Service operation | Before Repository access |
| Idempotency Key | Every Domain Event consumption, every Command retry, every AI Gateway retry | At the receiving Application Service |
| Feature Flag | Application Service branching logic only | Never inside Engine or Domain Service |
| Policy (Retry/Retention/Validation) | AI Gateway, Signal/Event storage, Validation Pipeline respectively | Infrastructure layer, outside domain logic |

### Appendix B: Eventual Consistency vs. Strong Consistency (Decision Matrix)

*(Placeholder — unchanged from Rev 1.)* Deferred pending resolution of the Tenant enforcement decision (§29) and the Process Manager's compensating-action model (§8), both of which materially affect where eventual consistency is acceptable versus where a single transaction is required.

### Appendix C: Glossary of Terms

*(Placeholder — unchanged from Rev 1.)* Deferred until this revision's terminology is stable, so it is not maintained twice during active revision.

---

## SELF-REVIEW

### Remaining Ambiguities

- **Module vs. Bounded Context granularity.** Unresolved from Rev 1: this document does not resolve how many Bounded Contexts ProjectEcho will actually have, or where their boundaries fall (e.g., is "Career DNA" its own Bounded Context, or part of a larger one). That remains a design exercise for the EAD, not a definition exercise for the EAF.
- **Use Case vs. Application Service boundary in practice.** The conceptual distinction is stated clearly (one user goal vs. one bounded orchestration step), but in a simple CRUD-like operation the two may collapse to a 1:1 relationship. This document does not mandate that a Use Case always coordinate *multiple* Application Services — only that it is the one responsible for the whole goal even when that happens to require just one.
- **Domain Service vs. Specification boundary.** Both are stateless, I/O-free, pure domain constructs. The distinction drawn here — Specification is a single predicate, Domain Service is broader domain behavior that may use one or more Specifications — is a reasonable default but has not been stress-tested against a real Module design; expect refinement once the EAD is drafted.
- **Provenance Record scope.** Still unresolved from Rev 1: it is unambiguous that Readiness and Recommendation must attach one. It is not resolved whether every Domain Event also needs one, or whether the Domain Event's own immutability already satisfies provenance for simple state transitions.
- **Read Model rebuild trigger.** Still unspecified whether rebuilds are triggered on-demand, scheduled, or continuous — left open intentionally, since it is implementation-adjacent.

### Hidden Assumptions

- This document assumes ProjectEcho will eventually be multi-tenant, since the primitive was named in the original TOC and carried forward. If Founders intend single-tenant for the foreseeable future, §29/§30 should be marked deferred rather than active, to avoid over-building.
- This document assumes the CIF owns "Career Passport," "Skill," and other business-facing terms, and treats the EAF as a consumer of those definitions rather than a source. This has not been confirmed against ratified CIF text, since it was not available at drafting time.
- This document assumes LLM interactions are exclusively mediated through the AI Gateway with no exceptions. If a legitimate fallback path bypassing the Gateway is ever needed, it must be named explicitly as an exception rather than left as an implicit gap.
- The split of Service into Application Service and Domain Service assumes every existing reference to "Service" in prior product/engineering discussion meant orchestration (i.e., maps to Application Service). This has not been verified against any existing code or prior informal usage, since none was available at drafting time.
- The Computation primitive assumes Readiness, ranking, and matching are the only current examples requiring versioned deterministic calculation. Any future scoring mechanism should default to being modeled as a Computation rather than a one-off Engine output, to stay consistent with this assumption.

### Founder Decisions Still Required

1. Is ProjectEcho single-tenant or multi-tenant at MVP? (Blocks §29/§30 from moving out of draft status.)
2. What is the initial data-classification policy for Evidence containing third-party/PII data? (Blocks §28's constraint from being enforceable.)
3. Where is Tenant isolation enforced — Repository guard, row-level security, or schema separation? (Named as an explicit open item in §29; this is a security decision this document cannot make unilaterally.)
4. Does every derived value require a Provenance Record, or only Readiness and Recommendation?
5. Which Context Map (§6) relationship pattern applies between each pair of Bounded Contexts once those contexts are actually defined? (This cannot be answered until Founder/Architecture input defines the Bounded Contexts themselves.)

### Potential Governance Conflicts

- **This entire section remains provisional**, as in Rev 1, because ADR-0001, ADR-0002, ARBR-0001, the Framework Governance Model, and the ratified CIF text were not available at drafting time for either revision. Every primitive above — including the nine introduced in this revision (Factory, Context Map, Use Case, Command, Query, Specification, Computation, Policy, and the Application Service/Domain Service split) — should be re-checked against those documents before this EAF is ratified, specifically for:
  - Whether ADR-0001/0002 already made a decision this document treats as open (e.g., tenancy model, event transport, CQRS adoption).
  - Whether ARBR-0001 constrains anything about Module/Bounded Context boundaries, or about the Service split introduced in this revision, that would require reconciliation.
  - Whether the Framework Governance Model prescribes a different process for introducing new primitives than the process used here — this revision adds nine new/split primitives based on architecture review input; Governance sign-off on that addition process itself may be required before they are treated as ratified, consistent with the same open item carried forward from Rev 1.
  - Whether the CIF already defines "Career Passport," "Career DNA," "Skill," "Readiness," or "Mission" in terms that conflict with the technical definitions given here — this document continues to treat CIF as authoritative on business meaning and EAF as authoritative on structural behavior, but that division of authority has not been confirmed by Governance.
- **New in this revision:** the introduction of Part 0's Engineering Quality Attributes creates a new surface for potential conflict — if ADR-0001 or ADR-0002 already established a different or differently-named set of quality objectives, Part 0 will need reconciliation rather than addition.

**Recommendation:** Route this revision through Framework Governance review with ADR-0001, ADR-0002, ARBR-0001, and current CIF text attached before treating any section above as ratified.
