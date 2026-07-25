# ProjectEcho: AI Project Context

**Purpose:** Read this document before proposing or making changes in ProjectEcho.
It summarizes the product, intended architecture, technical standards, and working
constraints. Treat the architecture documents in `docs/architecture/` as the source
of truth for deeper detail.

## Product Goals

ProjectEcho is an event-driven platform that accepts authenticated events from
customer systems, records them durably, routes them to configured destinations, and
makes their full lifecycle observable.

The product exists to replace fragile, bespoke event integrations with a reliable
and understandable event backbone. A customer should be able to publish an event,
see which rules matched, determine every delivery outcome, recover from a downstream
failure, and govern access without managing the underlying plumbing.

The MVP proves this end-to-end workflow:

1. An authenticated producer submits a versioned JSON event.
2. ProjectEcho validates and durably records the event.
3. Routing evaluates active rules by event type and source.
4. A delivery worker sends signed webhook requests with durable retries.
5. Operators can inspect event/delivery state and authorize replay of eligible
   failed deliveries.

Initial target users are application developers, platform/DevOps engineers,
operations/support teams, and engineering leaders. The product is not an analytics
warehouse, primary transactional database, generic workflow builder, full
observability suite, or an exactly-once guarantee for external side effects.

## Tech Stack

| Area | Standard / intended choice |
| --- | --- |
| Language | Java 21 LTS |
| Application framework | Spring Boot 3 |
| External API | Versioned REST over HTTPS using JSON |
| Primary data store | PostgreSQL |
| Ephemeral cache/coordination | Redis or managed equivalent |
| Event backbone | Kafka; preserve topic contracts if MVP uses a managed queue first |
| Service runtime | Stateless, containerized services |
| Observability | OpenTelemetry with managed logs, metrics, and tracing backends |
| Secrets | Managed cloud key-management and secrets service |
| Delivery | Signed HTTPS webhooks; controlled outbound egress |
| Infrastructure | Infrastructure as code and CI/CD |

Use managed cloud equivalents when they meet the same reliability, security, and
operational requirements. Do not introduce a new framework, broker, datastore, or
cloud dependency without a documented reason and architectural review.

## Architecture

ProjectEcho separates synchronous acceptance from asynchronous delivery. An event is
acknowledged only after validation and durable persistence. Downstream destination
latency or failure must not block ingestion.

```text
Producer → API Gateway → Ingestion → Event Ledger + Outbox → Kafka → Routing
                                                                  │
                                                          Delivery Requests
                                                                  ▼
Operator → Control/Operations APIs ← Lifecycle Query ← Delivery → Webhook
                                      ▲              │
                                      └── Replay ────┘
```

Logical service boundaries are: Edge Gateway; Identity and Access; Configuration;
Event Ingestion; Routing; Delivery; Replay; Operations Query; and Audit and
Observability. The MVP can initially deploy related modules together, but code and
data ownership must respect these boundaries so services can be extracted later.

### Data and communication rules

- PostgreSQL is the durable source of truth. Redis is ephemeral and must never hold
  the only copy of important state.
- Each service owns its database/schema and is the sole writer. Never query or
  write another service's tables directly.
- Cross-service consistency uses local transactions plus a transactional outbox;
  never dual-write a database and Kafka from a request handler.
- Kafka delivers at least once. Consumers persist idempotency state keyed by
  `event_id`, then commit offsets only after their local transaction succeeds.
- Event records include tenant context, correlation ID, schema version, and
  causation information. Customer payloads and secrets are minimized in logs and
  Kafka records.
- Delivery is at-least-once and destinations must use the event ID for idempotency.
- Failed consumer processing uses bounded retry topics and a DLQ. Failed webhook
  delivery uses durable Delivery-service retry/dead-letter state and authorized
  replay, not a Kafka poison-message workflow.

See [System Overview](../docs/architecture/SYSTEM_OVERVIEW.md),
[Microservices](../docs/architecture/MICROSERVICES.md),
[Database Strategy](../docs/architecture/DATABASE.md), and
[Event Flow](../docs/architecture/EVENT_FLOW.md) before changing an architectural
boundary or event contract.

## Coding Conventions

- Use Java 21 and the current supported Spring Boot 3 release. Prefer clear,
  framework-independent domain/application logic over framework-heavy code.
- Use constructor injection. Keep controllers thin and put transaction boundaries
  on short application-service methods.
- Never make remote HTTP, Kafka, or blocking calls inside a database transaction.
- Validate every external input. JPA entities are persistence concerns and must not
  be used as REST or Kafka contract objects.
- Prefer immutable types and records where they express the domain well. Do not use
  nullable return values; use explicit validation/results or `Optional` for return
  values only.
- Use UTC `Instant` or `OffsetDateTime` for API and persisted timestamps.
- Structured JSON logs must include trace/correlation IDs where available. Never
  log secrets, authorization headers, raw API keys, cookies, or sensitive payloads.
- Use OpenTelemetry trace propagation over HTTP, Kafka, scheduled work, and outbound
  delivery calls.
- REST errors use a single RFC 9457 Problem Details-compatible shape with safe
  client detail, a stable machine-readable code, and correlation ID.
- Public REST APIs are versioned (`/v1/...`), JSON-based, authenticated, validated,
  documented with OpenAPI, and use cursor pagination for timelines.
- All external writes define idempotency. Ingestion idempotency is scoped to
  organization and environment.

## Naming Conventions

| Concern | Convention | Example |
| --- | --- | --- |
| Java base package | `com.projectecho` | `com.projectecho.delivery` |
| Service package | Lowercase singular service name | `com.projectecho.ingestion` |
| Feature structure | Feature first; `api`, `application`, `domain`, `infrastructure` within it | `com.projectecho.routing.rules.domain` |
| Java types | `PascalCase` nouns; verbs only for behavior | `DeliveryAttempt`, `RouteEvaluator` |
| Methods/fields | `camelCase`, intention revealing | `scheduleRetry`, `correlationId` |
| Constants | `UPPER_SNAKE_CASE` | `MAX_PAYLOAD_BYTES` |
| REST fields | `camelCase` | `eventId`, `occurredAt` |
| REST paths | Lowercase plural nouns | `/v1/events`, `/v1/deliveries` |
| Database tables/columns | `snake_case` | `delivery_attempt`, `occurred_at` |
| Kafka topic | `echo.<domain>.<event-name>.v<major>` | `echo.delivery.succeeded.v1` |
| Kafka key | Tenant by default; destination for delivery work | `organization_id`, `destination_id` |
| Docker image | Lowercase, versioned immutable tag | `projectecho/ingestion:<version>` |

Avoid generic packages such as `util`, `common`, and `manager`. Do not create a
shared library until there is a demonstrated multi-service need; shared libraries
are small, versioned, and free of accidental coupling.

## Development Workflow

1. Read the relevant architecture and product documents before changing behavior.
2. Check the working tree first. Preserve unrelated user changes and do not revert,
   overwrite, or reformat files outside the requested scope.
3. For a material design change, update the relevant documentation and create an ADR
   when the decision is durable, consequential, or has alternatives worth recording.
4. Make the smallest coherent change. Keep domain, API, persistence, and messaging
   contracts explicitly separated.
5. Add tests at the lowest meaningful level. Use unit tests for logic; use real,
   disposable PostgreSQL, Kafka, Redis, and HTTP dependencies for integration
   behavior. Test duplicate messages, retries, failure paths, DLQ transitions, and
   replay where relevant.
6. Run formatting, static analysis, relevant tests, and `git diff --check` before
   handoff. Report what was changed and what verification was run.
7. Do not commit, push, create pull requests, contact external systems, or perform
   destructive actions unless the user explicitly requests it.

## Constraints and Non-Negotiables

- Do not weaken tenant isolation. Every customer-facing read/write must be scoped by
  organization and environment and authorized accordingly.
- Do not store plaintext secrets in source code, configuration files, Docker images,
  logs, database records, or Kafka messages.
- Do not trade durability for throughput: accepted events must be persisted before
  they are acknowledged.
- Do not introduce exactly-once claims for webhooks or hide retries; record lifecycle
  transitions and use idempotency instead.
- Do not make synchronous destination calls on the ingestion path.
- Do not use Redis as a source of truth or allow cache availability to corrupt state.
- Do not bypass transactional outbox, schema compatibility, or consumer idempotency
  when introducing Kafka events.
- Do not use cross-service database access. Use APIs, published events, or a
  rebuildable read model.
- Do not expose stack traces or internal details in public API errors.
- Do not add arbitrary customer code execution, broad connector catalogs,
  cross-region active-active operation, enterprise provisioning, or full analytics
  scope to the MVP without an explicit product decision.

## Repository Map

| Path | Purpose |
| --- | --- |
| `docs/PROJECT_VISION.md` | Product direction, MVP scope, non-goals, and success measures |
| `docs/ENGINEERING_GUIDE.md` | Implementation and operational standards |
| `docs/architecture/SYSTEM_OVERVIEW.md` | Components, deployment, and high-level responsibilities |
| `docs/architecture/MICROSERVICES.md` | Service ownership, APIs, dependencies, and Kafka contracts |
| `docs/architecture/DATABASE.md` | PostgreSQL, Redis, ownership, migrations, and indexes |
| `docs/architecture/EVENT_FLOW.md` | Kafka topics, producer/consumer flow, retry, and DLQ behavior |
| `docs/adr/` | Architecture decision records |
| `.ai/` | AI-specific project context, commands, style guidance, and agent instructions |
