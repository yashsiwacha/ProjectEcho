---
Document ID: ADR-001
Title: Adr 001 Career Intelligence Framework Foundations
Version: 1.0
Status: Frozen
Classification: Architecture
Owner: Principal Architect
Authority Level: 4
Primary Audience: Engineers
Governed By: CIF-0001
Review Cadence: N/A
Last Updated: 2026-08-04
Next Review: N/A
---
# ADR-001

## Career Intelligence Framework Foundations

> [!WARNING]
> **DEPRECATED: SUPERSEDED BY CIF-0001**
> All business definitions, vocabulary, and domain rules defined in this historical ADR are explicitly superseded by the ratified `CIF-0001` Career Intelligence Framework. This document is retained for historical provenance only. It holds no authority over the domain model.



**Status:** Approved — Frozen
**Date:** 2026-07-24
**Supersedes:** None (foundational record)
**Amendment policy:** This document is not edited after freeze. Any change to a decision recorded here requires a new ADR that explicitly supersedes the relevant Decision ID.

---

### How to read this document

Each entry records one architectural decision in isolation: what was decided, what it was chosen over, and why. Entries are ordered roughly in the sequence they were decided, since later decisions frequently depend on earlier ones. Cross-references are noted where a decision constrains or is constrained by another.

This document does not describe how to implement any model. It describes why the model boundaries are where they are. Implementation lives in the Career Intelligence Framework (CIF); this document exists so the CIF's boundaries are never re-litigated from scratch.

---

## Decision 001 — Framework Naming: Career Intelligence Framework (not Specification)

**Status:** Approved

**Decision:** The foundational document governing the platform's intelligence models is named the Career Intelligence Framework (CIF), not the Career Intelligence Specification (CIS).

**Context:** The document was initially proposed as a "Specification." As scope was defined, it became clear the document would cover models, engines, relationships, first principles, and operating rules — not a single implementable spec for one system.

**Alternatives Considered:**
- *Career Intelligence Specification* — implies a narrow, single-system technical spec.
- *Career Intelligence Architecture* — considered, but under-communicates that this document also encodes product philosophy and rules, not just structure.

**Why Alternatives Were Rejected:** "Specification" undersells the scope and invites future teams to treat it as optional technical documentation rather than the conceptual foundation the whole product is built on. "Architecture" alone omits the rules and principles content that make this document normative, not just descriptive.

**Final Decision:** Career Intelligence Framework.

**Consequences:** All future references, onboarding materials, and engineering documentation should refer to "the CIF" as the canonical source of intelligence-model truth, distinct from the PRD (which describes product behavior) and from individual engineering specs (which describe implementation).

**Future Revisit Conditions:** None anticipated. Naming is low-risk to revisit later if needed, but should not be changed casually once teams reference "the CIF" externally.

---

## Decision 002 — Five-Dimensional Career Intelligence Model

**Status:** Approved

**Decision:** The Career Intelligence Model consists of exactly five dimensions: Identity, Intent, Capability, Confidence, Readiness.

**Context:** An earlier draft included a sixth dimension, Behavior, alongside the other five. This was reviewed and found to conflate two different kinds of thing: stable, verifiable representations of the user (the five dimensions) versus raw activity data used as an input signal.

**Alternatives Considered:**
- *Six dimensions including Behavior* — the original proposal.
- *Behavior as a sub-attribute of Capability* — considered briefly, rejected for the same reason as inclusion generally (see Decision 003).

**Why Alternatives Were Rejected:** Including Behavior as a peer dimension would have made "engagement" (logins, streaks, activity frequency) architecturally equivalent to "verified skill" — directly contradicting the founding principle that watching content and platform activity do not constitute progress. It also made the model's five other dimensions internally inconsistent, since Identity/Intent/Capability/Confidence/Readiness are all things the system *believes about the user*, while Behavior is a *category of input*, not a belief.

**Final Decision:** Five dimensions only. Behavior is demoted to a Signal Source (see Decision 003).

**Why this makes the company stronger:** It keeps the platform's core claim — "we measure competency, not consumption" — true at the data-model level, not just in marketing language. A platform whose own architecture can't tell the difference between "logged in a lot" and "demonstrably capable" would eventually leak that confusion into its scoring, and from there into what it tells employers.

**What engineering problems it prevents:** Prevents activity-volume signals from silently dominating skill representations through sheer data volume (continuous behavioral signals vastly outnumber episodic verified signals). Prevents future engineers from building features that write to "Behavior" as if it were a scored, storable dimension with the same status as Capability.

**What future features it enables:** Clean separation lets the platform later introduce more Behavior signal sources (e.g., time-of-day study patterns, pacing) without ever needing to ask "does this count as intelligence now?" — the answer is architecturally always no.

**Future Revisit Conditions:** If product research finds that behavioral patterns need to directly influence a user-facing score (not just calibration or timing), that would require a new dimension and a new ADR — not a quiet exception to this one.

---

## Decision 003 — Behavior as Signal Source, Not Intelligence Dimension

**Status:** Approved

**Decision:** Behavior (platform activity, learning habits, GitHub activity, coding frequency, interview behavior, external platform activity) is a category of Signal Source. It may influence Confidence calibration, Recommendation timing, and Coaching strategy. It never changes Capability, never creates Evidence, and never directly changes Readiness.

**Context:** Direct consequence of Decision 002. Once Behavior was removed as a dimension, its role needed explicit definition, since it clearly still matters to the product (e.g., knowing someone is actively engaged should affect coaching tone and timing).

**Alternatives Considered:**
- *Behavior with no defined influence at all* — architecturally clean but discards genuinely useful signal (e.g., pacing information relevant to coaching).
- *Behavior allowed to influence Capability under certain thresholds* (e.g., sustained high-frequency practice implies skill growth) — considered as a "soft evidence" concept.

**Why Alternatives Were Rejected:** Zero influence wastes real product value — behavioral cadence is legitimately useful for *when* and *how* to coach someone, just not *what* the system believes they can do. Allowing Behavior to influence Capability under thresholds was rejected because it reintroduces exactly the completion-economy failure mode the product was built to reject: frequency becoming a proxy for competency. There is no frequency threshold, however high, that constitutes evidence of ability without an actual verifiable output.

**Final Decision:** Behavior influences calibration, timing, and coaching strategy only. It is explicitly barred from Capability, Evidence, and Readiness.

**Why this makes the company stronger:** Preserves the "Evidence over Consumption" principle as an enforced architectural boundary, not a stated value that erodes under product pressure to show more responsive-feeling scores.

**What engineering problems it prevents:** Prevents a whole category of future bug/exploit where a user could inflate their measured competency purely through activity volume (e.g., bot-like repeated logins, artificially frequent commits) without producing anything verifiable.

**What future features it enables:** Enables rich behavior-driven coaching features (nudges, pacing advice, motivation timing) without any risk to the integrity of Capability or Passport data, since the two are structurally walled off.

**Future Revisit Conditions:** None anticipated; this is a core trust boundary and should require unusually strong justification to revisit.

---

## Decision 004 — Inferred vs. Verified Intelligence (Signal → Evidence promotion)

**Status:** Approved

**Decision:** All external input enters the system as a Signal and is Inferred by default. A Signal becomes Verified only by being promoted into Evidence through Evidence Verification Rules. Inferred and Verified are not two parallel pipelines — they are two states a Signal can be in.

**Context:** Early product philosophy called for a hard distinction between "fast, estimated" intelligence and "slow, evidence-backed" intelligence. The initial framing implied two separate systems; architectural review found this created ambiguity about where the boundary actually lived and risked duplicate data paths.

**Alternatives Considered:**
- *Two entirely separate pipelines* (Inferred Intelligence Model and Verified Intelligence Model as distinct top-level systems) — the original framing.
- *A single unified score with a confidence-weighted blend of inferred and verified input, with no hard state distinction* — considered as a simpler alternative.

**Why Alternatives Were Rejected:** Two separate pipelines would have duplicated Identity/Intent/Capability logic and created exactly the two-sources-of-truth risk this framework repeatedly guards against elsewhere (see Decision 011, Career DNA). A blended score with no hard state distinction was rejected because it would make it impossible to guarantee, at the Passport/Trust boundary, that only Verified claims are ever shown externally — the blend would already have mixed unverifiable claims into every downstream number.

**Final Decision:** Single Signal pipeline with a promotion boundary (Evidence Model) rather than two parallel systems.

**Why this makes the company stronger:** Makes "we never confuse inferred and verified intelligence" a structural guarantee rather than a discipline the team has to maintain by convention.

**What engineering problems it prevents:** Prevents data duplication between an "inferred" and "verified" copy of the same user's Capability, and prevents accidental leakage of unverified claims into externally-facing systems by construction rather than by manual filtering at each output point.

**What future features it enables:** Any future Signal Source (a new integration, a new assessment type) automatically fits the existing promotion pipeline without requiring new architecture — it only needs its own Evidence Verification Rule.

**Future Revisit Conditions:** If a future product need requires showing *degrees* of verification (not just binary Verified/Inferred) — e.g., "self-reported," "platform-observed," "third-party-verified" — this decision should be revisited to introduce a verification-tier concept rather than a binary state.

---

## Decision 005 — Career DNA as Aggregation Interface, Not Data-Owning Model

**Status:** Approved

**Decision:** Career DNA is a first-class architectural model whose sole responsibility is to compose a consistent, on-demand read view over Identity, Intent, Capability, Confidence, Uncertainty, Readiness, and Progress trajectory. It computes nothing and stores nothing that another model already owns.

**Context:** Founder request to introduce "Career DNA" as the personalized intelligence profile representing the user's evolving professional identity, to be consumed by Career Coach, Recommendation Engine, and Progress Engine, with Career Passport as its external representation.

**Alternatives Considered:**
- *Career DNA as a conceptual abstraction only*, with no architectural definition — each consumer assembles its own view of "the profile."
- *Career DNA as a first-class model that stores its own copies* of Capability/Confidence/Readiness/Evidence/Growth data.

**Why Alternatives Were Rejected:** A conceptual-only abstraction would let Recommendation, Coach, Progress, and Passport each independently define "the user's profile," producing four slightly different joins across the same underlying data that would drift out of sync over time, and four separate places needing to independently implement the Explainability Contract (Decision 013). A data-owning Career DNA would duplicate facts already owned by Capability, Confidence, Readiness, and Evidence models — creating the exact two-sources-of-truth risk this framework was designed to avoid at every other layer.

**Final Decision:** Career DNA is a first-class aggregation interface: one canonical place to read "the whole profile," backed by zero independently stored state.

**Why this makes the company stronger:** Every consumer-facing surface (Coach conversation, Recommendation input, Passport export) reads from the same assembled view, so the platform can never show a user two different versions of "who they are" depending on which feature they're looking at.

**What engineering problems it prevents:** Prevents drift between four independently-built "profile" joins; prevents a future bug class where fixing a Capability calculation doesn't propagate to Passport because Passport was reading a stale duplicated copy.

**What future features it enables:** New consumer surfaces (e.g., a future mobile widget, a future employer dashboard) can be built by reading Career DNA once, rather than re-deriving the profile assembly logic independently each time.

**Future Revisit Conditions:** If performance requirements ever demand caching or materializing part of Career DNA's output, that is an implementation/caching decision, not an architectural reversal — Career DNA remains the canonical assembly logic even if its output is cached downstream.

---

## Decision 006 — Career Knowledge Graph as a Separate Foundational Model

**Status:** Approved

**Decision:** The Career Knowledge Graph (CKG) — modeling relationships between Roles, Skills, Technologies, Projects, Assessments, Learning Resources, Companies, Interview Topics, Certifications, and Career Paths — is a separate foundational model from the Skill Graph, not a merged or absorbed structure.

**Context:** Founder question on whether career-ecosystem relationships (roles, resources, companies, paths) should live inside the existing Skill Graph or as a distinct model, motivated by the need for recommendation explainability and contextual reasoning, and by the fact that Exploration Mode (Decision 014) had no data model to reason over.

**Alternatives Considered:**
- *Merge into Skill Graph* — one graph covering both skill-to-skill propagation and the broader career ecosystem.
- *No dedicated graph* — treat these relationships as ad hoc lookups embedded in Recommendation logic.

**Why Alternatives Were Rejected:** Merging would combine a small, computationally auditable scoring structure (Skill Graph) with a large, frequently-edited, content-adjacent structure (roles, resources, companies) — meaning routine content edits (e.g., updating what a role requires) could silently perturb score-propagation math that feeds Confidence, Readiness, and ultimately the Passport. It would also violate the single-responsibility principle applied everywhere else in this framework. Ad hoc embedded lookups were rejected because Exploration Mode, Recommendation explainability, and Career Coach's contextual reasoning all need the same relationship data — building it three times inside three engines guarantees drift and makes explainability (Decision 013) impossible to guarantee structurally.

**Final Decision:** CKG is a separate foundational model. It stores relationships and thin entity references only, not full content (see Open Question G, still pending).

**Why this makes the company stronger:** Gives Exploration Mode — a core part of the user journey for the "Lost" arrival state — an actual data model to run on, where previously none existed.

**What engineering problems it prevents:** Prevents scoring infrastructure (Skill Graph) from being destabilized by routine content operations edits to role/resource/company relationships.

**What future features it enables:** Recommendation and Mission Selection explainability ("why this, why now") become directly answerable by tracing a CKG path, rather than requiring bespoke explanation logic per feature. New Roles, career paths, or content types can be added to CKG without touching Skill Graph's propagation math at all.

**Future Revisit Conditions:** If CKG's content volume grows large enough that "thin references only" becomes impractical to enforce, revisit the CKG/content-system boundary (see Open Question G).

---

## Decision 007 — Skill Graph as a Specialized Computational Model, Downstream of CKG

**Status:** Approved

**Decision:** Dependency order is Skill Taxonomy → Career Knowledge Graph → Skill Graph. Skill Graph is a specialized, auditable scoring-propagation structure that consumes a curated, versioned subset of CKG-derived relationships — not the live CKG graph directly.

**Context:** Initial architecture proposed Skill Graph as depending only on Skill Taxonomy, with CKG as a separate, parallel structure. Founder review proposed reordering so CKG sits between Taxonomy and Skill Graph, on the reasoning that most useful skill-to-skill propagation ("Node.js confidence should nudge Express.js confidence") comes from real-world co-occurrence patterns (roles, projects, companies) rather than pure taxonomic hierarchy.

**Alternatives Considered:**
- *Original ordering*: Skill Graph depends only on Taxonomy, CKG parallel and unrelated to Skill Graph.
- *Full founder proposal as stated*: Skill Graph reads live CKG edges directly.

**Why Alternatives Were Rejected:** The original ordering was reconsidered because pure taxonomic hierarchy produces a weak, tree-shaped propagation graph, while the genuinely useful propagation signal (skills that move together because they co-occur in real roles and projects) is CKG's domain, not taxonomy's — so the original ordering under-used CKG's value. The founder's proposal to read live CKG edges directly was accepted in direction but refined: since CKG is deliberately loosely-governed, frequently-edited, content-adjacent data, a live read would let a routine content edit silently perturb Confidence/Capability propagation math that ultimately feeds the Trust Model and Passport. Skill Graph, as scoring infrastructure, needs to remain stable and auditable.

**Final Decision:** Dependency order Taxonomy → CKG → Skill Graph is correct. Skill Graph consumes a curated, versioned, promoted subset of CKG relationships, not a live feed. (Governance mechanism for this promotion step is an open item — see Open Question M.)

**Why this makes the company stronger:** Produces genuinely useful, real-world-grounded skill propagation while keeping the platform's scoring math stable enough to be trustworthy and auditable — both properties the company's trust-moat strategy depends on.

**What engineering problems it prevents:** Prevents a class of bug where an unrelated content-team edit (e.g., adding a new resource to a role's requirements) unexpectedly changes a user's Confidence score in an unrelated skill area overnight.

**What future features it enables:** Allows CKG to grow rapidly and be edited by non-engineering teams (content ops, curriculum) without engineering needing to review every edit for scoring-safety — only the promotion step into Skill Graph needs that scrutiny.

**Future Revisit Conditions:** Requires the promotion/governance mechanism (Open Question M) to be resolved before Skill Graph's section can be fully specified. Not a reason to reopen the dependency order itself, which is settled.

---

## Decision 008 — Signal Model as the Universal Ingestion Boundary

**Status:** Approved

**Decision:** All external input, regardless of source or type, enters the system exclusively through the Signal Model, which normalizes it into a Signal with a source, timestamp, and type. No downstream model reads raw external data directly.

**Context:** Needed to formalize how Resume, GitHub, LinkedIn, Assessments, Interviews, and Behavior data (a heterogeneous set of input types) enter a single coherent system.

**Alternatives Considered:**
- *Per-source ingestion pipelines*, each feeding relevant downstream models directly (e.g., GitHub data feeds Capability directly, resume data feeds Identity directly).

**Why Alternatives Were Rejected:** Per-source pipelines would mean every new Signal Source requires its own bespoke integration into every downstream model it might affect, and would make it structurally impossible to apply uniform rules (Evidence promotion, Conflict Resolution, Temporal Decay) across all input types consistently. It also directly contradicts the layering principle established across the rest of the framework: raw external data should never be visible to Layer 2/3 models.

**Final Decision:** Single universal Signal Model as the only ingestion boundary.

**Why this makes the company stronger:** Every new integration (new assessment provider, new platform connector) is a well-defined, bounded engineering task — add a new Signal Source — rather than a cross-cutting change touching multiple downstream models.

**What engineering problems it prevents:** Prevents inconsistent handling of the same conceptual event (e.g., "user submitted a coding challenge") depending on which team built that particular integration.

**What future features it enables:** New third-party data sources (future partnerships, new assessment types) can be added by defining a new Signal Source and mapping rule, without redesigning any Layer 2 model.

**Future Revisit Conditions:** None anticipated.

---

## Decision 009 — Evidence Model as the Sole Promotion Path to Verified Status

**Status:** Approved

**Decision:** Evidence Model defines the rules by which a Signal is promoted to Verified status. This is the only mechanism by which a Signal becomes Evidence.

**Context:** Direct extension of Decision 004 — the Inferred/Verified distinction needed an explicit, single owner of the promotion rules.

**Alternatives Considered:**
- *Verification handled ad hoc per Signal Source* (e.g., GitHub has its own verification logic, Assessments have their own, with no shared model).

**Why Alternatives Were Rejected:** Ad hoc, per-source verification would mean the definition of "Verified" varies by feature team and drifts over time — directly undermining the Trust Model's ability to make a single, consistent guarantee about what "Verified" means anywhere in the product, including the Passport shown to employers.

**Final Decision:** Evidence Model is the single, shared owner of verification rules across all Signal Sources.

**Why this makes the company stronger:** "Verified" means the same thing everywhere in the product — in the Passport, in Career Coach's language, in Readiness calculations — which is foundational to the trust claim the whole company is built on.

**What engineering problems it prevents:** Prevents divergent, inconsistent verification standards emerging across features built by different teams at different times.

**What future features it enables:** New verification methods (e.g., a future live-proctored assessment, a future employer-side verification) can be added as new rules within one model, immediately consistent with every existing Verified claim.

**Future Revisit Conditions:** None anticipated.

---

## Decision 010 — Conflict Resolution Model as a Distinct Layer 1 Gate

**Status:** Approved

**Decision:** Conflict Resolution Model is a distinct, mandatory model sitting between Signal/Evidence and the Intelligence layer, responsible for deciding the system's internal belief when Signals or Evidence disagree about the same claim. It is explicitly separate from the Trust Model, which governs external disclosure.

**Context:** Contradictions between inferred and verified data are not an edge case — e.g., a resume claims a skill level with no supporting verified evidence, or two Signal Sources disagree. Initial drafts handled this inside Evidence Verification Rules as a sub-case; review found it needed to be a first-class gate.

**Alternatives Considered:**
- *Handle conflicts as a sub-rule inside Evidence Model.*
- *Handle conflicts inside Trust Model*, treating "what to believe" and "what to disclose" as one decision.

**Why Alternatives Were Rejected:** Burying conflict resolution inside Evidence Model would make it an afterthought rather than infrastructure every Layer 2 model depends on, and would make the rule harder to audit in isolation. Merging conflict resolution with Trust Model was explicitly rejected by founder decision (Decision 012): internal belief and external disclosure are different questions with different stakeholders and must remain architecturally separate, even though they are related.

**Final Decision:** Conflict Resolution Model is a standalone Layer 1 model, upstream of Identity/Intent/Capability, and strictly separate from Trust Model.

**Why this makes the company stronger:** Makes the platform's internal reasoning ("what do we actually believe, given contradictory input") independently auditable and testable, separate from and prior to any question of what gets shown to whom.

**What engineering problems it prevents:** Prevents contradictory Signals from silently and inconsistently resolving differently depending on which downstream model happens to encounter them first.

**What future features it enables:** Enables Career Coach's "invite the user to verify a contested claim" behavior (Decision 004/012) as a clean, well-defined trigger, since contested state is explicitly tracked rather than implicitly resolved and discarded.

**Future Revisit Conditions:** None anticipated.

---

## Decision 011 — Per-Skill-Node Confidence (No Global Aggregate Storage)

**Status:** Approved

**Decision:** Confidence is not a global value. It exists at the individual skill-node level, alongside Capability, Evidence, and Last Updated, for every skill node. Higher-level scores (Backend Readiness, Role Readiness, Career Readiness, etc.) are derived through rollups over the skill graph at query time. No aggregate confidence is stored directly.

**Context:** Early drafts left Confidence's granularity ambiguous — a single global score versus per-skill. Founder review determined this ambiguity would cascade into every downstream model (Uncertainty, Readiness).

**Alternatives Considered:**
- *A single global Confidence scalar per user*, for simplicity of display.
- *Both a stored per-skill value and a separately stored, independently maintained global rollup.*

**Why Alternatives Were Rejected:** A single global scalar would erase exactly the information that makes the platform useful — "confident in backend fundamentals, no evidence yet for system design" is a meaningfully different and more actionable state than a single blended number, and collapsing it would undermine both Recommendation targeting and Uncertainty measurement. Storing a separately maintained global rollup alongside per-skill values was rejected because it creates a second, independently-updatable source of truth that can drift out of sync with the per-skill data it's supposed to summarize — the same two-sources-of-truth risk addressed elsewhere in this document.

**Final Decision:** Confidence stored only at skill-node granularity. All higher-level scores are computed rollups, never separately stored state.

**Why this makes the company stronger:** Keeps every higher-level number (Role Readiness, Career Readiness) provably traceable back to specific skill-level evidence, which is exactly what "evidence-based" needs to mean architecturally, not just rhetorically.

**What engineering problems it prevents:** Prevents rollup scores silently going stale relative to the skill-level data they summarize, since rollups are computed on read rather than cached and independently updated.

**What future features it enables:** Enables any future contextual rollup (a new role definition, a new certification's requirement profile) to be computed immediately from existing skill-node data, without needing new storage or a data migration.

**Future Revisit Conditions:** If read-time rollup computation becomes a performance bottleneck at scale, caching may be introduced — but as a caching/invalidation strategy over the same underlying computation, not as a second source of truth.

---

## Decision 012 — Contextual Readiness (No Universal Readiness Score)

**Status:** Approved

**Decision:** Readiness is not a sixth intelligence dimension and not a single universal score. It is a contextual query, computed on demand against Capability, Confidence, and Uncertainty, parameterized by a target context (a role, an interview type, a specific skill, a company profile). Role Readiness, Interview Readiness, Skill Readiness, Career Readiness, and Company Readiness are the same computation run against different context parameters.

**Context:** Founder requirement that Readiness support multiple contextual forms (Role, Interview, Skill, Career, Company Readiness) without proliferating into five separate, redundant models.

**Alternatives Considered:**
- *Five separate Readiness models*, one per context type.
- *A single stored Readiness score per user, non-contextual.*

**Why Alternatives Were Rejected:** Five separate models would duplicate the same underlying computation five times, guaranteeing drift and violating the single-responsibility principle. A single non-contextual score was rejected because "ready" is meaningless without a target — someone can be ready for a junior backend role and not ready for a senior systems-design interview simultaneously; collapsing that into one number would actively mislead users.

**Final Decision:** One Readiness model, required to take a context parameter, queried on demand rather than stored.

**Why this makes the company stronger:** Lets the product truthfully answer "ready for what, specifically" rather than presenting a single potentially misleading confidence number — directly serving the "reduce uncertainty" North Star rather than replacing one kind of uncertainty with a falsely precise one.

**What engineering problems it prevents:** Prevents five parallel Readiness implementations from diverging in behavior and definition over time.

**What future features it enables:** New context types (e.g., a future "Freelance Readiness" or "Promotion Readiness") can be added by defining a new context parameter, not a new model.

**Future Revisit Conditions:** If product research shows users need one headline "how ready am I overall" number for motivational/UX purposes, that should be treated as a presentation-layer default context (e.g., "Career Readiness" as the default view), not a new stored aggregate.

---

## Decision 013 — Explainability as a Structural Output Contract

**Status:** Approved

**Decision:** Every Recommendation and Mission Selection output must be able to answer "Why this?", "Why now?", and "Expected impact?" from real upstream data — respectively sourced from Career Knowledge Graph relationships, Uncertainty Model state, and a projected Readiness/Progress delta. This is a required field on the model's output type, not hardcoded UI text.

**Context:** Founder decision that explainability must "emerge naturally from the framework rather than being hardcoded UI text," to ensure recommendations are genuinely traceable rather than plausibly-worded.

**Alternatives Considered:**
- *A separate "Explainability Model"* that generates explanations independently after the fact.
- *UI-layer-only explanation text*, written per feature without a structural data requirement.

**Why Alternatives Were Rejected:** A separate post-hoc explainability model would have no guarantee of actually reflecting the real reasoning behind a recommendation — it would be generating plausible-sounding justification rather than reporting true provenance, which is a trust risk for a company whose stated moat is trust. UI-layer-only text was rejected because it can't be validated or guaranteed consistent, and was explicitly what the founders wanted to avoid.

**Final Decision:** Explainability is a mandatory structural field on Recommendation and Mission Selection outputs, populated from real upstream model state, not authored separately.

**Why this makes the company stronger:** Recommendations become auditable and defensible — including to a skeptical user or, eventually, regulator — because the explanation is provably derived from the same data that produced the recommendation, not written to sound convincing after the fact.

**What engineering problems it prevents:** Prevents a recommendation from shipping with no real justification behind it (if the trace fields can't be populated from real data, the recommendation itself shouldn't be allowed to generate).

**What future features it enables:** Enables any future UI surface (chat, dashboard, notification) to render trustworthy "why" explanations without each surface needing its own explanation-generation logic.

**Future Revisit Conditions:** The mechanism for computing "Expected impact" requires a forward-projection capability not yet owned by any model (see Open Question N) — this must be resolved before the contract can be fully implemented, but does not change the decision itself.

---

## Decision 014 — Exploration Mode vs. Guided Mode

**Status:** Approved

**Decision:** The platform operates in two modes. Exploration Mode is used before Intent is known, and produces career suggestions, interest discovery, and initial recommendations. Guided Mode is activated once Intent is established, and governs all Recommendation, Mission, Coach, and Readiness logic by default.

**Context:** Onboarding must serve users in a "Lost" arrival state who cannot yet state an Intent, but most of the framework (Uncertainty, Readiness, targeted Recommendation) is only meaningful relative to a known Intent.

**Alternatives Considered:**
- *A single mode with a "default/generic" Intent* assumed for all users until they specify otherwise (e.g., defaulting to "general software engineering").
- *Blocking all recommendation functionality until Intent is explicitly captured.*

**Why Alternatives Were Rejected:** A default generic Intent risks silently misclassifying "Lost" users' arrival state and steering them down a path they never chose, undermining the "this platform understands me" onboarding goal. Blocking all functionality until Intent is captured would fail the <90-second time-to-first-clarity target and leave genuinely lost users with nothing useful to do.

**Final Decision:** Two explicit modes, with CKG-driven discovery logic specific to Exploration Mode, and the full pipeline (Uncertainty/Readiness/targeted Recommendation) reserved for Guided Mode.

**Why this makes the company stronger:** Serves the "Lost" arrival state honestly — with real discovery support — instead of forcing a premature, potentially wrong Intent assumption onto users who don't have one yet.

**What engineering problems it prevents:** Prevents Uncertainty and Readiness models from having to handle an undefined-Intent case internally, since Exploration Mode is a structurally distinct pathway rather than a degenerate case of Guided Mode.

**What future features it enables:** Enables Exploration Mode to evolve independently (e.g., richer interest-discovery mechanics) without touching Guided Mode's more constrained, evidence-driven logic.

**Future Revisit Conditions:** The precise transition trigger from Exploration to Guided Mode (how much confidence in inferred Intent is required before switching) is not yet defined and should be resolved during Intent Model's detailed design — not a reason to reopen the two-mode decision itself.

---

## Decision 015 — Evidence-Only Capability Growth

**Status:** Approved

**Decision:** Capability changes only as a result of Evidence (Verified Signals). No other input — including Behavior, raw Signals, or self-reported claims — can change a Capability value.

**Context:** Direct enforcement of the "Progress is earned through evidence" and "Watching content never creates progress" founding principles at the data-model level.

**Alternatives Considered:**
- *Allow high-confidence Inferred Signals to partially move Capability*, weighted lower than Evidence but still contributing.

**Why Alternatives Were Rejected:** Any path by which Inferred Signals move Capability — even weighted lower — reopens the door to exactly the gaming and false-signal risk the Verified/Inferred split exists to prevent (e.g., an inflated resume claim nudging a Capability score upward even slightly). A hard boundary is significantly more defensible and auditable than a soft-weighted one.

**Final Decision:** Capability is a pure function of Evidence. No exceptions.

**Why this makes the company stronger:** Makes "Capability" mean something specific and defensible — every point of Capability traces to a verifiable artifact, which is the entire basis of the Passport's value to employers.

**What engineering problems it prevents:** Prevents gradual erosion of Capability's meaning through well-intentioned "soft signal" features added later under product pressure to make the platform feel more responsive.

**What future features it enables:** Makes it possible to build employer-facing trust claims ("every skill shown here is backed by verified evidence") with total confidence, since it's structurally guaranteed rather than aspirational.

**Future Revisit Conditions:** None anticipated; this is a core trust guarantee.

---

## Decision 016 — Bidirectional Recommendation Loop (Active Uncertainty Reduction)

**Status:** Approved

**Decision:** Recommendation Model does not only consume intelligence to suggest next steps toward a stated goal — it can also actively select directions specifically intended to reduce measured Uncertainty, by targeting gaps identified in the Uncertainty Model at skill-node granularity.

**Context:** Founder requirement that the framework be explicitly bidirectional: the system should be able to decide what evidence to go collect next, not just react to evidence as it arrives.

**Alternatives Considered:**
- *Recommendation Model as a purely reactive consumer* of Capability/Confidence/Uncertainty/Readiness, with no mandate to target measurement gaps directly.

**Why Alternatives Were Rejected:** A purely reactive model can only ever optimize for the user's stated goal using whatever evidence happens to already exist — it can never notice and address "we actually don't know if this person can do X" as its own actionable problem, which is a core part of what "reducing uncertainty" (the platform's North Star metric) is supposed to mean.

**Final Decision:** Recommendation Model has two selection modes — goal-directed and uncertainty-directed — feeding Mission Selection with tasks engineered to produce evidence for a specific, currently-unmeasured skill node.

**Why this makes the company stronger:** Turns "reduce career uncertainty" from a passive measurement into an active system behavior — the platform doesn't just report what it doesn't know, it does something about it.

**What engineering problems it prevents:** Prevents Uncertainty Model from becoming a purely descriptive dashboard number with no mechanism connecting it back to user-facing action.

**What future features it enables:** Enables a future "fastest path to a trustworthy Passport" feature — recommending the specific set of missions that would most efficiently convert a resume-heavy, evidence-light profile into a fully verified one.

**Future Revisit Conditions:** The tension between goal-directed and uncertainty-directed selection (they may sometimes recommend different next steps) needs an explicit prioritization rule during Recommendation Model's detailed design — flagged as expected, not a reason to revisit this decision.

---

## Decision 017 — Progressive Profiling / Never Ask What Can Be Inferred

**Status:** Approved

**Decision:** The Career MRI onboarding experience should infer as much as possible from available Signal Sources (resume, GitHub, LinkedIn, portfolio, assessments) before asking the user directly, with the platform's understanding continuously updated over time (Progressive Profiling) rather than captured once at onboarding.

**Context:** Founding onboarding philosophy: the platform should feel like a diagnosis, not a questionnaire, with a target of <90 seconds to first clarity.

**Alternatives Considered:**
- *Traditional upfront questionnaire* covering Identity, Intent, and Capability before any recommendation is shown.

**Why Alternatives Were Rejected:** A traditional questionnaire directly works against the <90-second time-to-first-clarity goal and against the "this platform understands me" onboarding feel that differentiates this product from generic roadmap tools.

**Final Decision:** Infer-first onboarding via Signal ingestion, with explicit acknowledgment (see Open Question B, now resolved by Decision 014) that Intent specifically often cannot be reliably inferred and may require direct capture even under this principle.

**Why this makes the company stronger:** Delivers on the core "clarity in seconds, not questionnaires" differentiator the product is positioned around.

**What engineering problems it prevents:** Prevents onboarding flows from defaulting to generic, low-effort question forms simply because that's the easiest thing to build, which would undermine the product's positioning from day one.

**What future features it enables:** Enables onboarding to keep improving over time purely by adding new Signal Sources, without ever needing to add more questions to the initial flow.

**Future Revisit Conditions:** None anticipated beyond the Intent-capture nuance already resolved by the Exploration/Guided Mode split.

---

## Decision 018 — Single Responsibility Rule Across All Models

**Status:** Approved

**Decision:** Every major model in the framework must declare a single responsibility, its primary consumers, and its owning architectural layer. No two models may claim the same responsibility.

**Context:** This rule emerged directly from repeated review findings during framework design — the original table of contents had at least two clear overlaps (Recommendation Model vs. Mission Selection Model; Confidence Model vs. Readiness Model) that were only caught through explicit review.

**Alternatives Considered:**
- *No formal responsibility declaration requirement*, relying on section authors to avoid overlap organically.

**Why Alternatives Were Rejected:** Organic avoidance already demonstrably failed once during this framework's own design process — overlaps were found and had to be corrected. A formal, checkable requirement is the only reliable prevention mechanism given the framework's size and the number of people who will eventually extend it.

**Final Decision:** Every model entry includes a mandatory Single Responsibility / Primary Consumers / Primary Owner declaration (see the model hierarchy table in the CIF), checked at design time for any new or modified model.

**Why this makes the company stronger:** Keeps the framework legible and extensible as the team grows — new engineers can determine where a new capability belongs by checking existing responsibility declarations rather than by tribal knowledge.

**What engineering problems it prevents:** Prevents the same category of overlap bug (two models quietly doing the same job, drifting apart, producing inconsistent results) that was caught twice during this framework's own design.

**What future features it enables:** Makes it straightforward to onboard new team members and external contributors to the framework, since "which model owns this" is always answerable by inspection rather than institutional memory.

**Future Revisit Conditions:** None anticipated; this is a standing process rule, not a one-time decision.

---

## Decision 019 — Strict Layered Architecture (Layer 0–4)

**Status:** Approved

**Decision:** The framework is organized into strict layers — Sources, Signal Processing, Intelligence, Action, Expression — plus a parallel Structural layer (Taxonomy/CKG/Skill Graph) and an Interface layer (Career DNA). A layer may only read from layers/structures it depends on; no model may skip a layer (e.g., Recommendation Model never reads raw Signals directly; Passport never reads Conflict Resolution's internal state directly).

**Context:** Needed to prevent shortcuts where a model reaches directly into low-level data for convenience, bypassing the rules (verification, decay, conflict resolution, disclosure filtering) that exist specifically to govern how that data should be used.

**Alternatives Considered:**
- *A flatter architecture* where any model can read any other model's output directly, governed only by convention/code review rather than structural layering.

**Why Alternatives Were Rejected:** A flat architecture relies entirely on discipline to prevent a model from taking a shortcut under deadline pressure (e.g., Recommendation reading raw GitHub Signals directly instead of going through Evidence/Conflict Resolution/Decay), which is precisely the kind of shortcut that would quietly reintroduce unverified data into user-facing scoring or, worse, into the Passport.

**Final Decision:** Strict, enforced layering with defined read boundaries.

**Why this makes the company stronger:** Makes every trust guarantee in this document (Evidence-only Capability growth, Verified-only Passport disclosure, auditable Skill Graph propagation) structurally enforceable rather than dependent on every future engineer independently remembering and honoring the rule.

**What engineering problems it prevents:** Prevents an entire category of "convenient shortcut" bugs where a feature reaches past the governance layer that exists specifically to protect data integrity, trust, or scoring stability.

**What future features it enables:** Enables confident, fast iteration within a layer (e.g., improving Recommendation logic) without needing to re-audit the entire pipeline each time, since the layer boundaries guarantee what data any given model can and cannot see.

**Future Revisit Conditions:** None anticipated; this is the organizing invariant of the entire framework.

---

## Summary Table

| ID | Decision | Status |
|---|---|---|
| 001 | Framework renamed CIF (not CIS) | Approved |
| 002 | Five-dimensional Career Intelligence Model | Approved |
| 003 | Behavior as Signal Source, not dimension | Approved |
| 004 | Inferred vs. Verified as Signal state, not parallel pipelines | Approved |
| 005 | Career DNA as aggregation interface, not data owner | Approved |
| 006 | Career Knowledge Graph as separate foundational model | Approved |
| 007 | Skill Graph downstream of CKG, curated-subset consumption | Approved |
| 008 | Signal Model as universal ingestion boundary | Approved |
| 009 | Evidence Model as sole promotion path to Verified | Approved |
| 010 | Conflict Resolution as distinct Layer 1 gate, separate from Trust | Approved |
| 011 | Per-skill-node Confidence, no stored global aggregate | Approved |
| 012 | Contextual Readiness, single model with context parameter | Approved |
| 013 | Explainability as structural output contract | Approved |
| 014 | Exploration Mode vs. Guided Mode | Approved |
| 015 | Evidence-only Capability growth | Approved |
| 016 | Bidirectional Recommendation Loop | Approved |
| 017 | Progressive Profiling / infer-first onboarding | Approved |
| 018 | Single Responsibility Rule across all models | Approved |
| 019 | Strict layered architecture (Layer 0–4) | Approved |

## Open Items Carried Forward (not decisions — tracked for future ADRs)

These were identified during architecture design but deliberately left unresolved pending detailed model design. They are not part of this ADR's approved decisions and should be closed out via future ADRs referencing this one:

- **Open Item F:** CKG versioning — independent vs. coupled to Skill Taxonomy versions.
- **Open Item G:** Whether CKG stores entity detail or thin references only, with a separate content system owning detail.
- **Open Item H:** Whether CKG needs its own lightweight conflict/confidence-tagging rule for disagreeing edges.
- **Open Item M:** Governance/promotion process for CKG relationships becoming eligible Skill Graph propagation edges.
- **Open Item N:** Which model owns forward projection of Readiness/Progress for the Explainability Contract's "Expected impact" field.

---

*This document is frozen upon approval. Any future change to a decision recorded here requires a new ADR that explicitly references and supersedes the relevant Decision ID above.*
