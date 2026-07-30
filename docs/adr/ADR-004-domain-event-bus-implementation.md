# ADR-004: Domain Event Bus Implementation

**Status:** Proposed
**Date:** 2026-07-29
**Category:** Integration

## Context
Modules must communicate state changes (e.g., "Evidence Processed") without direct method-call coupling to preserve the DAG dependency structure defined in the EAD.

## Decision
We will use **Spring ApplicationEvents (In-Memory Event Bus)** for synchronous and asynchronous intra-process communication. We will employ the Transactional Outbox pattern backed by PostgreSQL for events that require guaranteed at-least-once delivery.

## Consequences
- **Positive:** No external broker (Kafka/RabbitMQ) required at this stage, keeping the deployment topology simple.
- **Negative:** If we split the monolith into microservices in the future, we will need to replace the in-memory bus with a distributed broker.
