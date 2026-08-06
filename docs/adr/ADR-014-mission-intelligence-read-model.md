---
Document ID: ADR-014
Title: Mission Intelligence Read Model Strategy
Version: 1.0
Status: Proposed (Requires Founder Approval)
Classification: Architecture
Owner: Principal Architect
Authority Level: Founder
Primary Audience: Engineers
Governed By: CIF-0001, FDR-003, ADR-0002
---
# ADR-014: Mission Intelligence Read Model Strategy

**Status:** Approved
**Date:** 2026-08-06
**Category:** Performance / Architecture

## Context
ProjectEcho must evaluate 11 inputs across multiple module boundaries to generate a Mission Intelligence Score for users. PRD NFR-02 mandates a sub-200ms API response time. Directly querying the distributed canonical models (Identity, Evidence, Taxonomy, Mission) across isolated PostgreSQL schemas (ADR-003) at request-time violates this latency bound.

## Founder Decision Constraints (2026-08-06)
Mission Intelligence calculations may introduce derived read models provided that:
- Domain ownership remains unchanged.
- Write models remain authoritative.
- Business rules remain exclusively in the Rule Engine.
- Explainability is preserved.
- Traceability is preserved.

## Options Evaluated

### Option 1: Synchronous Aggregation
- **Mechanism**: The `echo-mission` module makes synchronous internal calls to `echo-evidence` and `echo-taxonomy` at request time.
- **Trade-offs**: Strong consistency. However, network/serialization overhead and massive cross-module data aggregation will predictably breach the 200ms threshold for users with dense profiles.

### Option 2: Distributed Caching (Redis)
- **Mechanism**: The calculated scores are cached in Redis.
- **Trade-offs**: Fast reads. However, complex cache invalidation logic is required across 4 domains (e.g., if a new taxonomy node is added, millions of cache entries invalidate). Violates simplicity constraint.

### Option 3: Event-Updated Materialized Read Model
- **Mechanism**: The `echo-intelligence` module acts as the authoritative Rule Engine. Upon calculating the 11-input score, it emits an integration event. The `echo-mission` module listens to this event and updates a flat, UI-optimized read model.
- **Trade-offs**: Eventual consistency. Write models remain pristine. Rule Engine retains total authority. Explainability is preserved by storing the authoritative `ReasoningCardId` alongside the flat data.

### Option 4: Full CQRS with Event Sourcing
- **Mechanism**: Separate command and query databases entirely using Kafka/EventStore.
- **Trade-offs**: Highly scalable but massively over-engineered for a Modular Monolith MVP.

## Recommended Approach
**Option 3: Event-Updated Materialized Read Model** is APPROVED.

### Implementation Mandates
Per Founder Approval, the following architectural constraints are mandatory:

1. **Event Contract Ownership**: Integration events are owned by the Shared Kernel contract layer (or equivalent shared contract package). Only immutable event contracts may cross module boundaries. Domain entities must never cross module boundaries. Furthermore, Integration Events must rely exclusively on language primitives (e.g., UUID, String) and must never reference internal Java Domain Value Objects to maximize decoupling and serialization safety.
2. **Transaction Reliability**: The architecture shall guarantee atomic publication of integration events with respect to the authoritative write model. The implementation mechanism will be selected during the Engineering Design phase.
3. **Read Model Recovery**: Read models are disposable. Read models are never authoritative. Read models must be rebuildable from authoritative sources.
4. **Event Versioning**: Every integration event must include: Event ID, Event Version, Correlation ID, Causation ID, Timestamp, and Aggregate Identifier.
5. **Explainability Preservation**: The read model shall never execute business rules or derive business decisions. It may only materialize outputs already produced by the Rule Engine.

## Governance Impact
- Modifies `EAD-0001` to explicitly sanction a read-model schema in `echo-mission`.
- Resolves the final Open Architecture Decision, authorizing the Architecture Freeze.
