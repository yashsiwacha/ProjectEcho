# EDF-0005: Testing Strategy

**Document ID:** EDF-0005
**Status:** FROZEN
**Version:** 1.0

---

## 1. Testing Pyramid

### 1.1. Unit Tests (Base)
- **Scope**: Domain entities, value objects, domain services, and application use cases.
- **Frameworks**: JUnit 5, Mockito, AssertJ.
- **Coverage Goal**: 90% instruction coverage for `domain` packages.

### 1.2. Integration Tests (Middle)
- **Scope**: Repository queries (Testcontainers with PostgreSQL), REST controllers (`MockMvc`), and Event Listeners (`@SpringBootTest`).
- **Data Initialization**: Use Flyway to initialize schemas in Testcontainers. Never use `spring.jpa.hibernate.ddl-auto=create`.
- **Event Validation**: Spring Modulith's `@ApplicationModuleTest` or Spring's `@RecordApplicationEvents` must be used to verify that cross-module events are published correctly upon transaction commits.

### 1.3. Architecture Tests (Top)
- **Scope**: Global enforcement of EDF-0004 dependency rules and layered architecture.
- **Framework**: ArchUnit.
- **Execution**: Runs during the standard `test` phase.

## 2. Contract Testing
- Given the monolithic nature, internal API boundaries are enforced via ArchUnit rather than Pact.
- **External Contracts**: Integrations with external Identity Providers or Credential Issuers (FDR-002) must use WireMock for simulating external APIs.

## 3. Performance & Quality Gates
- **Static Analysis**: SonarQube ruleset configured to fail the build on critical vulnerabilities or technical debt exceeding 5%.
- **Performance**: Given NFR-02 (sub-200ms latency), integration tests covering the Mission Dashboard Read Model (`echo-mission`) should include JMH benchmarks or Spring StopWatch metrics to validate data retrieval latency against the materialized view.
