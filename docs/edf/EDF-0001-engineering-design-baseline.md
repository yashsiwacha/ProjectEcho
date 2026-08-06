# EDF-0001: Engineering Design Baseline

**Document ID:** EDF-0001
**Status:** FROZEN
**Version:** 1.0
**Date:** 2026-08-06
**Governed By:** EAD-0001, ADR-014, ADR-003

---

## 1. Persistence Engineering

Derived from `ADR-003` (PostgreSQL) and `ADR-014` (Materialized Read Models).

### 1.1. Repository Interfaces
- **Technology**: Spring Data JPA.
- **Location**: `com.projectecho.[module].infrastructure.persistence`.
- **Constraint**: Repositories must return Domain Entities or Aggregates, never raw DTOs or projections (except explicitly in `echo-mission` read models).

### 1.2. Transaction Boundaries
- Transactions (`@Transactional`) are strictly bounded to a single Aggregate Root.
- Cross-aggregate or cross-module state mutations are strictly forbidden within the same transaction.
- **Implementation**: Any side-effects must be implemented via Integration Events published after the primary transaction commits.

### 1.3. Flyway Strategy
- **Isolation**: Each Bounded Context (Module) manages its own Flyway migration scripts.
- **Location**: `src/main/resources/db/migration/{module}/`.
- **Execution**: Spring Boot executes all locations on startup.

### 1.4. Concurrency & Locking
- **Strategy**: Optimistic Locking (`@Version`) is mandatory on all Aggregate Roots (e.g., `CareerPassport`, `EvidenceClaim`).
- Pessimistic locking is forbidden unless explicitly authorized by an ADR due to latency risks.

---

## 2. Integration Engineering

### 2.1. Internal Event Infrastructure
- **Technology**: Spring Application Events.
- **Transaction Reliability**: Events must be published atomically with respect to the authoritative write model (Per ADR-014).
  - **Implementation**: `TransactionalEventListener(phase = AFTER_COMMIT)` coupled with a transactional Outbox table per module.
- **Event Contracts**: Immutable Java `record` classes located in `echo-shared/src/main/java/com/projectecho/shared/events`.
- **Versioning**: Every event extends a base `IntegrationEvent` containing: `eventId`, `eventVersion`, `correlationId`, `causationId`, `timestamp`, `aggregateId`.

### 2.2. REST API Conventions
- **Style**: Richardson Maturity Model Level 2.
- **Versioning**: URI Versioning (`/api/v1/...`).
- **Data Transfer**: Strict isolation. DTOs live in `com.projectecho.[module].application.dto` and never enter the Domain layer.

### 2.3. Authentication Integration
- **Technology**: Spring Security with JWT filters (ADR-006).
- **Execution**: A global security filter intercepts requests, validates the JWT, and extracts the `PassportId`. The `PassportId` is injected into controller methods via a custom `@CurrentPassport` argument resolver.

### 2.4. Rule Engine Integration
- **Constraint**: `echo-intelligence` encapsulates all business logic.
- **Communication**: The Rule Engine listens to `TrustTierAssessedEvent` from Evidence, runs the evaluation, and emits `IntelligenceScoreCalculatedEvent`. No synchronous HTTP calls are permitted to trigger rules.
