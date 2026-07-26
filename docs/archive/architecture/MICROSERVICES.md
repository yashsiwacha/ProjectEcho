# ProjectEcho Service Architecture

**Status:** Draft

**Scope:** Logical service boundaries and Kafka event contracts

## Operating Model

This document defines the logical services of ProjectEcho. For the MVP, these
boundaries may run as modules within a small number of deployables; they must still
honor the ownership and interface rules described here. A service is extracted only
when independent scaling, security isolation, release cadence, or team ownership
creates a clear benefit.

Kafka is the target event backbone used in this document. If the MVP begins with a
managed queue rather than Kafka, it must preserve the same domain-event names,
schema-versioning, idempotency, and failure semantics so migration remains an
infrastructure substitution rather than a product-contract change.

All service APIs are versioned, authenticated, and authorized. Each emitted Kafka
record carries `event_id`, `event_type`, `schema_version`, `occurred_at`,
`organization_id`, `environment_id`, `correlation_id`, and `causation_id` where
applicable. Payloads must not include secrets, and consumers must tolerate duplicate
delivery and unknown additive fields.

## Service Map

```text
                    ┌──────────────────────────────┐
                    │ Edge Gateway                  │
                    └───────────────┬──────────────┘
                                    │ HTTPS
              ┌─────────────────────┼─────────────────────┐
              ▼                     ▼                     ▼
       Ingestion API          Control API           Operations API
              │                     │                     │
              ▼                     ▼                     ▼
          Event Ledger     Configuration/IAM      Lifecycle Query
              │                     │
              └──────► Kafka ◄──────┘
                          │
                  ┌───────┴────────┐
                  ▼                ▼
           Routing Service    Audit/Observability
                  │
                  ▼
           Delivery Service ─────────────► Customer destinations
                  ▲
             Replay Service
```

## Kafka Conventions

| Convention | Decision |
| --- | --- |
| Topic naming | `echo.<domain>.<event-name>.v<major>`; for example, `echo.events.accepted.v1`. |
| Partition key | `organization_id` by default. Use `destination_id` for delivery-work topics when destination ordering is required. |
| Delivery semantics | At-least-once. Consumers store or derive an idempotency key before a non-idempotent effect. |
| Publishing | A transactional outbox is written with the service's database change; a relay publishes it to Kafka. No dual write from request handlers. |
| Consumption | Consumers commit only after durable local state changes. Failed messages use bounded retries and a named dead-letter topic. |
| Schema governance | Versioned schemas with backward-compatible additive changes within a major version. Breaking changes use a new major topic. |
| Access control | Service identities receive least-privilege produce/consume ACLs only for their declared topics. |

## 1. Edge Gateway

**Responsibility**

Provide the controlled public entry point. It terminates TLS, applies web-application
firewall rules, request-size limits, coarse rate limits, and routes requests to the
appropriate internal API. It never evaluates business routing rules or stores event
payloads.

**Public APIs**

- Public HTTPS hostnames for producer ingestion and the operator console/control
  plane.
- Health and readiness endpoints suitable for load-balancer checks; they reveal no
  tenant or operational data.

**Internal dependencies**

- Identity service or an API-key validation adapter for coarse authentication.
- Ingestion, configuration/control, and operations API services.
- Managed WAF, certificate manager, and rate-limit store.

**Database ownership**

None. Ephemeral rate-limit counters may use Redis; they are not a system of record.

**Kafka events**

- Produces no business events.
- Does not consume business events. Access logs flow to the observability platform,
  not Kafka, unless a future security pipeline requires a dedicated export.

**Future scaling considerations**

Remain stateless and horizontally scalable at the edge. Isolate ingestion and
control-plane routes into distinct upstream pools as their traffic patterns diverge.
Use tenant-aware rate limits and regional edges before introducing global traffic
routing.

## 2. Identity and Access Service

**Responsibility**

Authenticate API keys and operator identities, authorize actions by organization,
environment, and role, and manage credential lifecycle. It provides decisions and
identity claims; product services remain responsible for enforcing authorization on
their resources.

**Public APIs**

- Internal authentication and authorization decision API.
- Operator login/session endpoints when ProjectEcho hosts identity directly.
- Credential-management endpoints exposed through the control API, not directly to
  untrusted producers.

**Internal dependencies**

- Configuration service for organization and role context.
- Managed identity provider for operator authentication, where selected.
- Key-management and secrets services for API-key hashing, signing, and rotation.

**Database ownership**

Owns identity bindings, role assignments, hashed API-key verification data, key
status, and credential audit references. Raw API keys and destination secrets are
never stored in plaintext.

**Kafka events**

- Produces `echo.identity.api-key.created.v1`,
  `echo.identity.api-key.revoked.v1`, and `echo.identity.role.changed.v1`.
- Consumes `echo.configuration.organization.deleted.v1` to revoke or tombstone
  tenant credentials after lifecycle checks.
- Its events are consumed by audit/observability and cache-invalidation consumers;
  services must not depend on an asynchronous event for immediate authorization.

**Future scaling considerations**

Cache short-lived verification results close to ingestion, with explicit revocation
propagation. Separate interactive user identity from high-volume producer-key
validation if ingestion demand warrants it. Enterprise SSO and SCIM are deferred
until enterprise requirements justify a dedicated identity integration boundary.

## 3. Configuration Service

**Responsibility**

Own the tenant control plane: organizations, environments, destinations, routing
rules, delivery-policy configuration, and API-key metadata. Validate configuration
before activation and retain an auditable version history.

**Public APIs**

- Versioned control-plane REST API for organizations, environments, destinations,
  routing rules, and delivery policies.
- Internal read API for routing and delivery services to resolve active
configuration by immutable version.

**Internal dependencies**

- Identity service for authorization and credential lifecycle.
- Managed secret store for encrypted destination credentials.
- Audit service for durable audit recording.

**Database ownership**

Owns the configuration PostgreSQL schema: organization and environment records,
destination metadata, encrypted secret references, routing rules, configuration
versions, and policy history. Other services may read through its API or consume
published snapshots; they must not query its tables.

**Kafka events**

- Produces `echo.configuration.organization.created.v1`,
  `echo.configuration.organization.deleted.v1`,
  `echo.configuration.destination.activated.v1`,
  `echo.configuration.destination.disabled.v1`,
  `echo.configuration.route.published.v1`, and
  `echo.configuration.route.retired.v1`.
- Consumes `echo.delivery.destination.unhealthy.v1` only to surface status or
  require operator action; it must not silently change delivery policy.

**Future scaling considerations**

Publish immutable configuration snapshots and let routing cache them by version.
Partition tenant data only after a measured storage or noisy-neighbor need. Add
schema registry integration, change-approval workflows, and environment promotion
as governance complexity grows.

## 4. Event Ingestion Service

**Responsibility**

Accept producer events, authenticate the producer, validate the versioned envelope,
enforce tenancy and rate limits, persist an immutable accepted-event record, and
initiate asynchronous routing. It does not synchronously contact destinations.

**Public APIs**

- `POST /v1/events` for single-event acceptance.
- A future batch ingestion API only after limits, partial-failure semantics, and
backpressure behavior are specified.
- Readiness and health endpoints for platform operations.

**Internal dependencies**

- Identity service or local verification cache for API-key validation.
- Event ledger database and transactional outbox.
- Redis or equivalent for rate-limit coordination.
- Kafka through the outbox relay.

**Database ownership**

Owns the immutable event ledger: event ID, tenant context, source, type, received
time, payload reference, envelope version, idempotency key, acceptance result, and
outbox records. Its payload storage is the authoritative accepted copy through the
retention period.

**Kafka events**

- Produces `echo.events.accepted.v1` after a durable acceptance transaction.
- Produces `echo.events.rejected.v1` only for internally useful, privacy-safe
  diagnostics; client validation errors are returned synchronously.
- Does not consume customer event topics in the MVP. It may consume
  `echo.identity.api-key.revoked.v1` to invalidate local verification caches.

**Future scaling considerations**

Scale horizontally by request rate and partition the ledger by time and tenant.
Protect the database with payload-size limits and object-storage spillover for
approved large payloads. Add regional ingestion cells before attempting global
ordering or cross-region writes.

## 5. Routing Service

**Responsibility**

Evaluate the active configuration against each accepted event and create a durable,
independent delivery job for every matched destination. Record the rule and
configuration version used for explainability. It does not perform outbound network
calls.

**Public APIs**

- No producer-facing API.
- Internal, authorized diagnostic API to retrieve a routing decision by event ID.
- Future simulation API for evaluating a proposed rule against sample events.

**Internal dependencies**

- Kafka consumer for accepted events.
- Configuration service or immutable configuration snapshot cache.
- Event lifecycle database and transactional outbox.

**Database ownership**

Owns routing-decision records: event ID, evaluated configuration version, matched
rule IDs, destination IDs, decision time, and skipped/error reasons. It owns the
initial delivery-job records, while the delivery service owns later attempt state.

**Kafka events**

- Consumes `echo.events.accepted.v1`.
- Consumes `echo.configuration.route.published.v1` and
  `echo.configuration.route.retired.v1` to refresh its configuration cache.
- Produces `echo.routing.completed.v1` and `echo.delivery.requested.v1` per
  eligible destination.
- Produces `echo.routing.failed.v1` when a durable routing error requires
  intervention.

**Future scaling considerations**

Scale consumer groups by Kafka partitions. Preserve required ordering by selecting
the partition key deliberately. Add a compiled rule representation and bounded
payload-field indexing before supporting complex predicates; arbitrary user code is
out of scope.

## 6. Delivery Service

**Responsibility**

Consume delivery requests and execute signed outbound delivery to webhook
destinations. It enforces destination concurrency, timeout, retry, backoff, and
dead-letter policies, and records every attempt without mutating the source event.

**Public APIs**

- No public producer API.
- Internal operational API for an authorized status lookup or to pause/resume a
  destination, with configuration changes delegated to the configuration service.

**Internal dependencies**

- Kafka consumer for delivery requests and replay requests.
- Configuration service for destination metadata and policy; secrets service for
  send-time signing material.
- Delivery database, controlled outbound egress, DNS/SSRF protections, and a
  scheduler or retry queue.

**Database ownership**

Owns delivery execution state: delivery job state, attempt number, request and
response metadata subject to redaction, retry schedule, dead-letter reason, and
delivery idempotency key. It references the event ledger by ID rather than copying
the canonical payload indefinitely.

**Kafka events**

- Consumes `echo.delivery.requested.v1` and `echo.replay.requested.v1`.
- Produces `echo.delivery.attempted.v1`, `echo.delivery.succeeded.v1`,
  `echo.delivery.retry-scheduled.v1`, `echo.delivery.failed.v1`, and
  `echo.delivery.dead-lettered.v1`.
- Produces `echo.delivery.destination.unhealthy.v1` after a policy-defined failure
  threshold; that signal is informative and does not automatically disable a
  destination.

**Future scaling considerations**

Scale workers by queue lag while enforcing per-destination concurrency partitions.
Isolate slow destinations and large tenants into separate worker pools to prevent
head-of-line blocking. Add connector-specific adapters behind the same delivery-job
contract rather than expanding webhook code paths.

## 7. Replay Service

**Responsibility**

Authorize and coordinate replay requests for eligible accepted events or failed
deliveries. It validates retention, tenant/environment boundaries, destination
state, and replay policy, then creates a new auditable replay request. It never
alters the original acceptance, routing, or attempt history.

**Public APIs**

- `POST /v1/events/{event_id}/replays` through the operations/control API.
- `POST /v1/deliveries/{delivery_id}/replays` for an authorized failed-delivery
  replay.
- Internal read API for replay status and reason codes.

**Internal dependencies**

- Identity service for authorization.
- Event ledger and routing-decision read APIs.
- Delivery service status API or read model.
- Kafka and its own transactional outbox.

**Database ownership**

Owns replay-request records: requestor, scope, original IDs, policy decision,
reason, approval state if introduced, resulting delivery IDs, and lifecycle status.

**Kafka events**

- Consumes `echo.delivery.dead-lettered.v1` to make eligible failures discoverable.
- Produces `echo.replay.requested.v1`, `echo.replay.accepted.v1`,
  `echo.replay.rejected.v1`, and `echo.replay.completed.v1`.
- Consumes `echo.delivery.succeeded.v1` and `echo.delivery.failed.v1` to update
  replay outcome projections.

**Future scaling considerations**

Most replay activity will be low volume. Optimize for policy correctness and audit
quality first. Add bulk replay with explicit rate limits, approval workflows, and
destination protection before allowing large historical reprocessing jobs.

## 8. Operations Query Service

**Responsibility**

Provide the authorized read model used by the console and support tooling: event
timeline, routing outcome, delivery status, and replay history. It makes lifecycle
state easy to inspect without granting direct database access or overloading write
services with complex queries.

**Public APIs**

- Versioned REST API for event search, event detail, delivery detail, and replay
  status.
- Internal support API with stricter, audited authorization for approved support
  workflows.

**Internal dependencies**

- Identity service for authorization.
- Read-only APIs from ingestion, routing, delivery, and replay services during the
  MVP, or lifecycle events to build a denormalized read projection as volume grows.
- Search index is optional and must not be the only source of truth.

**Database ownership**

Owns only its denormalized query projection and saved-query metadata. It does not
own or modify canonical event, routing, delivery, or replay records.

**Kafka events**

- Consumes accepted, routing, delivery, and replay lifecycle topics to build a
  query projection: `echo.events.accepted.v1`, `echo.routing.completed.v1`,
  `echo.delivery.*.v1`, and `echo.replay.*.v1`.
- Produces no business events in the MVP. Access logs are sent to the observability
  pipeline.

**Future scaling considerations**

Introduce a dedicated search index and cursor-based pagination once direct query
patterns become expensive. Keep payload indexing opt-in and privacy reviewed. Split
support access into a separate, tightly audited surface if support workflows expand.

## 9. Audit and Observability Service

**Responsibility**

Collect immutable audit evidence and operational telemetry. It answers who changed
configuration or initiated replay, while the observability pipeline answers whether
the platform is healthy. It must avoid becoming a second event ledger.

**Public APIs**

- Internal audit-write API used by services for security-relevant actions.
- Authorized audit-query API for administrators.
- Standard telemetry ingestion endpoints through the selected observability stack.

**Internal dependencies**

- Managed append-oriented audit store.
- OpenTelemetry-compatible metrics, logging, and tracing backends.
- Identity service for actor context and operations service for audited queries.

**Database ownership**

Owns immutable audit records, audit retention metadata, and operational telemetry
configuration. Product payloads are excluded or redacted by default.

**Kafka events**

- Consumes configuration, identity, replay, and optionally lifecycle events to
  create audit projections.
- Produces `echo.audit.recorded.v1` only when another security system needs an
  integration signal; normal product services must not depend on it.

**Future scaling considerations**

Use retention tiers and sampling for high-volume telemetry while preserving all
required audit records. Separate long-term compliance storage from hot operational
search. Add immutable external archival only when regulatory requirements demand it.

## Cross-Service Rules

1. A service writes only its owned database and its transactional outbox.
2. Synchronous calls are bounded by deadlines and used only where an immediate
   decision is essential; Kafka carries asynchronous lifecycle work.
3. Every consumer is idempotent and records enough state to explain duplicates,
   retries, and dead-letter handling.
4. Event schemas are contracts: publish documentation, compatibility rules, and
   deprecation windows before changing them.
5. Customer payload access is minimized. Services exchange references or necessary
   fields rather than copying full payloads unless delivery requires them.
6. A service failure must degrade locally: unavailable delivery must not prevent
   durable ingestion, and a stale query projection must not change delivery truth.
