# Engineering Playbook

This playbook provides standard operating procedures for the ProjectEcho Engineering Organization.

## 1. Introducing a New Bounded Context
When domain logic expands beyond existing boundaries, follow this process:
1. **Domain Language Review**: Scan the PRD/CIF for explicit new ubiquitous language.
2. **Architecture Review (ARBR)**: Present the proposed boundary, identifying the Aggregate Roots, inbound commands, and outbound Domain Events.
3. **Founder Approval**: Major structural additions require formal approval.
4. **Scaffolding**: Create the module (`echo-module-name`), update root `pom.xml`, update `echo-application/pom.xml`, and register the context in `ArchitectureTest.java`.
5. **Implementation**: Ensure strict adherence to Hexagonal Architecture (Domain -> Application -> Infrastructure -> Presentation).

## 2. Aggregate Review Workflow
Before finalizing an Aggregate Root:
- Identify invariants and illegal state transitions.
- Ensure all business logic resides within the Aggregate (or Domain Services), not Application Services.
- Ensure all side effects are expressed purely as `IntegrationEvent` or `DomainEvent` emissions.

## 3. Cross-Council Review Process
All major pull requests or implementation waves must pass Cross-Council Review:
- **Architecture**: Validates boundaries and dependencies.
- **Domain Modeling**: Validates naming conventions.
- **Red Team (Security/Governance)**: Actively attempts to break the assumptions, finding edge cases in PII, threading, or terminology drift.
- **Minority Opinions**: If a council dissents, the disagreement is recorded in a Conflict Register (e.g., `CONFLICT_REGISTER.md`) for Founder resolution.

## 4. Milestone Lifecycle
1. **Planning**: Define the exact architectural and business deliverables.
2. **Execution (Waves)**: Divide the milestone into manageable implementation waves.
3. **Hardening**: Run a dedicated sprint focusing solely on refactoring, testing, and technical debt reduction.
4. **Review**: Submit a milestone review package.
5. **Approval**: Await Founder sign-off before proceeding to the next milestone.

## 5. Release Readiness Checklist
- `mvn spotless:apply clean verify` passes.
- `REPOSITORY_SCORECARD.md` evaluates to >90%.
- Walkthrough documentation is generated.
- CodeRabbit Review is formally approved.
- Threat Model is verified.
