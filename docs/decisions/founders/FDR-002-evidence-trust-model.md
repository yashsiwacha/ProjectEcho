# Founder Decision Record: FDR-002
**Title:** Evidence Trust & Verification Model
**Status:** Approved
**Version:** 1.0
**Founder Approval:** Approved via Workshop
**Approval Date:** 2026-08-06

---

## Purpose
This Founder Decision resolves the `[FOUNDER CLARIFICATION PENDING]` item regarding how Evidence is verified within the Career Intelligence Platform (BR-01 and Feature 2.1).

## Core Principle
Project Echo is an Evidence-Driven Career Intelligence Platform. Every piece of evidence is accepted, but every piece of evidence carries an explicit Trust Level. The platform never treats all evidence equally, and the AI never declares evidence to be absolute "truth." Instead, it continuously evaluates confidence, trust, quality, consistency, recency, and completeness. Readiness is an explainable confidence model, not a binary verification model.

## Evidence Trust Model
Evidence is categorized into five strict Trust Tiers:

1. **Tier 1 — Claimed Evidence (Very Low Trust)**: Resumes, manual skill declarations, PDF uploads. Used for cold-start onboarding.
2. **Tier 2 — AI-Assessed Evidence (Low to Medium Trust)**: GitHub repository analysis, resume parsing, LinkedIn profile analysis. AI extracts observable signals; ownership is not guaranteed.
3. **Tier 3 — Platform Verified (High Trust)**: GitHub API, Credly, Coursera, AWS, LeetCode. Trusted third-party integrations.
4. **Tier 4 — Institutional Evidence (Very High Trust)**: Employer verification, University verification, employment records. Externally verified institutional trust.
5. **Tier 5 — Behavioral Evidence (Highest Trust)**: Mission completion, platform assessments, coding challenges, interview simulations. Evidence generated natively inside Project Echo.

## Readiness Model
Readiness must never depend only on evidence quantity. It evaluates:
- Evidence Quality & Trust Tier
- Evidence Freshness & Consistency
- Coverage of Competencies
- Behavioral Performance
- Confidence Score & Observed Improvement
*Every Readiness Score must be explainable.*

## AI Responsibilities
The AI must NEVER certify truth. It estimates confidence. 
- *Incorrect*: "This user definitely knows Spring Boot."
- *Correct*: "Current confidence that the user demonstrates Spring Boot competency: 91%, based on GitHub repositories, completed platform missions, and employer verification."

## Product Principle
Users are never blocked from adding evidence. Higher trust evidence naturally contributes more to Career Readiness. The platform rewards trustworthy evidence rather than restricting participation.

---

## Traceability & Consequences
- **Affected PRDs**: PRD-001 (Business Rules BR-01, Domain Glossary, Feature 2.1, AI Reasoning logic).
- **Affected Architecture**: The Rule Engine and Data Model must support 5 Trust Tiers and calculate confidence scores based on these weights. AI Prompts must be strictly constrained against certifying truth.
- **Affected UI**: The Stitch Design "Evidence-Based AI Reasoning Cards" must visually indicate the Trust Tier and Confidence Score, replacing binary "Verified" checkboxes.
