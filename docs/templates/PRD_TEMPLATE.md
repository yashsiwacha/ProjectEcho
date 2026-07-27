# PRD-NNNN — <Feature or Capability Name>

**Document ID:** PRD-NNNN
**Document Type:** Product Requirements Document
**Status:** Draft | In Review | Approved
**Version:** 1.0
**Classification:** Internal
**Owner:** Chief Product Officer
**Date:** YYYY-MM-DD
**Review Cadence:** Until delivered; then archived
**Governed By:** Engineering Design Framework → EAD → EAF → ADRs → Domain Frameworks → FGM
**Related Documents:**

---

## Purpose

What product behaviour this document specifies.

## Authority

A PRD specifies **product behaviour**. It may not decide architecture, technology, or module structure, and it may not require behaviour that contradicts an approved decision above it. Where a PRD needs something the architecture forbids, the correct output is an ADR proposal, not a PRD requirement.

## Scope

## Out of Scope

## Dependencies

Which framework definitions this PRD relies on. Business terminology is used exactly as the CIF defines it and is never redefined here.

## Related Documents

---

## 1. Problem Statement

The user problem, evidenced. Distinguish `[FACT]` from `[INFERENCE]`.

## 2. Users and Jobs

| User | Job to be done | Evidence |
|---|---|---|

## 3. Requirements

Each requirement is individually identified, testable, and traceable.

| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| R-001 | | Must / Should / Could | |

## 4. Behaviour Specification

Including failure behaviour and empty states. A requirement that specifies only the success path is incomplete.

## 5. Explainability Requirements

Every user-facing derived value states what it was derived from. Reference the Explainability Contract rather than restating it.

## 6. Non-Functional Requirements

Trace each to an EAF Quality Attribute.

## 7. Out-of-Scope Behaviour

What this deliberately does not do, and why. Protects against scope drift during implementation.

## 8. Success Metrics

| Metric | Baseline | Target | How measured |
|---|---|---|---|

## 9. Dependencies and Sequencing

## 10. Open Questions

| ID | Question | Owner | Blocks |
|---|---|---|---|

## Governance Check

- Which approved decisions constrain this PRD?
- Does any requirement here require a new ADR? If so, name it — the PRD may not proceed past review until that ADR exists.
- Does any requirement redefine a CIF or EAF term? If so, it is defective.

## Self-Review

**Remaining Ambiguity:**
**Hidden Assumptions:**
**Founder Decisions Still Required:**
**Conflicts with existing governance:**
