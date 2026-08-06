# PRD-0001 — Career Intelligence Platform Baseline

**Document ID:** PRD-0001
**Document Type:** Product Requirements Document
**Status:** Approved (Freeze Candidate)
**Version:** 1.0
**Classification:** Internal
**Owner:** Chief Product Officer
**Date:** 2026-08-06
**Governed By:** FDR-001, FDR-002, FDR-003, ADR-001, ADR-0002

---

## 1. Product Vision
Replace static, unverifiable resumes with continuously evolving, evidence-driven Career Intelligence.

## 2. Product Mission
Provide measurable, competency-based career representations (Career Passports), explainable recommendations (Missions), and an infrastructure that enforces governance and traceability for every professional decision.

## 3. Problem Statement
Traditional job boards rely on static, unverified resumes, creating a low-trust environment for both candidates and recruiters. High-level professionals require a premium, analytical "Career Operating System" that matches them to opportunities using verifiable evidence and transparent AI reasoning, rather than opaque algorithms.

## 4. Target Users
- **High-level Professionals/Executives**: Seeking precision matching and a premium interface to manage their career trajectories.
- **Enterprise Talent Acquisition**: Requiring deterministic, evidence-backed matching for critical roles.

## 5. User Personas
- **The Professional**: Maintains a living "Career Passport". Values privacy, precision, and understanding *why* they were matched to a role.
- **The Evaluator (System/Admin)**: Defines competency frameworks and validates evidence integrity.

## 6. Value Proposition
A premium Career OS that replaces the traditional job hunt with an evidence-based intelligence platform, featuring "Progressive Disclosure" AI reasoning that explains exactly *why* a professional is ready for a mission.

## 7. Product Principles
- **Explainability First**: AI decisions must provide "Data Evidence" via Progressive Disclosure.
- **Premium Restraint**: The UI must remain calm, analytical, and professional (per Stitch Design System).
- **Traceable Governance**: Every architectural and product decision must be documented and immutable.
- **Evidence over Claims**: Career data must be treated as verified facts, not self-reported claims.

## 8. Functional Requirements
| ID | Requirement | Priority | Acceptance Criteria |
|---|---|---|---|
| FR-01 | **Career Passport Creation** | Must | User can initialize a Career Passport with baseline demographics. |
| FR-02 | **Evidence Ingestion** | Must | System accepts "Evidence" data points mapped to specific skills. |
| FR-03 | **Deterministic Readiness** | Must | Rule Engine evaluates Readiness score based on accumulated Evidence. |
| FR-04 | **AI Reasoning Cards** | Must | AI generates explanations mapping Evidence to a recommended Mission. |
| FR-05 | **Progressive Disclosure** | Must | UI displays summary reasoning, expanding to show raw Data Evidence on click. |

## 9. Non-functional Requirements
| ID | Requirement | EAF Quality Attribute |
|---|---|---|
| NFR-01 | **Modular Architecture** | Maintainability (ADR-0002 Modular Monolith) |
| NFR-02 | **Sub-200ms API Response** | Performance (AGENTS.md Global Rules) |
| NFR-03 | **UI Rendering Fidelity** | Usability (Must match Stitch Design System tokens exactly) |
| NFR-04 | **Accessibility (WCAG 2.1 AA)** | Accessibility (AGENTS.md Global Rules) |

## 10. Business Rules
- **BR-01 (Evidence Trust Model)**: Evidence is accepted from any source but categorized into 5 explicit Trust Tiers (1: Claimed, 2: AI-Assessed, 3: Platform Verified, 4: Institutional, 5: Behavioral). The platform rewards trustworthy evidence rather than restricting participation.
- **BR-02 (Readiness Model)**: Readiness is an explainable confidence model, not a binary verification model. It evaluates Evidence Quality, Trust Tier, Freshness, Consistency, Competency Coverage, Behavioral Performance, Confidence Score, and Observed Improvement.
- **BR-03 (AI Certification Constraint)**: AI must NEVER certify truth. It must only estimate confidence (e.g., "Current confidence that the user demonstrates Spring Boot competency: 91%").
- **BR-04 (Mission Intelligence Score)**: Missions are not ranked by a single percentage. Scoring uses 11 inputs (Competency, Trust Score, Behavioral, Freshness, Relevance, Goals, Velocity, Complexity, Employer Reqs, Confidence, Trajectory).
- **BR-05 (Mission Categories)**: All Missions are visible, categorized as "Ready Now", "Ready Soon", or "Future Aspiration". Rejections are forbidden; gaps must generate personalized growth plans.

## 11. Domain Glossary
- **Career Passport**: The living, data-rich representation of a user's professional identity.
- **Evidence**: Data points proving a specific competency, weighted by a 5-tier Trust Level (Claimed to Behavioral).
- **Mission**: An opportunity, role, or project recommended to the user.
- **Reasoning Card**: An AI-generated, explainable component mapping Evidence to a Mission using Confidence Scores, never absolute truth.

## 12. Success Metrics
- **Profile Density**: Average number of verified Evidence data points per Career Passport.
- **Reasoning Acceptance**: Percentage of AI recommendations accepted by the professional.
- **Platform Trust Score**: User rating of recommendation accuracy.

## 13. Product Constraints
- Must utilize PostgreSQL and Redis (ADR-003).
- Must utilize Java 21 and Spring Boot 3 (PROJECT_MANIFEST.md).
- Must adhere exclusively to the Stitch "Career Operating System" Design System (Light/Dark mode, Inter typography).

## 14. Assumptions
- The AI layer (Spring AI) will handle natural language generation for Reasoning Cards based strictly on deterministic data provided by the Rule Engine.
- The platform will not act as a traditional social network (no public feeds).

## 15. Risks
- **AI Hallucination**: AI might invent evidence if the prompt engineering is not strictly bounded by the Rule Engine data.
- **Cold Start Problem**: Generating accurate AI Reasoning Cards requires a high initial volume of Evidence per user.

## 16. Out of Scope
- Real-time event delivery/webhook backbone (Deprecated by FDR-001).
- Social networking features (feeds, likes, comments).
- Authentication and identity provider implementation (Assumed as infrastructure capability, to be specified during Architecture/Engineering phases).

## 17. Release Strategy
- **Phase 1 (MVP)**: Career Passport creation, Manual Evidence entry, Basic Rule Engine matching, and AI Reasoning Cards generation.

---

# Epics & Feature Specifications

## Epic 1: Career Passport Management
**Purpose**: Allow professionals to establish and maintain their core identity.
**Business Value**: Forms the foundational data layer for all downstream AI matching.

### Feature Specification 1.1: Profile Initialization
- **Purpose**: Create the base Career Passport.
- **Business Value**: Onboards users into the platform ecosystem.
- **Actors**: The Professional.
- **Preconditions**: User has successfully authenticated.
- **Triggers**: First-time login.
- **Inputs**: Name, Contact Email, Current Job Title.
- **Outputs**: Instantiated Career Passport.
- **Workflow**: 1) Authenticate. 2) Provide demographics. 3) System initializes Passport. 4) User views empty dashboard.
- **Business Rules**: Must link to a single unique Identity (email).
- **Constraints**: Follows privacy regulations (GDPR).
- **Validation Rules**: Email must be properly formatted and unique.
- **Success Criteria**: Passport created; user reaches dashboard.
- **Failure Scenarios**: Network timeout, duplicate email.
- **Edge Cases**: User aborts midway (state should not persist).
- **Dependencies**: Authentication Service.
- **Non-functional Expectations**: Profile generation under 500ms.

### Feature Specification 1.2: Skill Taxonomy Mapping
- **Purpose**: Normalize self-reported claims into the platform's standardized ontology.
- **Business Value**: Enables deterministic rule-engine matching.
- **Actors**: The Professional.
- **Preconditions**: Career Passport exists.
- **Triggers**: User attempts to add a skill.
- **Inputs**: User search query for a skill.
- **Outputs**: Mapped System Skill ID attached to the Passport.
- **Workflow**: 1) User types skill. 2) System suggests taxonomy matches. 3) User selects. 4) System links to Passport as Tier 1 claim.
- **Business Rules**: BR-01 (Defaults to Tier 1 Trust).
- **Constraints**: Cannot map to skills outside the system taxonomy.
- **Validation Rules**: Input string must resolve to a valid UUID in the taxonomy table.
- **Success Criteria**: Skill successfully mapped and visible.
- **Failure Scenarios**: Skill not found in taxonomy.
- **Edge Cases**: User attempts to add a skill they already mapped.
- **Dependencies**: Skill Taxonomy Graph.
- **Non-functional Expectations**: Typeahead search under 100ms.

## Epic 2: Evidence Integration & Verification
**Purpose**: Ingest the raw data that proves a user's skills.
**Business Value**: Differentiates the platform from traditional claim-based resumes.

### Feature Specification 2.1: Tiered Evidence Ingestion
- **Purpose**: Ingest data proving a skill and assign a Trust Tier.
- **Business Value**: Populates the data model required for the Readiness Score.
- **Actors**: The Professional, System Evaluator.
- **Preconditions**: User has mapped at least one Skill (Feature 1.2).
- **Triggers**: User clicks "Add Evidence".
- **Inputs**: Document, URL, or OAuth token.
- **Outputs**: Evidence record linked to Skill with explicit Trust Tier.
- **Workflow**: 1) User provides evidence. 2) System assesses source. 3) System assigns Trust Tier (1-5). 4) System links evidence to Skill.
- **Business Rules**: BR-01, BR-03.
- **Constraints**: File uploads limited to 5MB PDFs.
- **Validation Rules**: OAuth tokens must be valid; URLs must be reachable.
- **Success Criteria**: Evidence mapped, Trust Score updated.
- **Failure Scenarios**: Invalid API token, unreachable URL.
- **Edge Cases**: Source goes offline after ingestion.
- **Dependencies**: Feature 1.2, External APIs (GitHub, Credly).
- **Non-functional Expectations**: Background processing for external API validation.

## Epic 3: AI Reasoning & Explainability Engine
**Purpose**: Generate transparent explanations for why a user matches a Mission.
**Business Value**: Builds trust and fulfills the "Explainability First" product principle.

### Feature Specification 3.1: Evidence-Based AI Reasoning Cards
- **Purpose**: Provide explainable matching without certifying absolute truth.
- **Business Value**: Differentiates platform through radical transparency.
- **Actors**: The Professional.
- **Preconditions**: User has a Readiness Score for a Mission.
- **Triggers**: User views a Mission Card.
- **Inputs**: Mission Requirements, User Evidence array.
- **Outputs**: AI text summary and raw evidence points.
- **Workflow**: 1) Rule engine calculates score. 2) Generates summary text. 3) User views summary. 4) User clicks to expand raw data.
- **Business Rules**: BR-03 (Must not certify truth), BR-02.
- **Constraints**: Must fit within Stitch Card UI dimensions.
- **Validation Rules**: Every claim in the summary MUST link to a specific Evidence ID.
- **Success Criteria**: Card renders correctly; expansion shows accurate data.
- **Failure Scenarios**: AI times out, degrading gracefully to raw data points only.
- **Edge Cases**: Massive amount of evidence requires pagination within the card.
- **Dependencies**: Feature 2.1, Generative AI service.
- **Non-functional Expectations**: Real-time rendering; AI text pre-computed asynchronously.

## Epic 4: Mission Recommendations
**Purpose**: Deliver relevant opportunities to the professional.
**Business Value**: The primary conversion mechanism of the platform.

### Feature Specification 4.1: Personalized Mission Dashboard
- **Purpose**: Display categorized opportunities optimizing for trajectory and readiness.
- **Business Value**: Retains user engagement and provides clear growth paths.
- **Actors**: The Professional.
- **Preconditions**: Career Passport initialized.
- **Triggers**: User navigates to Dashboard.
- **Inputs**: User's 11-point Mission Intelligence Score.
- **Outputs**: Ranked list of Missions categorized by Readiness.
- **Workflow**: 1) System fetches available Missions. 2) Calculates scores. 3) Categorizes (Ready Now/Soon/Future). 4) Generates Gap Analysis for "Soon/Future". 5) Renders Dashboard.
- **Business Rules**: BR-04, BR-05. Application button disabled unless threshold is met.
- **Constraints**: Maximum of 20 Missions rendered per page.
- **Validation Rules**: Score must explicitly include Trust Tiers and Behavioral data.
- **Success Criteria**: User sees categorized list with accurate Gap Analysis.
- **Failure Scenarios**: Score calculation fails; falls back to static list.
- **Edge Cases**: Zero Missions available (Empty State handling).
- **Dependencies**: Feature 3.1, Mission Database.
- **Non-functional Expectations**: Dashboard load < 300ms.

---

# User Stories & Acceptance Criteria

### Derived from Epic 1
**US-001 (Feature 1.1): Account Registration**
- **As a** Professional, **I want** to create my base Career Passport, **So that** I can begin using the platform.
- **Acceptance Criteria**: Form requires Name and Email. Successful submission creates the Passport.

**US-002 (Feature 1.1): View Empty Dashboard**
- **As a** Professional, **I want** to see an empty state dashboard on my first login, **So that** I know what actions to take next.
- **Acceptance Criteria**: UI clearly indicates "0 Skills Mapped" and prompts the user to add skills.

**US-003 (Feature 1.2): Search Taxonomy**
- **As a** Professional, **I want** to search for my skills using a typeahead field, **So that** I can map my claims to the system taxonomy.
- **Acceptance Criteria**: Typing 3 characters triggers a taxonomy search. Results are displayed in a dropdown.

**US-004 (Feature 1.2): Map Claimed Skill**
- **As a** Professional, **I want** to select a skill from the dropdown, **So that** it is added to my Passport.
- **Acceptance Criteria**: Selection adds the skill with a default "Tier 1: Claimed" Trust Level.

### Derived from Epic 2
**US-005 (Feature 2.1): Upload Manual Evidence**
- **As a** Professional, **I want** to upload a PDF certificate, **So that** I can back up my claimed skill.
- **Acceptance Criteria**: System accepts PDF (<5MB), assigns it Tier 1, and links it to the selected skill.

**US-006 (Feature 2.1): Connect OAuth Source**
- **As a** Professional, **I want** to link my GitHub account, **So that** my coding skills are automatically verified.
- **Acceptance Criteria**: Successful OAuth flow assigns Tier 3 (Platform Verified) to the ingested repositories.

### Derived from Epic 3
**US-007 (Feature 3.1): View Reasoning Summary**
- **As a** Professional, **I want** to read a 2-sentence summary of why I was matched to a Mission, **So that** I understand the AI's confidence.
- **Acceptance Criteria**: Summary explicitly uses confidence terminology (e.g., "85% confidence").

**US-008 (Feature 3.1): Expand Raw Data Evidence**
- **As a** Professional, **I want** to click the Reasoning Card to see the underlying data, **So that** I can verify the AI's logic.
- **Acceptance Criteria**: Click expands card to reveal a paginated list of Evidence records used in the calculation.

### Derived from Epic 4
**US-009 (Feature 4.1): View Categorized Missions**
- **As a** Professional, **I want** to see Missions divided into "Ready Now", "Ready Soon", and "Future Aspiration", **So that** I understand my current standing.
- **Acceptance Criteria**: Dashboard contains 3 distinct visual sections (or tabs) grouping the Missions.

**US-010 (Feature 4.1): View Mission Gap Analysis**
- **As a** Professional, **I want** to see a Gap Analysis for "Ready Soon" Missions, **So that** I know exactly what to do to unlock them.
- **Acceptance Criteria**: Mission Card lists missing competencies and suggests actions (e.g., "Upload Tier 3 Evidence for Kubernetes").
