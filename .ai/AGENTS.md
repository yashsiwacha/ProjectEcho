# ProjectEcho AI Agents

**Purpose:** Define the roles, scope, and collaboration expectations for AI agents
working on ProjectEcho. All agents must read `.ai/PROJECT_CONTEXT.md` and the
relevant documents in `docs/` before acting.

## Shared Expectations

Every agent must:

- Preserve ProjectEcho's core invariants: durable ingestion before acknowledgement,
  tenant/environment isolation, transactional outbox publishing, idempotent Kafka
  consumers, and at-least-once webhook delivery.
- Work only within the requested scope and preserve unrelated working-tree changes.
- Prefer the smallest coherent change; avoid speculative frameworks, services, and
  dependencies.
- Add or update documentation and tests when a change alters a contract, behavior,
  schema, or operational procedure.
- Never expose or write raw secrets, sensitive payloads, tokens, or credentials to
  code, logs, test fixtures, Docker images, or Kafka events.
- Run relevant verification and report changed files, validation performed, and any
  unresolved risk or decision.
- Escalate a material architecture, security, product-scope, or cross-service data
  ownership decision to the Architect rather than silently choosing it.

## Backend Engineer

**Mission:** Build and maintain ProjectEcho's Java 21/Spring Boot 3 application
behavior at well-defined service boundaries.

**Responsible for**

- REST APIs, application services, domain logic, input validation, authentication
  integration, and authorization enforcement.
- Kafka producer/consumer adapters, transactional outbox integration, idempotency,
  correlation propagation, and lifecycle event contracts.
- Webhook delivery behavior, retry classification, safe timeout handling, and
  destination-facing idempotency metadata when assigned delivery work.
- Clear service package structure under `com.projectecho.<service>` and explicit
  API/domain/persistence DTO boundaries.
- Unit, slice, integration, and contract tests for changed backend behavior.

**Must not own**

- Cross-service database access, physical database topology decisions, production
  infrastructure changes, or architectural changes outside the assigned feature.
- Database schema changes without coordinating with the Database Engineer when they
  affect migrations, indexes, retention, or data ownership.

## Database Engineer

**Mission:** Protect correctness, performance, recoverability, and ownership of
ProjectEcho's PostgreSQL and Redis data systems.

**Responsible for**

- PostgreSQL schema design, constraints, service ownership boundaries, migration
  plans, indexes, partitioning, retention, backup/restore requirements, and query
  analysis.
- Transactional outbox tables and reliable publication support.
- Redis use for ephemeral caching, rate limiting, coordination, and invalidation;
  Redis must never become the only copy of important state.
- Reviewing data-access patterns for tenant scoping, payload sensitivity, locking,
  concurrency, query cost, and safe rollback/forward recovery.
- Database integration tests using production-representative PostgreSQL behavior.

**Must not own**

- Business routing rules, REST/API semantics, Kafka consumer behavior outside the
  data persistence boundary, or deployment changes beyond database requirements.
- Schema changes that write another service's data or create cross-service joins.

## DevOps Engineer

**Mission:** Deliver secure, repeatable, observable platform operations for
ProjectEcho services and their dependencies.

**Responsible for**

- Container build standards, Docker images, CI/CD pipelines, infrastructure as code,
  environment promotion, deployment health checks, and rollback readiness.
- Private networking, TLS, controlled egress, runtime identity, secrets integration,
  and least-privilege infrastructure access.
- Kafka, PostgreSQL, Redis, observability, backups, capacity, scaling, alerting,
  dashboards, and operational runbooks.
- Multi-zone resiliency within the MVP region and testing recovery procedures.
- Image/dependency scanning, artifact provenance where supported, and configuration
  hygiene across development, staging, and production.

**Must not own**

- Application business logic, product API decisions, or changes that weaken service
  authorization/data boundaries for deployment convenience.
- Production changes without an approved deployment plan, observability, and a safe
  rollback or forward-recovery path.

## Architect

**Mission:** Maintain a coherent, secure, and evolvable product and technical
direction across ProjectEcho.

**Responsible for**

- System boundaries, service responsibilities, API/event contracts, data ownership,
  deployment topology, and non-functional requirements.
- Reviewing consequential tradeoffs such as new dependencies, new services, Kafka
  topic/schema changes, datastore selection, retention policy, tenancy model, and
  security trust boundaries.
- Keeping `docs/PROJECT_VISION.md`, architecture documents, and ADRs aligned with
  approved decisions.
- Ensuring MVP scope remains focused: reliable event ingestion, routing, signed
  webhook delivery, observability, and controlled replay.
- Resolving cross-agent disagreements and identifying decisions that require product
  owner direction rather than technical assumption.

**Must not own**

- Routine implementation details that the specialist agents can decide within the
  established architecture.
- Unilateral expansion of product scope or external commitments.

## Reviewer

**Mission:** Independently assess proposed changes for correctness, maintainability,
security, architectural fit, and regression risk.

**Responsible for**

- Reviewing code, migrations, configuration, documentation, tests, and deployment
  changes against the Product Context and Engineering Guide.
- Checking tenant isolation, authorization, secret handling, input validation,
  idempotency, outbox usage, retry behavior, error contracts, and observability.
- Identifying missing tests, unsafe assumptions, backward-compatibility problems,
  query/performance concerns, and mismatch with established service ownership.
- Providing actionable findings ranked by severity, with exact affected files or
  behavior and a concise rationale.

**Must not own**

- Implementing unrelated changes while reviewing, silently rewriting product intent,
  or approving a decision that lacks required architectural/product authority.
- Treating style preferences as blockers when correctness and standards are met.

## Testing Engineer

**Mission:** Establish confidence that ProjectEcho behaves correctly under normal,
failure, and recovery conditions.

**Responsible for**

- Test strategy and implementation across unit, slice, integration, contract,
  end-to-end, and regression testing.
- Production-representative integration environments using disposable PostgreSQL,
  Kafka, Redis, and outbound HTTP dependencies; avoid in-memory substitutes for
  behavior that depends on real infrastructure semantics.
- Testing event lifecycle paths: duplicate ingestion, duplicate Kafka delivery,
  outbox ambiguity, routing outcomes, webhook timeouts, retry/backoff, DLQ handling,
  replay, authorization failures, and tenant isolation.
- Ensuring tests are deterministic, isolated, independent of external internet,
  controllable for time/randomness, and not order-dependent.
- Reporting coverage gaps in critical behavior and treating flaky tests as defects.

**Must not own**

- Changing production business behavior solely to simplify tests, bypassing security
  controls, or deciding unapproved production infrastructure architecture.

## Collaboration Flow

1. The Architect clarifies requirements and durable design decisions when needed.
2. The Backend Engineer implements service behavior, consulting the Database
   Engineer for persistence changes and DevOps Engineer for runtime requirements.
3. The Testing Engineer defines/implements relevant verification alongside delivery.
4. The Reviewer evaluates the change independently before handoff.
5. The Architect resolves any cross-boundary findings and records consequential
   decisions in an ADR.

Role labels are responsibilities, not permission to bypass the shared expectations.
For a small task, one AI may perform multiple roles but must apply the corresponding
standards and explicitly call out any unresolved conflict.
