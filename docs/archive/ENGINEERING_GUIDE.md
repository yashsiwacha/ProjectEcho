# ProjectEcho Engineering Guide

**Status:** Draft

## Purpose

This guide defines the default engineering standards for ProjectEcho. Deviations are
allowed only when documented in an ADR or approved during design review. Optimize
for clarity, reliability, secure operation, and safe evolution over cleverness.

## Java 21

- Use Java 21 LTS as the project baseline. CI, local development, and container
  builds must use the same major JDK.
- Prefer standard Java features before adding libraries. Use records for immutable
  transport/value types where their semantics fit; use sealed types when a closed
  domain hierarchy adds clarity.
- Keep domain logic independent of HTTP, Kafka, persistence, and framework types.
- Avoid nullable return values. Use validation, explicit result types where useful,
  and `Optional` only for an optional return value, never for fields or parameters.
- Treat warnings as defects. Build tooling should enable compiler linting and apply
  consistent formatting and static analysis before merge.
- Use UTC `Instant`/`OffsetDateTime` for persisted and API timestamps. Never use a
  server default timezone for business behavior.

## Spring Boot 3

- Use the current supported Spring Boot 3 release compatible with Java 21; pin and
  upgrade dependencies through the build's dependency-management mechanism.
- Use constructor injection only. Components must have focused responsibilities and
  explicit interfaces at architectural boundaries.
- Keep web controllers thin: authenticate/authorize, validate request input, call an
  application service, and map the result to a response.
- Use Spring's validation support at every external boundary. Do not rely on database
  constraint exceptions as normal request validation.
- Use `@Transactional` only at application-service boundaries. Keep transactions
  short and never make remote HTTP, Kafka, or blocking calls inside them.
- Publish Kafka events through the transactional outbox described in the architecture
  documents; do not dual-write from request handlers.
- Configure health, readiness, liveness, metrics, and graceful shutdown through
  Spring Boot Actuator. Protect non-public actuator endpoints.

## Package Naming and Code Organization

- Base package: `com.projectecho`.
- Service applications live below `com.projectecho.<service>`, using lowercase,
  singular service names such as `ingestion`, `routing`, and `delivery`.
- Package names are lowercase ASCII and use no underscores, hyphens, or technology
  vendor names.
- Organize each service by feature/domain first, with clear layers within it:
  `api`, `application`, `domain`, and `infrastructure`. For example,
  `com.projectecho.ingestion.events.application`.
- Keep adapters at the edge: REST controllers in `api`, Kafka/persistence/HTTP
  adapters in `infrastructure`, and framework-free rules in `domain`.
- Do not expose JPA entities directly through REST or Kafka contracts. Define
  explicit request, response, command, and event DTOs.
- Avoid generic catch-all packages such as `util`, `common`, or `manager`. Share
  code only after a demonstrated multi-service need, and keep shared libraries small
  and versioned.

## Logging and Observability

- Emit structured JSON logs in deployed environments. Every log record includes
  timestamp, severity, service, environment, deployment version, trace ID, and
  correlation ID when available.
- Include organization/environment IDs and resource IDs only when needed for
  operations; never log raw API keys, authorization headers, cookies, destination
  secrets, or unredacted sensitive payloads.
- Use levels consistently: `ERROR` for actionable failures, `WARN` for unexpected
  recoverable conditions, `INFO` for significant lifecycle actions, and `DEBUG` for
  bounded diagnostic detail. Do not use logs as a high-volume event store.
- Log exceptions once at the boundary where they become actionable. Preserve causes
  and stack traces; avoid duplicate logging as errors propagate.
- Propagate W3C trace context and ProjectEcho correlation IDs through HTTP, Kafka,
  scheduled work, and outbound webhooks. Instrument with OpenTelemetry.
- Publish metrics for request rate/latency/error rate, event acceptance, outbox lag,
  Kafka lag, retry depth, delivery outcomes, and database pool health. Use metrics
  and traces—not raw payload logs—for performance diagnosis.

## Testing

### Required layers

| Test type | Scope | Standard |
| --- | --- | --- |
| Unit | Domain and application logic | Fast, isolated, deterministic; no Spring context or network. |
| Slice | Web, persistence, and messaging adapters | Verify serialization, validation, queries, and adapter configuration. |
| Integration | Service with real infrastructure dependencies | Use disposable PostgreSQL, Kafka, and Redis instances; verify outbox and idempotency behavior. |
| Contract | REST and Kafka boundaries | Verify producer/consumer compatibility and error contracts before deployment. |
| End-to-end | Critical production-like flows | Cover accepted event through routing, delivery, failure, and authorized replay. |

- Use JUnit 5 and a consistent assertion/mocking approach. Prefer test fixtures and
  builders with explicit defaults over opaque shared fixtures.
- Use Testcontainers or an equivalent disposable-real-dependency strategy for
  PostgreSQL, Kafka, Redis, and outbound HTTP integration tests. Do not substitute
  in-memory databases for PostgreSQL behavior tests.
- Every defect fix includes a regression test at the lowest meaningful layer.
- Test failure paths: duplicate Kafka delivery, outbox publication ambiguity,
  database failure, invalid input, authorization denial, webhook timeout, retry,
  DLQ transition, and replay.
- Tests must not depend on wall-clock timing, test order, external internet access,
  or shared persistent environments. Clock and random-ID sources should be
  controllable in tests.
- CI must run unit and relevant integration/contract tests, static analysis, and
  dependency/security scanning before merge. Flaky tests are treated as defects.

## Docker

- Build immutable, versioned container images using a multi-stage build. Runtime
  images contain only the application runtime and required OS libraries.
- Use an approved Java 21 runtime base image, pin image digests where the delivery
  pipeline supports it, and regularly rebuild for security patches.
- Run as a non-root user with a read-only filesystem where feasible. Do not bake
  secrets, private keys, environment-specific config, or build credentials into an
  image.
- Supply configuration through environment variables, mounted secrets, or a managed
  configuration system; validate required configuration at startup without logging
  secret values.
- Expose only the application port and required health endpoints. Containers must
  handle `SIGTERM` gracefully: stop accepting traffic, complete bounded in-flight
  work, and release resources.
- Local Docker Compose is for development dependencies and repeatable onboarding;
  it is not the production orchestration design.
- Scan images in CI, produce an SBOM where tooling permits, and fail builds on
  unapproved critical vulnerabilities according to the security policy.

## REST API Standards

- Use HTTPS only. Version external APIs in the path, for example `/v1/...`; do not
  introduce a breaking change within a published version.
- Model resources with nouns and HTTP methods with their standard meaning. Use
  `POST` for commands such as event submission or replay creation, and return the
  created resource or an explicit acceptance representation.
- Use JSON with `application/json`, UTF-8, ISO-8601 UTC timestamps, opaque stable
  IDs, and consistent `camelCase` field names.
- Validate request bodies, path/query parameters, content type, request size, and
  authorization before executing business behavior.
- List endpoints use cursor pagination with documented stable sort order. Do not use
  deep offset pagination for event timelines.
- Define idempotency behavior for retryable write endpoints. Ingestion supports an
  idempotency key scoped to organization and environment; the same key and request
  must return the original outcome.
- Apply rate limits at the gateway and service boundary. Return documented rate-limit
  headers and a `429` response with a retry hint where applicable.
- Document every public endpoint with OpenAPI, including authentication, request and
  response schema, validation rules, pagination, idempotency, and error responses.

## Error Handling

- Use a single RFC 9457 Problem Details-compatible error shape for REST APIs:
  `type`, `title`, `status`, `detail`, `instance`, `code`, and `correlationId`.
- `detail` must be safe for clients and never disclose stack traces, internal host
  names, SQL, tokens, or sensitive data. Log diagnostic detail internally with the
  same correlation ID.
- Map expected errors consistently: `400` malformed request, `401` unauthenticated,
  `403` unauthorized, `404` absent/not visible resource, `409` state or idempotency
  conflict, `422` semantically invalid request, `429` rate limited, and `5xx` only
  for server/dependency failures.
- Centralize exception-to-response mapping. Controllers and domain code do not
  construct ad hoc error payloads.
- Distinguish retryable from non-retryable failures in Kafka and webhook processing;
  record durable reason codes and preserve the causal chain for operations.
- Fail closed for authorization, validation, and unknown configuration. Use graceful
  degradation only where it preserves correctness, such as temporarily refusing new
  ingestion when a required durable dependency is unavailable.

## Security

- Follow least privilege for human users, service identities, database roles, Kafka
  ACLs, cloud roles, and network paths. Separate development, staging, and
  production credentials and data.
- Authenticate producer requests with scoped, rotatable API keys. Store only a
  verifier/hash; show the raw secret once at creation and revoke on compromise.
- Authenticate operators through an approved identity provider. Enforce role-based
  authorization by organization and environment for every operation.
- Encrypt data in transit and at rest. Use a managed secret store and key-management
  system for destination signing secrets, encryption keys, and rotation; never put
  secrets in source control, logs, Docker images, or Kafka events.
- Treat event payloads as sensitive. Apply payload size limits, retention policies,
  redaction rules, scoped read access, and audit logging for sensitive actions.
- Protect outbound webhooks from SSRF: validate URLs and DNS resolution, block
  private/link-local/reserved destinations, restrict redirects, use controlled
  egress, enforce timeouts, and sign requests.
- Use dependency scanning, secret scanning, static analysis, and image scanning in
  CI. Patch high-severity vulnerabilities according to a documented response target.
- Threat-model changes that introduce a new public endpoint, data category,
  privileged action, external integration, or trust boundary. Record material
  architectural security decisions as ADRs.
