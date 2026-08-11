# Project Echo Agent Rules (`AGENTS.md`)

This document defines the Global Operating Principles for the Project Echo Autonomous AI Software Engineering Organization.

## 1. Organization Structure & Workflow
Project Echo is managed by specialized, autonomous offices. 
The **Delivery Office** is the central orchestrator. No feature is implemented without following the Default Execution Workflow.

**Workflow Sequence**:
1. Project Resume (Delivery Office)
2. Delivery Review
3. Product Review (`product-office`)
4. Architecture Review (`architecture-office`)
5. GitHub Review & Stitch Review
6. **[APPROVAL GATE] Founder Approval**
7. Backend / Frontend Implementation
8. **Engineering Excellence Engine (EEE) Audit** (Executed automatically via `yes excellence review` across all 8 rubrics: Architecture, Design, Code Quality, Security, Performance, Testing, Documentation, and Refactoring)
9. Final EEE Metrics update and report generation
10. Final Validation & **Founder Acceptance**

*No office may skip another office. Every implementation must satisfy EEE baseline constraints.*

## 2. Global Engineering Rules
Always enforce:
- Clean Architecture
- Domain-Driven Design (DDD)
- SOLID principles
- DRY (Don't Repeat Yourself)
- KISS (Keep It Simple, Stupid)
- OWASP Security standards
- Testability & Maintainability
- Explainability & Documentation

## 3. Project Governance
Project Echo follows a strict documentation-first engineering process.
- **Sources of Truth**: Founder Decisions, PRDs, ADRs, Stitch MCP, GitHub.
- Implementation **must always** align with all of these sources. If conflicts exist, prioritize Founder Decisions and request clarification.

## 4. MCP Integration
### GitHub MCP
- Use for Issues, Milestones, PRs, Branches, and Execution Tracking.
- Never create duplicate Issues or duplicate Pull Requests.
- Ensure PR descriptions comprehensively detail implemented Epics, Features, and affected modules.
- **MANDATORY GITHUB WORKFLOW**: To maintain a balanced contribution graph and follow industry standards, all development must follow this strict sequence:
  1. **Issue**: Create a GitHub Issue for the task before writing any code.
  2. **Branch**: Create and checkout a new feature/bugfix branch from `main`.
  3. **Commit**: Make logical commits to the isolated branch.
  4. **Pull Request**: Open a Pull Request against `main` and explicitly link the issue (e.g., "Closes #123").
  5. **Review & Merge**: Trigger the `coderabbit-reviewer` skill and merge the PR only when all quality gates pass.
  *Direct commits to the `main` or `master` branch are strictly prohibited.*

### Stitch MCP
- Treat Stitch as the authoritative source for UI, UX, Components, Design Tokens, Flows, and Accessibility.
- Never invent UI when a Stitch design exists.
- Any intentional deviations must be explicitly documented.

## 5. Approval & Quality Gates
**Founder approval is STRICTLY REQUIRED for:**
- PRD Freeze
- Architecture Freeze
- Major ADRs
- Database redesigns
- Breaking APIs
- Security model changes
- Authentication/Authorization
- Release Candidates and Production Deployments

**No Feature is complete until:**
- PRD satisfied, Founder Decisions satisfied, ADRs respected.
- Architecture compliant and Stitch compliant.
- Tests passing, Security passed, Documentation updated, and GitHub synchronized.

## 6. Centers of Excellence (CoE) — Founder Directive FD-0013

The following external CoEs are permanently adopted as part of the Engineering Constitution.
All evaluation gates are **mandatory** and must be completed before any implementation begins.

### 6.1 Frontend Feature Evaluation Gate
Every **frontend feature** must evaluate all five dimensions before implementation:

| CoE | Reference | Responsibility |
|-----|-----------|----------------|
| **Motion Engineering** | Motion.dev, Motion AI Kit | Animations, page transitions, shared-layout, gestures, scroll-linked animation, spring physics, exit animations, micro-interactions, `prefers-reduced-motion` compliance |
| **Design System** | KokonutUI | Premium component design, design tokens, Tailwind architecture, loading/empty/error states, dark mode, WCAG compliance |
| **Accessibility** | WCAG 2.1 AA | ARIA roles, keyboard navigation, focus management, screen-reader compatibility |
| **Data Visualization** | Bklit UI | Dashboard design, charts, KPIs, analytics, real-time data, executive reporting |
| **Performance** | Lighthouse, Web Vitals | LCP ≤ 2.5 s, CLS < 0.1, INP < 200 ms, code-splitting, critical CSS |

**Mandatory Rules:**
- Every frontend feature must evaluate whether meaningful motion improves usability.
- Animations must never reduce accessibility or performance.
- Do not build custom components if an existing design-system pattern satisfies the requirement.
- New components must be reusable.
- Every dashboard must expose meaningful business KPIs.
- Charts must remain accessible and responsive.

### 6.2 Platform Feature Evaluation Gate
Every **platform / backend feature** must evaluate all five dimensions before implementation:

| CoE | Reference | Responsibility |
|-----|-----------|----------------|
| **Autonomous AI** | Manus | Autonomous planning, multi-step execution, research workflows, long-running orchestration, agent collaboration, self-verification |
| **Workflow Optimisation** | — | Reduce manual steps; automate repeatable processes |
| **Reusability** | — | Extract shared modules; never duplicate domain logic |
| **Developer Experience** | — | Fast local setup, clear APIs, self-documenting code |
| **Maintainability** | — | Clean Architecture, SOLID, DRY, test coverage ≥ 90 % |

**Mandatory Rules:**
- Every large Founder request must first be decomposed into executable work packages before implementation begins.
- Use AI automation only where it improves understanding, speed, or quality — never for decoration.

### 6.3 Continuous Learning Protocol
When a new pattern, component, animation technique, interaction model, dashboard design, or AI workflow is discovered from these ecosystems:
1. Evaluate it against existing company standards.
2. Extract reusable knowledge.
3. Store it in the Knowledge Platform.
4. Update templates where appropriate.
5. Never duplicate functionality; never copy blindly — adapt patterns to the YES architecture.

*Reference: `decisions/ADR-003-CoE.md`

## 7. Engineering Intelligence Platform — Founder Directive FD-0014

Project Echo enforces a continuous learning constraint. The organization must compound its capabilities after every Founder request.

### 7.1 The Continuous Learning Constraint
- **Mandatory Reusable Output:** No engineering task, bug fix, or workspace modification may be marked complete or merged without producing or updating at least one reusable knowledge artifact in `EOS/Intelligence/`.
- **Allowed Artifact Types:**
  - **New Pattern:** A documented design or logic pattern in `PatternLibrary/` (e.g. `PatternLibrary/authentication/JWT_PATTERN.md`).
  - **New Component:** An extracted or custom UI component in `DesignSystem/COMPONENT_CATALOGUE.md`.
  - **New Motion Primitive:** An animation config or primitive in `Motion/PRIMITIVES.md`.
  - **New Playbook:** An AI or UX operational guide in `AIPlaybooks/` or `UXPlaybooks/`.
  - **New Benchmark:** A completed scorecard in `Benchmarking/` following `BENCHMARK_ENGINE.md` rules.
  - **New Template:** A standard boilerplate in `EOS/Templates/`.
- **Pre-Implementation Verification:** Every agent/engineer must search `EOS/Intelligence/PatternLibrary/` for existing solutions before writing code. Reuse existing patterns; do not duplicate functionality.
- **Workflow Hook:** The Review Office and Delivery Office will audit task deliverables against this constraint. Non-compliant tasks will be rejected.

