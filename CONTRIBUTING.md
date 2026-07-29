# Contributing to ProjectEcho

**Document ID:** NAV-0006
**Document Type:** Repository Process Document
**Status:** Active
**Version:** 1.0
**Classification:** Internal
**Owner:** Documentation & Architecture Engineering
**Date:** 2026-07-28
**Review Cadence:** On change to the review protocol or the governance corpus

---

## Purpose

How to make a change to this repository.

## Authority

**None.** This document introduces no policy. Every rule below is restated from [`PROJECT_MANIFEST.md`](PROJECT_MANIFEST.md), the [Review Protocol](.ai/workflows/REVIEW_PROTOCOL.md), or the [Documentation Standard](docs/reference/standards/DOCUMENTATION_STANDARD.md), and links to its source. Where this document and a source disagree, the source is correct.

---

## Before you start

Read, in order: [PROJECT_MANIFEST.md](PROJECT_MANIFEST.md) → [README.md](README.md) → [docs/INDEX.md](docs/INDEX.md) → the ADRs relevant to your change. This order is set by the manifest's Development Workflow.

Then read [PROJECT_STATUS.md](PROJECT_STATUS.md). The repository has open S1 conflicts, and some work is blocked in ways that are not obvious from the code.

## The governing rule

> Governance precedes implementation. Implementation cannot contradict approved governance documents.
> — [PROJECT_MANIFEST.md](PROJECT_MANIFEST.md), Governance Philosophy

Concretely: if your change contradicts an ADR, the change is wrong — or the ADR must be superseded first. Editing an approved document to match your change is never the route. Both current ADRs are **Frozen**, meaning they are closed to edits entirely and can only be replaced by a superseding ADR.

## Which kind of change are you making?

**Documentation.** Follow the [Documentation Standard](docs/reference/standards/DOCUMENTATION_STANDARD.md): metadata block (§1), required sections (§3), naming (§6), relative links (§7). Start from a file in [`docs/templates/`](docs/templates/). Add the document to [`docs/INDEX.md`](docs/INDEX.md).

**A decision that changes architecture or governance.** Open an ADR or ARBR — manifest step 3. Do not implement first and document after; that inverts the governance philosophy above.

**Code.** Feature branch, PR linked to the ADR or task that authorises it. Note that implementation is currently gated behind the EAD, which does not exist and is blocked ([PROJECT_STATUS.md](PROJECT_STATUS.md)).

**A review of someone else's document.** The [Review Protocol](.ai/workflows/REVIEW_PROTOCOL.md) is binding: review, do not rewrite; never modify approved decisions; never invent requirements; tag claims `[FACT]`, `[OBSERVATION]`, `[INFERENCE]` or `[SPECULATION]`; stop after the review.

## If you find a contradiction

Do not fix it. Discovering a conflict is not authority to resolve it ([Documentation Standard §8](docs/reference/standards/DOCUMENTATION_STANDARD.md)).

1. Add it to the [Conflict Register](docs/reports/engineering/CONFLICT_REGISTER.md) with both positions, evidence by file and line, and the authority that can close it.
2. Add a cross-reference in the affected documents — and change nothing else in them.
3. Escalate per the Review Protocol's Founder Escalation rules: two approved frameworks conflict, founder intent is ambiguous, a decision changes another framework, or precedence cannot be determined.

This applies especially when the fix looks obvious. Several conflicts currently open have an obvious-looking answer; recording them as decided would manufacture a founder decision that was never made.

## Branches and commits

Feature branches, one PR per logical change, linked to an ADR or task — manifest Repository Workflow.

`[OBSERVATION]` Commit history follows Conventional Commits (`chore:`, `refactor(repo):`). This is observed practice rather than a documented rule; `.gitmessage` exists but is empty. Following it is advisable; it is not enforced, and no document mandates it.

## Validation

```
mvn -f backend/pom.xml test
```

Maven only. There is no Gradle build and no Maven wrapper, so a local Maven install is required ([F-17](docs/reports/engineering/DAR-0001-documentation-audit.md#f-17)). No module currently produces a bootable jar.

There is **no CI**. Nothing runs automatically on a PR. CI design is an EAD-level concern and the EAD is blocked, so validation is manual and reviewer-enforced for now.

## Updating project memory

When a decision is approved, update the relevant `docs/adr/` document and `PROJECT_STATUS.md` — manifest Documentation Workflow. Record the approval on the artifact itself as well; a status stated only in a navigation document is a governance defect ([Documentation Standard §2](docs/reference/standards/DOCUMENTATION_STANDARD.md)).

## Not covered here

Licensing (no licence has been selected — [CR-013](docs/reports/engineering/CONFLICT_REGISTER.md#cr-013)), code of conduct (`CODE_OF_CONDUCT.md` is empty; adopting one is a founder decision), and security disclosure (no policy exists). These are recorded as gaps, not filled by default.
