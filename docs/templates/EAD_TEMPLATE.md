# ENGINEERING ARCHITECTURE DOCUMENT (EAD)

**Document ID:** EAD-vMAJOR.MINOR
**Document Type:** Engineering Architecture Document — the technology-bound realisation of the EAF
**Status:** Draft | In Review | Approved
**Version:** vMAJOR.MINOR
**Classification:** Internal
**Owner:** Principal Software Architect
**Date:** YYYY-MM-DD
**Review Cadence:** On each ADR that changes the stack or topology; otherwise quarterly
**Governed By:** Engineering Architecture Framework → Architecture Decision Records → Framework Governance Model
**Related Documents:**

---

## Purpose

The EAD is where technology enters. It states how each EAF primitive is realised in the chosen stack, and it is the last document before implementation.

## Authority

The EAD decides **implementation architecture**: libraries, layouts, mechanisms, topologies. It may not decide anything the EAF, an ADR, or a founder decision has already decided, and it may not introduce a primitive the EAF has not defined.

**Traceability requirement.** Every major structural choice in this document states which EAF Quality Attribute it serves and which ADR Decision ID authorises it. A choice that cannot cite either is out of scope for this document and requires an ADR first.

## Scope

## Out of Scope

Everything the EAF owns (what primitives *are*), everything an ADR owns (which architecture and stack), and everything the EDF owns (how a feature is designed within this architecture).

## Dependencies

The EAD may not be authored ahead of a ratified EAF. List the exact document versions this EAD is written against.

## Related Documents

---

## 1. Architectural Context

The approved architecture this document realises, cited by ADR Decision ID. Do not restate the decisions — link them.

## 2. Technology Realisation of EAF Primitives

The core of this document. One row per EAF primitive.

| EAF Primitive | Realisation | Quality Attribute Served | Authorising Decision |
|---|---|---|---|

Any primitive with no realisation is stated explicitly as deferred, with the reason.

## 3. Module Structure

Per [ARBR-0001 AR-001](../arbr/ARBR-0001.md), a Module is a bounded context with an explicit interface, owned aggregates, owned business rules, and owned persistence access.

| Module | Bounded Context | Interface | Owner | Version |
|---|---|---|---|---|

## 4. Shared Kernel Governance

What the Shared Kernel may contain and what it may never contain. Reference the governing decision on whether Shared Kernel changes require ADR approval.

## 5. Transaction and Consistency Boundaries

Where transactions begin and end. Which interactions are strongly consistent and which are eventually consistent, and why.

## 6. Partial Failure and Error Model

How a Module failure is translated into a domain error, and how the caller decides recovery. Cascading failure must be structurally impossible, not merely discouraged.

## 7. Persistence Architecture

Schema ownership, migration strategy, Repository realisation, Read Model rebuild triggers.

## 8. Event Architecture

Transport, delivery semantics, idempotency, ordering, replay, schema evolution.

## 9. AI Gateway and Validation Pipeline

The realisation of the mandatory pipeline: structured output → validation → business rules → persistence. State the enforcement point that makes bypass impossible.

## 10. Rule Engine

How the Rule Engine's sole-decision-authority property is structurally enforced, and how rule versions attach to outputs.

## 11. Observability

Metrics, logging, tracing, correlation. Note the EAF records Observability as a stated goal with an open structural gap — this section is where that gap closes.

## 12. Security Architecture

Authentication, authorisation enforcement point, tenant isolation mechanism, secret management, data classification.

## 13. Feature Flags

Lifecycle, storage, evaluation, and the constraint that flags branch only in Application Services.

## 14. Caching, Resilience, Performance Budgets

## 15. Testing Architecture

## 16. Module Extraction Playbook

The concrete procedure that makes "designed for extraction, not extracted now" a testable property rather than an aspiration.

## 17. Deployment Topology

## Traceability Matrix

| EAD Section | EAF Quality Attribute | ADR Decision | ARBR Item |
|---|---|---|---|

## Self-Review

**Remaining Ambiguity:**
**Hidden Assumptions:**
**Founder Decisions Still Required:**
**Conflicts with existing governance:**
