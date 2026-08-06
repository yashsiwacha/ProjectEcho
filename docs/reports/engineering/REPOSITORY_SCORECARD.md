# Repository Scorecard

This scorecard provides a repeatable metric for evaluating the health of the ProjectEcho repository. It should be evaluated continuously as part of the Improvement Framework.

## 1. Architecture Compliance
- [ ] ArchUnit tests pass with zero exceptions.
- [ ] No circular dependencies exist between bounded contexts.
- [ ] Bounded Contexts only communicate via `echo-shared` Domain Events.

## 2. Governance Compliance & ADR Coverage
- [ ] Every major architectural decision is backed by an ADR.
- [ ] Ubiquitous Language strictly mirrors the CIF-0001 definitions.
- [ ] No artifacts exist with "Draft" status that have been implemented in code.

## 3. Documentation Freshness
- [ ] `README.md` runs flawlessly on a fresh machine.
- [ ] `walkthrough.md` reflects the current sprint accurately.
- [ ] API Guides match actual code implementations.

## 4. Test Quality & Build Health
- [ ] Unit Test coverage on Domain Models is 100%.
- [ ] `mvn spotless:apply clean verify` passes in under 120 seconds.
- [ ] Static Analysis (PMD/SpotBugs/Checkstyle) reports zero warnings.

## 5. Technical Debt & Maintainability
- [ ] PMD / SpotBugs suppressions (`@SuppressWarnings`) are strictly documented and minimized.
- [ ] No deprecated APIs are in use.
- [ ] Naming conventions strictly adhere to Domain Driven Design over technical patterns (e.g., `Passport` over `UserDataTable`).

## 6. Security Posture
- [ ] Threat modeling documents are up to date.
- [ ] Secrets (if any) are injected via environment, never hardcoded.
- [ ] PII data access boundaries are respected by all modules.

## 7. Developer Onboarding
- [ ] A new engineer can commit code within 4 hours of cloning the repository.
- [ ] `ENGINEERING_PLAYBOOK.md` provides clear instructions for adding a new bounded context.
