# Career Intelligence Framework (CIF)

**Document ID:** CIF-001
**Document Type:** Domain Framework
**Status:** Proposed
**Version:** 1.0
**Owner:** Principal Domain Architect
**Governed By:** Framework Governance Model (FGM-001), Founding Charter (FC-001)

---

## 1. Purpose
The Career Intelligence Framework (CIF) establishes the foundational business domain for ProjectEcho. To build a coherent, explainable system, the entire organization—founders, designers, engineers, and AI assistants—must share a Ubiquitous Language. The CIF defines exactly what "Career Intelligence" is conceptually, ensuring that all downstream architectural and implementation decisions perfectly align with the business reality they exist to support.

## 2. Scope
This framework strictly governs:
- **Domain Vocabulary:** The exact terminology used throughout the organization and codebase.
- **Core Concepts:** The fundamental entities that comprise the Career Intelligence domain.
- **Domain Relationships:** How business entities interact logically.
- **Evidence Philosophy:** The rules defining what constitutes proof of capability.
- **Career Intelligence Principles:** The strategic goals of the domain.
- **Semantic Boundaries:** The limits of what the domain encompasses.

## 3. Out of Scope
This framework does **not** govern or discuss:
- Database schemas or storage engines.
- REST APIs, GraphQL, or network protocols.
- Microservices, event buses (e.g., Kafka), or modular monolith boundaries.
- Programming languages (e.g., Java, Python), frameworks (e.g., Spring Boot), or infrastructure.
- User Interface (UI) or User Experience (UX) wireframes.
- Any implementation details.

---

## 4. Vision of Career Intelligence
**Career Intelligence** is the paradigm shift from self-reported claims to evidence-backed reality. 

Traditional resumes are static, keyword-stuffed documents optimized for applicant tracking systems (ATS) rather than human truth. Skill checklists and certificates represent *completion*, not *competency*. Job portals match keywords without understanding context. 

Career Intelligence replaces this static model with a dynamic, evidence-driven, and continuously evolving profile. It does not ask a person what they know; it evaluates what they have verifiably done. It uses explainable insights to determine a person's true readiness for a mission, bridging the gap between historical experience and future potential.

## 5. Guiding Principles
- **Evidence Over Claims:** A skill without supporting evidence is a hypothesis, not a fact.
- **Competency Over Completion:** Finishing a course matters less than demonstrating the acquired capability in practice.
- **Explainability:** No black-box AI magic. Every recommendation or readiness score must be transparently traceable to its underlying evidence.
- **Continuous Evolution:** A career is a living timeline. The intelligence model must adapt to fresh signals continuously.
- **Context Over Keywords:** A skill (e.g., "Python") means entirely different things in the context of "Data Science" versus "Backend Web Development." Context is mandatory.
- **Human-Centered AI:** Intelligence exists to empower the Person to make informed decisions, not to automate them away.

---

## 6. Core Domain Concepts

- **Person:** The primary actor. The human professional whose career is being modeled.
- **Career Passport:** The holistic, living, evidence-backed representation of a Person's professional timeline, capabilities, and readiness. It replaces the traditional resume.
- **Mission:** A specific, defined professional objective or role (e.g., "Lead a migration to a new architecture"). It is what a Person aims to achieve.
- **Competency:** A proven, generalized ability to successfully execute tasks within a specific domain.
- **Skill:** A specific, granular tool, technique, or practice (e.g., "React.js", "Conflict Resolution").
- **Capability:** The intersection of Skill and Competency; the functional ability to apply a skill to achieve an outcome.
- **Evidence:** Verifiable proof that a Person has demonstrated a capability. 
- **Project:** A bounded professional endeavor that generates Evidence.
- **Experience:** The cumulative historical application of capabilities over time.
- **Learning:** The process of acquiring new capabilities, which generates weak evidence until applied in a Project.
- **Assessment:** A formal evaluation of a Capability.
- **Recommendation:** An explainable suggestion provided to the Person (e.g., a Mission to pursue or a Skill to learn).
- **Career Goal:** The long-term aspirational target of the Person.
- **Career Path:** The sequence of Missions required to reach a Career Goal.
- **Opportunity:** An external opening to fulfill a Mission.
- **Signal:** Raw, unverified data indicating potential professional activity (e.g., a GitHub commit, a calendar meeting). Signals become Evidence once verified and contextualized.
- **Confidence:** The systemic certainty that a Capability is truly possessed, derived from the quality of Evidence.
- **Readiness:** The aggregate measure of how prepared a Person is to successfully execute a specific Mission.
- **Gap:** The delta between a Person’s current Capabilities and the Capabilities required for a Mission.
- **Reflection:** A Person’s contextual self-evaluation of an Experience, providing qualitative Evidence.
- **Insight:** A system-generated observation about a Person's career trajectory or capabilities.
- **Knowledge:** Theoretical understanding of a domain, distinct from the practical application (Capability).
- **Context:** The environmental, cultural, and organizational circumstances surrounding an Experience or Evidence.

---

## 7. Domain Relationships
Concepts in Career Intelligence interact through logical associations:
- A **Person** owns a **Career Passport**.
- **Signals** are processed into **Evidence**.
- **Evidence** validates **Skills** and **Knowledge**.
- The proven application of **Skills** and **Knowledge** forms **Capability**.
- Consistent demonstration of **Capabilities** establishes **Competency**.
- A **Mission** requires specific **Capabilities**.
- The intersection of a Person's **Competencies** and a Mission's requirements determines **Readiness**.
- Missing Capabilities define the **Gap**.
- The system evaluates the **Gap** to generate an explainable **Recommendation**.
- A **Project** provides the **Context** that generates new **Signals**.

---

## 8. Evidence Model
Evidence is the atomic unit of truth in Career Intelligence. Without it, the system cannot function.
- **What Qualifies:** Any verifiable artifact or peer validation demonstrating the application of a capability (e.g., a merged pull request, a published design, a peer review).
- **Strong Evidence:** High provenance, recent, peer-validated, applied in a real-world Project.
- **Weak Evidence:** Self-reported claims, theoretical course completions, highly aged artifacts.
- **Confidence:** The systemic trust in a claim, which scales directly with the strength of the evidence.
- **Freshness:** Evidence decays over time. A skill demonstrated five years ago carries lower confidence today unless refreshed.
- **Provenance:** The verifiable origin of the evidence. Was it automatically ingested from a trusted source, or manually entered?

## 9. Competency Model
The CIF distinguishes clearly between knowing and doing:
- **Knowledge:** I understand how a bicycle works. (Theoretical)
- **Skill:** I know how to pedal, balance, and steer. (Granular)
- **Capability:** I can ride a bicycle down a mountain trail safely. (Applied)
- **Competency:** I am a skilled mountain biker capable of navigating various terrains. (Generalized)
- **Experience:** I have ridden mountain bikes for three years across ten different trails. (Historical Context)
- **Achievement:** I won the regional mountain biking championship. (Outcome)

## 10. Career Passport
The **Career Passport** is the ultimate output of the CIF. 
- **Purpose:** To provide a portable, undeniable proof of a professional's capabilities.
- **Contents:** The aggregated sum of Evidence, Competencies, Readiness, and Career Goals.
- **Ownership:** The Passport is owned by the Person, not the employer.
- **Evolution:** It is a living entity that grows dynamically as new Signals are processed into Evidence.
- **Explainability:** Every claim on the Passport can be expanded to reveal the exact Evidence supporting it.

## 11. Recommendation Philosophy
When the system suggests a Mission or Learning path to a Person, the recommendation must be:
- **Evidence-Backed:** Rooted in the Person's verified Passport.
- **Explainable:** Accompanied by a clear "Why we recommended this" statement based on current Capabilities and Gaps.
- **Goal-Aware:** Aligned with the Person's stated Career Goal.
- **Context-Sensitive:** Factoring in the Person's current organizational and environmental context.

## 12. Readiness Model
Readiness is not a boolean; it is a spectrum of preparedness for a specific Mission.
- **Readiness:** The percentage match between possessed Capabilities and required Capabilities.
- **Gap:** The explicit list of missing Capabilities preventing 100% Readiness.
- **Potential:** The likelihood a Person can rapidly close a Gap based on adjacent Competencies.
- **Risk:** The probability of failure in a Mission due to critical Gaps.

---

## 13. Domain Constraints
To maintain the integrity of the domain, the following business constraints are absolute:
1. **No Unsupported Claims:** A capability with zero evidence must be explicitly marked as "Self-Reported/Unverified."
2. **Evidence Never Disappears:** Evidence may decay in freshness, but historical proof is never deleted.
3. **Traceability:** Every Readiness score or Recommendation must mathematically and logically trace back to Evidence.

## 14. Domain Boundaries
ProjectEcho is **NOT**:
- An **Applicant Tracking System (ATS)** (We do not manage hiring pipelines).
- A **Learning Management System (LMS)** (We do not host courses).
- A **Job Board** (We match capabilities to Missions, we do not host classifieds).
- A **Social Network** (We are an intelligence platform, not a newsfeed).
- A **General AI Assistant** (We do not answer general trivia; we answer career-centric questions).

---

## 15. Domain Glossary
*This glossary serves as the Ubiquitous Language for all engineering and product discussions.*

- **Capability:** The applied execution of a Skill.
- **Career Passport:** The living, evidence-backed professional profile.
- **Competency:** A generalized, proven ability in a domain.
- **Confidence:** The systemic trust in a piece of Evidence.
- **Evidence:** Verifiable proof of a Capability.
- **Gap:** The missing Capabilities required for a Mission.
- **Mission:** A specific professional objective.
- **Person:** The human professional.
- **Readiness:** The measure of preparedness for a Mission.
- **Signal:** Raw data that can be processed into Evidence.
- **Skill:** A granular tool or technique.

---

## 16. Future Architecture Guidance
This framework sets the conceptual boundaries of the system. Future engineering documentation must align perfectly with this language:
- The **Engineering Architecture Framework (EAF)** will define the technical principles required to build this exact domain model.
- The **Engineering Architecture Document (EAD)** will define the bounded contexts and modules (e.g., an `evidence-engine` module or a `passport-service` module) that realize these concepts.
- **Implementation** must map classes and data structures directly to the Domain Glossary (e.g., `class CareerPassport`, `class Evidence`, `class Gap`). 

The CIF remains entirely technology-independent. If the technology stack changes from Java to Go, the CIF does not change.
