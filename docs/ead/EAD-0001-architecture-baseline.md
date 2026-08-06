# EAD-0001: Architecture Baseline Report

**Document ID:** EAD-0001
**Document Type:** Engineering Architecture Document
**Status:** Approved (Architecture Baseline)
**Version:** 1.0
**Classification:** Internal
**Owner:** Chief Software Architect
**Date:** 2026-08-06
**Governed By:** PRD-0001, FDR-001, FDR-002, FDR-003, ADR-001, ADR-0002, ADR-003, ADR-004, ADR-006

---

## 1. System Context
- **Overall Architecture**: Modular Monolith deployed as a single Spring Boot 3 Java 21 application (`[FACT]` ADR-0002).
- **External Actors**: 
  - The Professional (Target User for Career Passports and Missions)
  - The Evaluator (System/Admin for Evidence Verification)
- **External Systems**: 
  - Trusted Evidence APIs (GitHub, Credly, Coursera - `[FACT]` FDR-002)
- **System Boundaries**: 
  - The system is an Evidence-Driven Career Intelligence Platform. It handles ingestion of career evidence, computation of Mission Intelligence, and presentation of AI reasoning. It explicitly **does not** act as an event delivery/webhook backbone (`[FACT]` FDR-001).

## 2. Bounded Context Map
Identified contexts derived **only** from the PRD and CIF.

1. **Identity Context** (Derived from PRD Epic 1)
   - *Purpose*: Establish and maintain the Professional's core identity.
   - *Responsibilities*: User authentication mapping, demographic storage, profile state.
   - *Owned Capabilities*: Feature 1.1 (Profile Initialization).
   - *Dependencies*: Auth module.
   - *Downstream Contexts*: Evidence Context, Mission Context.

2. **Taxonomy Context** (Derived from PRD Epic 1.2 / CIF Layer 0)
   - *Purpose*: Maintain the standardized skill ontology.
   - *Responsibilities*: Ensure claimed skills map to a verifiable source of truth.
   - *Owned Capabilities*: Feature 1.2 (Skill Taxonomy Mapping).
   - *Upstream Contexts*: None.
   - *Downstream Contexts*: Evidence Context, Intelligence Context.

3. **Evidence Context** (Derived from PRD Epic 2 / FDR-002)
   - *Purpose*: Ingest raw data and assess Trust Tiers (1-5).
   - *Responsibilities*: File storage linking, OAuth ingestion, immutable Trust Tier tagging.
   - *Owned Capabilities*: Feature 2.1 (Tiered Evidence Ingestion).
   - *Upstream Contexts*: Identity, Taxonomy.
   - *Downstream Contexts*: Intelligence Context.

4. **Intelligence Context** (Derived from PRD Epic 3 / FDR-003)
   - *Purpose*: Execute the Rule Engine matching and generate AI Reasoning Cards.
   - *Responsibilities*: Calculate the 11-input Mission Intelligence Score.
   - *Owned Capabilities*: Feature 3.1 (Evidence-Based AI Reasoning Cards).
   - *Upstream Contexts*: Evidence, Taxonomy, Mission.
   - *Downstream Contexts*: None.

5. **Mission Context** (Derived from PRD Epic 4)
   - *Purpose*: Manage opportunities and application eligibility thresholds.
   - *Responsibilities*: Categorize Missions (Ready Now/Soon/Future), handle gap analysis display.
   - *Owned Capabilities*: Feature 4.1 (Personalized Mission Dashboard).
   - *Upstream Contexts*: Identity.
   - *Downstream Contexts*: Intelligence Context.

## 3. Module Architecture
Backend module structure enforcing Clean Architecture.

- `echo-identity`: 
  - *Public Interfaces*: Profile creation and retrieval APIs.
  - *Internal Components*: User Registration Service.
  - *Allowed Dependencies*: `echo-shared`.
  - *Forbidden Dependencies*: `echo-intelligence`, `echo-mission`.
- `echo-taxonomy`: 
  - *Public Interfaces*: Skill typeahead APIs.
  - *Allowed Dependencies*: `echo-shared`.
- `echo-evidence`: 
  - *Public Interfaces*: Evidence upload endpoints.
  - *Allowed Dependencies*: `echo-identity`, `echo-taxonomy`.
  - *Forbidden Dependencies*: `echo-mission` (Evidence does not care about missions).
- `echo-intelligence`: 
  - *Public Interfaces*: Asynchronous rule engine triggers.
  - *Allowed Dependencies*: `echo-evidence`, `echo-taxonomy`, `echo-mission`.
  - *Future Extraction Considerations*: Highly likely to be extracted to a separate microservice due to CPU-bound AI reasoning generation.
- `echo-mission`: 
  - *Public Interfaces*: Mission dashboard APIs.
  - *Allowed Dependencies*: `echo-identity`.

## 4. Aggregate Catalog
- `echo-identity`: 
  - **Aggregate**: `CareerPassport` (Root).
- `echo-taxonomy`: 
  - **Aggregate**: `SkillNode`.
  - **Repository Interface**: `TaxonomyRepository`.
- `echo-evidence`: 
  - **Aggregate**: `EvidenceClaim`. 
  - **Value Objects**: `TrustTier` (Enum 1-5).
  - **Domain Services**: `TrustAssessmentService`.
- `echo-intelligence`: 
  - **Aggregate**: `MissionIntelligenceEvaluation`. 
  - **Entities**: `ReasoningCard`.
  - **Domain Services**: `ReadinessRuleEngine`.
- `echo-mission`: 
  - **Aggregate**: `Mission`. 
  - **Value Objects**: `ApplicationEligibility`.

*`[FACT]` No aggregates have been invented. All map directly to PRD-0001 features.*

## 5. Shared Kernel
Intentionally minimal to prevent tight coupling.
- **Shared Value Objects**: `PassportId`, `SkillId`, `MissionId`.
- **Shared Interfaces**: `DomainEvent` marker interface.
- **Shared Policies**: JWT Security Context evaluation (ADR-006).

## 6. Event Catalog
Logical events utilizing Spring Application Events in-memory (`[FACT]` ADR-004).
- `PassportInitializedEvent` (Publisher: Identity, Consumer: Evidence). Purpose: Set up empty evidence collections.
- `EvidenceIngestedEvent` (Publisher: Evidence). Trigger: Manual or API upload.
- `TrustTierAssessedEvent` (Publisher: Evidence, Consumer: Intelligence). Trigger: After AI/Peer verifies evidence. Purpose: Recalculate Readiness.
- `IntelligenceScoreCalculatedEvent` (Publisher: Intelligence, Consumer: Mission). Trigger: Rule Engine completes 11-input algorithm.

## 7. Rule Engine Blueprint
The Rule Engine is the **only** business decision authority (`[FACT]` CIF).
- **Rule Categories**: Readiness Calculation, Application Eligibility.
- **Rule Sources**: The 11 inputs mandated by FDR-003 (Competency, Trust Score, Behavioral, Freshness, Relevance, Goals, Velocity, Complexity, Employer Reqs, Confidence, Trajectory).
- **Rule Execution Flow**: 
  1. Triggered by `TrustTierAssessedEvent`.
  2. Query Taxonomy Graph for skill distances.
  3. Execute deterministic weights on Evidence array.
  4. Prompt Spring AI to generate English summary (AI generates text, NOT decisions).
  5. Store `ReasoningCard`.
- **Explainability Model**: Deterministic weights are stored explicitly on the `ReasoningCard` entity so the UI can trace back to specific `EvidenceClaim` IDs.

## 8. API Contracts
- `POST /api/v1/passports` (Creates Career Passport).
- `GET /api/v1/taxonomy/search?q={query}` (Typeahead Skill Search).
- `POST /api/v1/evidence` (Uploads documents/claims).
- `GET /api/v1/missions` (Retrieves categorized Mission Dashboard).
*`[FACT]` DTO boundaries exist exclusively at the Controller layer. DTOs are forbidden inside Domain layers.*

## 9. Persistence Model
- **Repository Interfaces**: Spring Data JPA interfaces located in the infrastructure layer of each module.
- **Transaction Boundaries**: Strict per-aggregate. Cross-aggregate mutations use `DomainEvent` listeners.
- **Data Isolation**: `[FACT]` Each Bounded Context utilizes its own logical schema within a single PostgreSQL database (`identity`, `taxonomy`, `evidence`, `intelligence`, `mission`) per ADR-003.

## 10. Architecture Traceability Matrix
| Architectural Element | Derived From (Governance Artifact) |
|---|---|
| Modular Monolith Structure | ADR-0002 |
| `TrustTier` (1-5) Enum | FDR-002, PRD Feature 2.1 |
| 11-Input Rule Engine | FDR-003, PRD BR-04 |
| `ReasoningCard` Entity | PRD Feature 3.1, US-007 |
| Categories (Ready Now/Soon/Future) | FDR-003, PRD Feature 4.1 |
| In-Memory Spring Events | ADR-004 |
| Schema-per-module PostgreSQL | ADR-003 |
| Materialized Read Model Strategy | ADR-014 |

## 11. Open Architecture Decisions
All Open Architecture Decisions have been resolved.

**OAD-001: Mission Intelligence Read Model Strategy**
- **Status**: [RESOLVED]
- **Resolution**: `ADR-014` has been formally approved by the Founder (2026-08-06). An Event-Updated Materialized Read Model is authorized with strict architectural constraints (Shared Kernel Event Ownership, Atomic Publication, Disposable Read Models, Explicit Versioning).

---
## Architecture Readiness Assessment
**Status**: FROZEN.
The Architecture Baseline successfully traces 100% of the Product Requirements into modular architectural components while honoring every existing ADR and Founder Decision. All Open Architecture Decisions have been resolved. Engineering is now authorized to safely begin scaffolding the Monolith.
