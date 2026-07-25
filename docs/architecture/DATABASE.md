# ProjectEcho Database Strategy

**Status:** Draft

**Scope:** MVP persistence design and scale path

## Principles

ProjectEcho uses PostgreSQL as the durable system of record for transactional
product data and Redis only for ephemeral, rebuildable state. Kafka distributes
domain events but is not the authoritative query store for product lifecycle data.

- Each logical service owns its data and is the only service permitted to write it.
- A service never reads another service's tables. It uses a versioned API, a
  published domain event, or a purpose-built read projection.
- Customer event payloads are sensitive. They are encrypted at rest, access-scoped
  by organization and environment, redacted from diagnostic data, and retained
  only for the configured policy period.
- All identifiers are globally unique, opaque IDs. Every tenant-owned table carries
  `organization_id` and `environment_id` where relevant.
- PostgreSQL timestamps use `timestamptz` in UTC. Deletion and retention actions
  are recorded as lifecycle facts, not silently overwritten.

## PostgreSQL Usage

PostgreSQL is the primary durable datastore for the MVP because it provides ACID
transactions, transactional outbox support, mature backup/recovery tooling, and
strong indexing for both control-plane and event-lifecycle workloads.

### Workload allocation

| Workload | PostgreSQL role | Notes |
| --- | --- | --- |
| Tenant configuration | Authoritative relational store | Organizations, environments, destinations, routing rules, policy versions, and secret references. |
| Identity and access | Authoritative credential metadata store | Only hashed API-key verification data and role/identity bindings; raw credentials stay outside the database. |
| Event ledger | Append-oriented immutable record | Accepted event envelope, payload reference, idempotency result, and retention metadata. |
| Routing and delivery | Transactional lifecycle state | Routing decisions, delivery jobs, attempts, retry schedules, and dead-letter reason. |
| Replay | Auditable command state | Replay requests, policy decisions, approvals if added, and outcome references. |
| Operations query | Denormalized read projection | Rebuildable event timeline/search projection, isolated from write-path query load. |
| Audit | Append-only audit evidence | Security-relevant actions and configuration-change references, with payload minimization. |

### Data modeling rules

- Use normalized relational models for configuration and lifecycle ownership; use
  `jsonb` only for variable event payloads, provider-specific metadata, and sparse
  extensibility fields that are not central query keys.
- The event ledger is append-only. Corrections, redactions, expiration, and replay
  are represented by separate records or explicit lifecycle columns; accepted event
  content is not updated in place.
- Every state-changing transaction writes a transactional outbox record in the same
  PostgreSQL transaction. An outbox relay publishes the corresponding Kafka event.
  This prevents dual-write gaps between the database and Kafka.
- Use database constraints for invariants that must never be violated: foreign
  keys inside a service boundary, unique IDs/idempotency keys, valid enumerated
  states, non-null tenant context, and time-range checks where applicable.
- Use row-level security only as defense in depth after query patterns and
  connection roles are proven. Application-level tenant authorization remains the
  primary control and service databases are not shared by untrusted users.

### Availability, security, and retention

- Run managed PostgreSQL with private network access, multi-zone high availability,
  encryption at rest/in transit, automated backups, point-in-time recovery, and
  periodic restoration tests.
- Use separate application roles per service with least privileges; migration roles
  are separate from runtime roles.
- Encrypt sensitive fields using managed key-management facilities where database
  encryption alone is insufficient. Store destination secrets in a dedicated
  secrets manager and retain only a reference and non-sensitive metadata in
  PostgreSQL.
- Implement retention as scheduled, observable policy execution. Purge or redact
  payload data only after policy and legal requirements are defined; retain a
  minimal auditable tombstone when appropriate.

## Redis Usage

Redis is a performance and coordination dependency, never the source of truth for
events, configuration, credentials, delivery state, or audit evidence. The system
must remain correct when Redis is flushed, unavailable, or rebuilt.

| Use case | Data shape | Expiry / recovery | Guardrail |
| --- | --- | --- | --- |
| API and tenant rate limits | Counters or token buckets keyed by tenant, credential, and route | Short TTL; safely recreated | If unavailable, fall back to conservative gateway limits or reject with a retryable response. |
| Short-lived API-key verification cache | Key status and authorization claims | TTL shorter than credential-revocation propagation target | Revocation events actively invalidate cached entries. |
| Active configuration cache | Immutable configuration snapshots keyed by version | TTL plus event-driven invalidation | Routing records the exact configuration version and can fall back to PostgreSQL/API. |
| Distributed coordination | Narrow leases for singleton maintenance work | Expiring lease with fencing/ownership checks | Never use as the sole proof that a transaction completed. |
| Delivery concurrency controls | Per-destination counters or semaphores | Short TTL and reconciliation | Durable delivery status and retry schedule remain in PostgreSQL. |

Redis is deployed as a managed, encrypted service in private networking. Persistence
may improve recovery speed but is not relied on for correctness. Redis keys use a
documented namespace, include tenant scope where applicable, and never contain raw
credentials or unencrypted customer payloads.

## Database per Service

### MVP physical layout

Start with one managed PostgreSQL cluster containing separate databases, or separate
schemas only when database-level isolation is not yet operationally practical. Each
service still owns its schema, migration history, database role, backup/restore
runbook, and data-access boundary. A schema is not permission to join across
services.

The preferred progression is:

```text
MVP: one PostgreSQL cluster
     ├─ configuration database/schema
     ├─ identity database/schema
     ├─ event-ledger database/schema
     ├─ delivery-lifecycle database/schema
     ├─ replay database/schema
     ├─ operations-query database/schema
     └─ audit database/schema

Scale: independently provision or shard the highest-volume stores
       (event ledger, delivery lifecycle, operations projection)
```

### Ownership map

| Service | Owned database / schema | Core records | Notes |
| --- | --- | --- | --- |
| Edge Gateway | None | None | Redis rate-limit keys are ephemeral infrastructure state. |
| Identity and Access | `identity` | Identity bindings, roles, API-key hashes/status, credential audit references | Managed identity provider data remains externally owned. |
| Configuration | `configuration` | Organizations, environments, destinations, rule versions, policy versions, secret references | Does not retain plaintext destination secrets. |
| Event Ingestion | `event_ledger` | Accepted events, payload references, idempotency decisions, retention metadata, outbox | Append-heavy and likely first candidate for time partitioning. |
| Routing | `routing` | Routing decisions, matched rule references, initial delivery-job intent, outbox | May be combined with delivery lifecycle initially, while preserving table ownership. |
| Delivery | `delivery` | Delivery jobs, attempts, retry schedule, dead-letter state, destination health projection, outbox | High write rate and frequent status transitions. |
| Replay | `replay` | Replay requests, authorization/policy decision, outcome reference, outbox | Retains a complete request audit trail. |
| Operations Query | `operations_query` | Rebuildable denormalized lifecycle projection, saved queries | No canonical state; rebuilds from service APIs/events. |
| Audit and Observability | `audit` | Append-only audit records, retention metadata | Metrics/logs/traces normally live in specialized managed backends. |

### Cross-service consistency

Cross-service transactions are prohibited. A service commits its local state and
outbox together, then publishes to Kafka. Consumers update their own state
idempotently. This provides eventual consistency across services while preserving a
durable, explainable source of truth for every local decision.

For the initial deployment, routing and delivery may use the same physical
PostgreSQL database to reduce operational overhead, but they retain distinct tables,
roles, migration ownership, and no direct write access to each other's tables.

## Migration Strategy

### Ownership and tooling

Each service owns versioned, reviewable SQL migrations stored with that service's
source. A migration tool may be standardized across the repository, but migrations
run under a dedicated migration role and are recorded in a per-service migration
history table. Application startup must verify compatibility but must not run
unreviewed production migrations automatically.

### Deployment process

1. Back up and verify the target database; assess the migration on a production-like
   copy for large or high-risk changes.
2. Apply additive, backward-compatible schema changes first: new tables, nullable
   columns, indexes, or new enum values where safely supported.
3. Deploy application code that can read both old and new representations and write
   the new representation.
4. Backfill asynchronously in bounded batches with progress metrics, retry safety,
   and no long-running transaction.
5. Switch reads only after completeness and performance are verified.
6. In a later release, remove obsolete code and then remove obsolete schema only
   after the rollback window and retention requirements have passed.

This expand–migrate–contract pattern is mandatory for online production changes.
Avoid table rewrites, unbounded locks, and destructive in-place type changes during
normal traffic. Create indexes concurrently where supported and use online schema
tools or maintenance windows for unavoidable high-impact work.

### Migration controls

- Every migration has an owner, release reference, rollback or forward-recovery
  plan, expected lock behavior, and estimated runtime.
- Destructive data changes require an explicit retention/legal review and a tested
  restore path; snapshots are not a substitute for a recovery plan.
- Database constraints are introduced as `NOT VALID` and validated later when that
  reduces lock risk, then enforced once existing data is compliant.
- Partition maintenance, retention deletion, and outbox cleanup run as separately
  observable jobs, not as deploy-time migrations.
- Schema changes that alter a Kafka payload follow event-schema compatibility rules
  and are independently versioned from SQL migrations.

## Index Strategy

Indexes exist to serve measured access patterns, integrity constraints, and
operational queue claims. Every new index must name the query or constraint it
supports, be verified with representative query plans, and be monitored for write
overhead and bloat.

### Baseline conventions

- All tables use a primary key on a globally unique ID.
- Tenant-facing queries include `organization_id`, `environment_id`, and a bounded
  time range whenever possible. Place those selective equality columns before time
  in composite B-tree indexes.
- Use partial indexes for active/retryable states rather than indexing low-value
  historical rows.
- Partition large append-only tables by received time (monthly initially) only
  after volume warrants it. Indexes are created per partition and retention drops
  whole partitions when policy permits.
- Use `jsonb` GIN indexes sparingly and only for approved, stable filter paths;
  prefer extracted typed columns for high-frequency routing or search fields.
- Foreign-key columns are indexed when they support joins, deletes, or integrity
  checks. Every unique business key has a unique constraint, not merely a
  non-unique lookup index.

### Service index plan

| Service / data | Required initial indexes | Purpose |
| --- | --- | --- |
| Identity | Unique active API-key fingerprint; `(organization_id, status)` | Fast credential verification and tenant credential management. |
| Configuration | `(organization_id, environment_id, status)` on destinations; unique `(environment_id, route_name, version)`; `(environment_id, active)` on route versions | Resolve active destinations and rules without scans. |
| Event ledger | Unique `(organization_id, environment_id, idempotency_key)` where supplied; `(organization_id, environment_id, received_at DESC, event_id)`; `(organization_id, environment_id, event_type, received_at DESC)` | Enforce safe retries and power event timeline/type queries. |
| Routing | Unique `(event_id, destination_id, configuration_version)`; `(event_id)`; `(destination_id, decided_at DESC)` | Prevent duplicate job creation and explain a routing decision. |
| Delivery | Unique delivery-job ID; partial `(next_attempt_at, destination_id)` where status is retryable; `(event_id, created_at DESC)`; `(destination_id, status, updated_at DESC)` | Efficient scheduler claims, event timeline, and destination health views. |
| Replay | Unique request idempotency key per tenant; `(organization_id, environment_id, requested_at DESC)`; `(original_delivery_id)` | Prevent duplicate replay requests and support audit/status lookup. |
| Operations query | `(organization_id, environment_id, occurred_at DESC, event_id)`; `(delivery_status, updated_at DESC)`; approved selective filters | Cursor pagination and operational triage. |
| Audit | `(organization_id, occurred_at DESC)`; `(actor_id, occurred_at DESC)`; `(resource_type, resource_id, occurred_at DESC)` | Tenant, actor, and resource audit investigations. |
| Transactional outbox | Partial `(created_at, id)` where `published_at IS NULL`; unique event ID | Efficient, ordered publisher polling and exactly-once publication tracking at the producer boundary. |

### Query and lifecycle safeguards

- Use keyset/cursor pagination, never deep `OFFSET` pagination, for event and
  delivery timelines.
- Route large analytical or export queries to a read projection or warehouse in the
  future; they must not compete with ingestion and delivery writes.
- Inspect `EXPLAIN (ANALYZE, BUFFERS)` against representative data before shipping
  critical indexes or query changes.
- Monitor slow queries, lock waits, connection saturation, index bloat, cache hit
  rate, replication lag, partition sizes, and autovacuum health.
- Revisit indexes after retention or partition changes; unused indexes impose a
  write and storage cost and should be removed through the normal migration process.
