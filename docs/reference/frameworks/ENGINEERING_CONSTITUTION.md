# ProjectEcho Engineering Constitution

## 1. Preamble
This Constitution establishes the absolute engineering laws for ProjectEcho. It supersedes all individual team preferences and defines the operational mechanics of the autonomous engineering organization to ensure longevity, maintainability, and architectural purity.

## 2. Engineering Principles
- **Governance Precedes Implementation**: Code is the byproduct of governance. No feature shall be implemented without a governed Architectural Decision Record (ADR) or Product Requirements Document (PRD).
- **Ubiquitous Language Primacy**: Code must mathematically reflect the domain language (e.g., CIF-0001). Divergence is a critical architectural violation.
- **Explainability by Default**: All decisions (especially AI or rule-based) must produce an auditable trace (e.g., `DecisionGraph`).

## 3. Council Responsibilities
- **Architecture Council**: Owns the modular monolith boundaries, event catalog, and cross-context interactions.
- **Domain Modeling Council**: Owns the fidelity of the Ubiquitous Language and the integrity of Aggregate Roots.
- **Security & Red Team**: Owns threat modeling, PII isolation, and deliberate adversarial testing of governance gaps.
- **Developer Experience (DevEx) & Platform**: Owns CI/CD, local tooling, and the friction-free scaling of the repository.

## 4. Decision Authority & Escalation
- Routine implementations are governed by the respective Councils.
- **Founder Approval Gates**: The Founder retains absolute veto power and mandatory approval authority for:
  - Bounded Context extraction/creation.
  - Changes to the Framework Governance Model (FGM).
  - Data privacy/tenancy architecture shifts.
- **Escalation Process**: Disagreements between Councils trigger an ARBR (Architecture Review Board Report) presented to the Founder for binding resolution.

## 5. Review Workflow & Definition of Done
- **Definition of Done**: 
  - Code compiles without static analysis warnings (PMD, SpotBugs).
  - ArchUnit boundaries are unbroken.
  - ADRs and Walkthroughs are updated.
  - `CodeRabbit` review is green.
- **Refactoring Policy**: Refactoring is continuous. The codebase must be left cleaner than it was found. Technical debt is tracked formally in the `REPOSITORY_SCORECARD`.
- **Documentation Synchronization**: Code and documentation are one artifact. PRs failing to update corresponding `docs/` must be rejected.
