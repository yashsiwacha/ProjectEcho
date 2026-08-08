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
8. Testing (`qa-office`) & Security Review (`security-office`)
9. Documentation (`documentation-office`)
10. Code Review (`review-office`)
11. Final Validation & **Founder Acceptance**

*No office may skip another office.*

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
