# Engineering Architecture Document (EAD)

**Document ID:** EAD-001
**Document Type:** Architecture Blueprint
**Status:** Proposed
**Version:** 1.0
**Owner:** Chief Software Architect
**Governed By:** EAF, CIF, FGM, FC

---

## 1. Purpose
The Engineering Architecture Document (EAD) is the definitive engineering blueprint for ProjectEcho. It translates the technology-agnostic principles of the Engineering Architecture Framework (EAF) into a concrete, actionable system architecture. This document provides the explicit boundaries, dependency rules, and integration mechanisms that engineering teams will implement. 

## 2. Scope
The EAD governs:
- Logical architecture and systemic structure.
- Bounded Context definitions and their boundaries.
- Module definitions, responsibilities, and dependency rules.
- Integration mechanisms and communication patterns.
- Deployment strategy (Modular Monolith).
- Cross-cutting concerns and security architecture.

## 3. Out of Scope
The EAD does **not** define:
- Business strategy or product identity (Governed by FC-001).
- Governance rules (Governed by FGM-001).
- Domain terminology (Governed by CIF-001).
- Specific coding standards or stylistic linting rules.
- Class-level implementation details or database schemas.

---

## 4. Architectural Overview
ProjectEcho is built as a **Modular Monolith**. 
- **Deployment Philosophy:** The system deploys as a single runtime artifact to maximize operational simplicity and developer velocity. 
- **Bounded Context Philosophy:** While deployed together, the internal architecture is strictly segregated into Bounded Contexts aligned with the CIF domain. 
- **Scalability Approach:** The monolith scales horizontally. If future scalability requires independent scaling of a specific domain, the strict internal module boundaries guarantee a safe extraction into a microservice.
- **Maintainability Goals:** Forced dependency structures (DAG) and anti-corruption layers ensure the monolith does not degrade into a "Big Ball of Mud".

## 5. System Context
- **Users:** Professionals (managing Passports), Platform Operators, Auditors.
- **External Systems:** Identity Providers (OAuth/OIDC), HRIS systems, external job boards (Opportunities).
- **AI Services:** External Large Language Model (LLM) APIs used for explainable synthesis and Readiness calculation.
- **Knowledge Sources:** GitHub, LinkedIn, Calendars (sources of Signals).

---

## 6. Bounded Contexts
Derived from the CIF, the system is divided into four primary Bounded Contexts:

### A. Passport Context
- **Purpose:** Manages the core identity, Career Goals, and the aggregate view of the Career Passport.
- **Owned Concepts:** Person, Career Passport, Career Goal.
- **Inbound Dependencies:** Consumes aggregated Readiness from Intelligence Context.
- **Outbound Dependencies:** None. (Core domain).

### B. Competency Context
- **Purpose:** Manages the ontological definitions of skills and the evaluation of capabilities.
- **Owned Concepts:** Competency, Skill, Capability, Assessment, Mission.
- **Inbound Dependencies:** Consumed by Evidence and Intelligence Contexts.
- **Outbound Dependencies:** None.

### C. Evidence Context
- **Purpose:** Ingests raw Signals, verifies provenance, and establishes immutable Evidence.
- **Owned Concepts:** Signal, Evidence, Project, Context, Confidence.
- **Inbound Dependencies:** Consumed by Intelligence Context.
- **Outbound Dependencies:** Competency Context (to map evidence to a capability).

### D. Intelligence Context
- **Purpose:** Calculates Readiness, identifies Gaps, and generates explainable Recommendations.
- **Owned Concepts:** Readiness, Gap, Recommendation, Insight.
- **Inbound Dependencies:** Web/API presentation layer.
- **Outbound Dependencies:** Passport, Competency, and Evidence Contexts.

---

## 7. Module Architecture
Modules represent the physical manifestation of Bounded Contexts within the codebase (e.g., Maven modules).

1. **`echo-common`**: Defines cross-cutting interfaces, shared exceptions, and base domain events. Contains no business logic.
2. **`echo-identity`**: Handles authentication, token validation, and RBAC. 
3. **`echo-passport`**: Implementation of the Passport Context. 
4. **`echo-competency`**: Implementation of the Competency Context.
5. **`echo-evidence`**: Implementation of the Evidence Context. 
6. **`echo-intelligence`**: Implementation of the Intelligence Context. 
7. **`echo-bootstrap`**: The application entry point. Assembles the monolith, wires dependencies, and exposes the unified REST API.

*See `Module_Catalog.md` for detailed responsibilities and lifecycle.*

---

## 8. Dependency Rules
- **Allowed:** `echo-bootstrap` may depend on all modules. Domain modules may depend on `echo-common`.
- **Forbidden:** Domain modules (`passport`, `competency`, `evidence`, `intelligence`) may not depend on `echo-bootstrap` or `echo-identity`. Circular dependencies between domain modules are strictly prohibited.
- **Dependency Direction:** Dependencies must always point inward toward the most stable core concepts (Passport and Competency).
- **Module Isolation:** Modules interact exclusively via public interfaces/contracts defined in `echo-common` or their respective `api` packages.

*See `Dependency_Matrix.md` for the explicit rules grid.*

---

## 9. Data Ownership
- **Passports & Goals:** Owned by `echo-passport`. Source of truth.
- **Skills & Missions:** Owned by `echo-competency`. 
- **Signals & Evidence:** Owned by `echo-evidence`. Must be treated as immutable, append-only data.
- **Readiness & Recommendations:** Ephemeral/cacheable data owned by `echo-intelligence`.

**Rule:** A module may never directly read or write to the database tables owned by another module.

---

## 10. Communication Architecture
- **Synchronous Communication:** Used for query operations (e.g., UI requesting a Passport view). Handled via direct Java interface calls between modules to avoid network latency within the monolith.
- **Asynchronous Events:** Used for state mutations (e.g., "Signal Ingested", "Evidence Verified"). Modules publish Domain Events to an in-memory event bus (e.g., Spring ApplicationEvents). 
- **Commands:** Explicit instructions to mutate state, validated by the receiving module.

---

## 11. Cross-Cutting Concerns
- **Authentication:** Resolved at the gateway/entry point (`echo-identity`). Internal modules trust the injected security context.
- **Observability:** Centralized logging via correlation IDs generated at the entry point. Every domain event logs its origin.
- **Audit:** All mutations to `Evidence` or `Passport` emit an immutable audit log.
- **Error Handling:** Standardized `EchoDomainException` hierarchy in `echo-common`. HTTP semantics (e.g., 404 vs 400) are mapped only in the presentation layer.

---

## 12. AI Gateway
- **Responsibilities:** The AI Gateway resides within `echo-intelligence`. It orchestrates prompts and abstracts the underlying LLM provider.
- **Evidence Retrieval:** The AI relies strictly on context provided by `echo-evidence`. It does not possess hidden state.
- **Human Approval:** Recommendations generated by AI are flagged as `Unverified_Insight` until reviewed by the Person.

---

## 13. External Integrations
- **Principles:** All external integrations (GitHub, LinkedIn, ATS) are treated as untrusted `Signal` sources.
- **Anti-Corruption Layers (ACL):** External payloads are immediately mapped to internal `Signal` DTOs by integration adapters within the `echo-evidence` module.

---

## 14. Deployment Architecture
- **Deployment Unit:** A single, self-contained executable JAR (e.g., Spring Boot executable jar) containerized via Docker.
- **Scaling Strategy:** N-instances behind a stateless load balancer.
- **Environment Separation:** Strict separation of Dev, Staging, and Production environments managed via externalized configuration.

---

## 15. Security Architecture
- **Identity:** Stateless JWT tokens validated by `echo-identity`.
- **Trust Boundaries:** The REST API edge is the primary trust boundary. Intra-module calls assume trusted data.
- **Privacy:** Personally Identifiable Information (PII) is isolated in `echo-passport` and restricted from analytical event logs.

---

## 16. Quality Attribute Mapping
- **Maintainability:** Guaranteed by strict module boundaries and acyclic dependencies.
- **Explainability:** Guaranteed by the Evidence model and immutable audit trails.
- **Traceability:** Guaranteed by correlation IDs across all logs and Domain Events.

---

## 17. Risks
- **Architectural Risk:** The Modular Monolith boundaries degrade over time. *Mitigation:* Automated architectural fitness functions (e.g., ArchUnit) running in CI.
- **Operational Risk:** In-memory event bus drops events on crash. *Mitigation:* Transactional Outbox pattern for critical events.

---

## 18. Architectural Decision Inventory
The following formal ADRs must be drafted to solidify specific implementation technologies:
- ADR-003: Selection of Database Technology (Relational vs. NoSQL)
- ADR-004: Event Bus Implementation (In-memory vs. External Broker)
- ADR-005: LLM Provider Abstraction Strategy
- ADR-006: Frontend-Backend Integration Protocol (REST vs. GraphQL)

*See `Architecture_Decision_Backlog.md` for tracking.*

---

## 19. Traceability
- **FC-001 (Founding Charter):** Establishes this is a Career Intelligence platform.
- **FGM-001 (Governance):** Dictates this EAD cannot be overridden by code.
- **CIF-001 (Domain):** Defines the nouns and verbs mapped in Section 6.
- **EAF-001 (Principles):** Defines the Modular Monolith strategy and Explainability mandate executed in this document.

---

## 20. Implementation Guidance
Engineers implementing this blueprint must:
1. Respect the package boundaries. Use package-private visibility heavily to enforce information hiding.
2. Never inject a Repository from Context A into Context B.
3. Emit Domain Events for all state changes instead of synchronously calling other modules.
4. If a feature requires breaking the dependency matrix, halt and propose an architecture amendment.
