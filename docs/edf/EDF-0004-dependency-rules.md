# EDF-0004: Dependency Rules (ArchUnit)

**Document ID:** EDF-0004
**Status:** FROZEN
**Version:** 1.0

---

## 1. ArchUnit Enforcement Strategy
The Modular Monolith boundaries will be rigorously enforced via compile-time ArchUnit tests located in `echo-application/src/test/java/com/projectecho/architecture/`.

## 2. Module Boundary Rules
The following dependency matrix will be codified as ArchUnit slices:

- **`echo-shared`**: May not depend on any other module.
- **`echo-identity`**: May only depend on `echo-shared`.
- **`echo-taxonomy`**: May only depend on `echo-shared`.
- **`echo-evidence`**: May depend on `echo-identity` (to resolve PassportId) and `echo-taxonomy` (to resolve SkillId).
- **`echo-intelligence`**: May depend on `echo-evidence`, `echo-taxonomy`, and `echo-mission` (only to reference the target Read Model ID for the UI).
- **`echo-mission`**: May depend on `echo-identity`.

## 3. Layer Architecture Rules (Hexagonal)
Within every module, ArchUnit will enforce the following layering:

1. **Domain Layer (`domain..`)**:
   - MUST NOT depend on `application`, `infrastructure`, or `presentation`.
   - MUST NOT depend on Spring Framework annotations (except `@Component` equivalents if explicitly authorized).
2. **Application Layer (`application..`)**:
   - MAY depend on `domain`.
   - MUST NOT depend on `infrastructure` or `presentation`.
3. **Infrastructure Layer (`infrastructure..`)**:
   - MAY depend on `domain` and `application`.
   - Contains all Spring Data JPA interfaces and external REST clients.
4. **Presentation Layer (`presentation..`)**:
   - MAY depend on `application`.
   - MUST NOT depend directly on `domain` aggregates. Communication is strictly via Application Use Cases and DTOs.
