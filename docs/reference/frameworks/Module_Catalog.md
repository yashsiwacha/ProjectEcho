# Module Catalog

**Governing Document:** EAD-001

This document defines the physical modules (e.g., Maven modules) that implement the bounded contexts of ProjectEcho. 

## 1. `echo-common`
- **Purpose:** Provide shared contracts and utilities required by all modules.
- **Responsibilities:** Definition of shared DTOs (e.g., `CorrelationId`), base domain events, and core exception hierarchies (`EchoDomainException`).
- **Owner:** Platform Engineering.
- **Data Owned:** None.

## 2. `echo-identity`
- **Purpose:** Secure the perimeter of the application.
- **Responsibilities:** Authenticating requests, validating JWTs, establishing the `SecurityContext`, and enforcing coarse-grained RBAC.
- **Owner:** Security Architecture.
- **Data Owned:** Minimal session state / token blacklists.

## 3. `echo-passport`
- **Purpose:** Implement the Passport Bounded Context.
- **Responsibilities:** Managing the Person entity, assembling the Career Passport view, and managing Career Goals.
- **Owner:** Product Engineering.
- **Data Owned:** `Person`, `CareerPassport`, `CareerGoal` records.

## 4. `echo-competency`
- **Purpose:** Implement the Competency Bounded Context.
- **Responsibilities:** Managing the ontology of Skills, defining Competencies, evaluating Capabilities, and managing Missions.
- **Owner:** Domain Engineering.
- **Data Owned:** `Competency`, `Skill`, `Capability`, `Mission` configurations.

## 5. `echo-evidence`
- **Purpose:** Implement the Evidence Bounded Context.
- **Responsibilities:** Ingesting external Signals, verifying provenance, constructing immutable Evidence records, and managing Projects.
- **Owner:** Data Engineering.
- **Data Owned:** `Signal`, `Evidence`, `Project` records.

## 6. `echo-intelligence`
- **Purpose:** Implement the Intelligence Bounded Context.
- **Responsibilities:** Calculating Readiness, identifying Gaps, generating Recommendations, and orchestrating LLM interactions.
- **Owner:** AI Engineering.
- **Data Owned:** Ephemeral `Readiness`, `Gap`, and `Recommendation` calculations.

## 7. `echo-bootstrap`
- **Purpose:** Assemble the Modular Monolith.
- **Responsibilities:** Wiring dependencies, externalized configuration, starting the embedded web server, and exposing the unified REST API. Contains no domain logic.
- **Owner:** Platform Engineering.
- **Data Owned:** None.
