# RAR-NNNN — <Title>

**Document ID:** RAR-NNNN
**Document Type:** Repository Architecture Report
**Status:** Draft | In Review | Approved
**Version:** 1.0
**Classification:** Internal
**Owner:** Documentation & Architecture Engineering
**Date:** YYYY-MM-DD
**Review Cadence:** On material change to repository structure
**Governed By:** Architecture Decision Records — a RAR sits below ADRs and above ARBRs in the precedence chain
**Related Documents:**

---

## Purpose

What this report establishes about the *repository* — its structure, layout, and physical organisation — as distinct from the *system's* architecture, which is the ADR's and EAD's concern.

## Authority

A RAR governs repository structure. It may not decide system architecture, product behaviour, or technology. Where a RAR and an ADR disagree, the ADR wins.

## Scope

## Out of Scope

## Dependencies

Which ADRs and frameworks this report's structure derives from. A RAR that does not trace its layout to an architectural decision is asserting structure without authority.

## Related Documents

---

## 1. Current Repository Structure

Describe the structure as it actually is, including what is empty. Distinguish clearly between *present*, *present but empty*, and *absent*.

```
<tree>
```

## 2. Structural Rationale

For each top-level directory: what it holds, what it must never hold, and which decision put it there.

| Directory | Holds | Must not hold | Justified by |
|---|---|---|---|

## 3. Alignment with Approved Architecture

Test the structure against each relevant ADR decision. Where the structure does not match, record it as a finding rather than adjusting the ADR.

| Architectural Decision | Structural Expression | Aligned? |
|---|---|---|

## 4. Findings

Use the finding format from [Documentation Standard §8](../reference/standards/DOCUMENTATION_STANDARD.md): Observation, Evidence, Impact, Affected Documents, Recommendation, Resolution Authority.

## 5. Recommended Structure

Only where a change is justified by an existing approved decision. A RAR may not propose structure that implies an architectural decision not yet made.

## 6. Migration Path

If restructuring is recommended: the ordered steps, what breaks, what references need updating, and who owns it.

## Self-Review

**Remaining Ambiguity:**
**Hidden Assumptions:**
**Founder Decisions Still Required:**
**Conflicts with existing governance:**
