---
Document ID: ADR-004
Title: Domain Event Bus Implementation
Version: 2.0
Status: Frozen
Classification: Architecture
Owner: Principal Architect
Authority Level: 4
Primary Audience: Engineers
Governed By: CIF-0001, FGM-0001
Review Cadence: N/A
Last Updated: 2026-08-04
Next Review: N/A
---

# ADR-004: Domain Event Bus Implementation

## Status
Accepted (Founder Decision FD-004)

## Context
ProjectEcho utilizes an event-driven architecture internally to maintain module decoupling (per ADR-0002). We must choose an event bus technology to implement the Domain Event passing. The initial scaffolding used Kafka, which contradicts the local execution and simplicity constraints of a Modular Monolith MVP.

## Options Considered
1. **Spring Application Events (In-memory)** - Best fit for Modular Monolith MVP. Synchronous by default, easily made asynchronous. Zero external dependencies.
2. **Kafka** - Highly scalable, distributed. Contradicts MVP constraints.
3. **Outbox Pattern with PostgreSQL** - Durable, medium complexity. Good for eventual consistency, but high overhead for MVP.

## Decision
We will use **Option 1: Spring Application Events**. Kafka must be removed from the local infrastructure stack. 

## Consequences
- The system remains a true Modular Monolith with no distributed deployment requirements.
- Transactions can span event emission locally if required, simplifying data consistency.
- Any future migration to microservices will require swapping this out for a distributed event bus (like Kafka or RabbitMQ) and implementing the Outbox Pattern.
