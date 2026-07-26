# ProjectEcho Kafka Event Flow

**Status:** Draft

## Topics

Kafka carries ProjectEcho's asynchronous lifecycle events. Processing is at-least-once: each consumer is idempotent, and a webhook recipient may receive duplicate delivery. PostgreSQL remains the authoritative lifecycle record.

Every record contains `event_id`, `event_type`, `schema_version`, `occurred_at`, `organization_id`, `environment_id`, `correlation_id`, and `causation_id` when applicable. `event_id` is the consumer idempotency key. Payloads must not contain credentials or signing secrets. Topic names use `echo.<domain>.<event-name>.v<major>`; additive compatible changes stay within a major version, while breaking changes create a new major topic.

| Topic | Producer | Consumers | Partition key |
| --- | --- | --- | --- |
| `echo.events.accepted.v1` | Ingestion | Routing, Operations Query, Audit | `organization_id` |
| `echo.routing.completed.v1` | Routing | Operations Query, Audit | `organization_id` |
| `echo.routing.failed.v1` | Routing | Operations Query, Audit, alerting | `organization_id` |
| `echo.delivery.requested.v1` | Routing | Delivery | `destination_id` |
| `echo.delivery.attempted.v1` | Delivery | Operations Query, Audit | `organization_id` |
| `echo.delivery.succeeded.v1` | Delivery | Operations Query, Replay, Audit | `organization_id` |
| `echo.delivery.retry-scheduled.v1` | Delivery | Operations Query, Audit, alerting | `organization_id` |
| `echo.delivery.failed.v1` | Delivery | Operations Query, Replay, Audit | `organization_id` |
| `echo.delivery.dead-lettered.v1` | Delivery | Operations Query, Replay, Audit, alerting | `organization_id` |
| `echo.delivery.destination.unhealthy.v1` | Delivery | Configuration, Operations Query, Audit | `destination_id` |
| `echo.replay.requested.v1` | Replay | Delivery, Audit | `destination_id` |
| `echo.replay.accepted.v1` | Replay | Operations Query, Audit | `organization_id` |
| `echo.replay.rejected.v1` | Replay | Operations Query, Audit | `organization_id` |
| `echo.replay.completed.v1` | Replay | Operations Query, Audit | `organization_id` |
| `echo.configuration.organization.created.v1` | Configuration | Identity, Audit | `organization_id` |
| `echo.configuration.organization.deleted.v1` | Configuration | Identity, Audit, Operations Query | `organization_id` |
| `echo.configuration.destination.activated.v1` | Configuration | Delivery, Routing, Audit | `destination_id` |
| `echo.configuration.destination.disabled.v1` | Configuration | Delivery, Routing, Audit | `destination_id` |
| `echo.configuration.route.published.v1` | Configuration | Routing, Audit | `environment_id` |
| `echo.configuration.route.retired.v1` | Configuration | Routing, Audit | `environment_id` |
| `echo.identity.api-key.created.v1` | Identity | Audit, cache invalidator | `organization_id` |
| `echo.identity.api-key.revoked.v1` | Identity | Cache invalidator, Audit | `organization_id` |
| `echo.identity.role.changed.v1` | Identity | Audit, authorization-cache invalidator | `organization_id` |
| `echo.audit.recorded.v1` | Audit | Optional security integration | `organization_id` |

`echo.events.rejected.v1` is a privacy-safe diagnostics topic only and never includes rejected customer payloads.

## Producers and Consumers

Each producer writes owned PostgreSQL state and an immutable outbox record in the same transaction. Its outbox relay publishes with idempotent Kafka producer settings and marks the record published only after broker acknowledgement. This prevents database/Kafka dual-write gaps.

Each consuming service has an independent consumer group. It validates schema, writes local state, an idempotency receipt, and any outgoing outbox record in one transaction, then commits its Kafka offset. Duplicate `event_id` values are safely acknowledged as already processed. A consumer does not hold an uncommitted offset while calling another service.

Kafka ordering exists only within partitions. `organization_id` is the default key; delivery work uses `destination_id` for per-destination sequencing and concurrency. Select partition count from measured throughput, consumer parallelism, and recovery needs; plan expansion carefully because reassignment can affect ordering.

## Retry Strategy

| Stage | Mechanism | Use | Outcome |
| --- | --- | --- | --- |
| Immediate | 2–3 in-process retries with jitter | Brief broker, database, or dependency fault | Success or delayed retry |
| Delayed 1 | `<source-topic>.retry.1` | Recoverable failure after immediate retry | Short bounded delay |
| Delayed 2 | `<source-topic>.retry.2` | Persistent recoverable failure | Longer bounded delay |
| Dead letter | `<source-topic>.dlq` | Invalid schema, poison message, exhausted retry, permanent error | Record context and commit source offset |

Delayed retries use scheduled relays or an approved delay queue; consumers never sleep while holding a partition. Retry metadata retains original topic, partition, offset, first-seen time, attempt count, error class, and correlation ID. Invalid schema, missing tenant context, authorization failure, and invariant violations go directly to a DLQ. Temporary dependency and timeout failures use bounded retry.

Webhook retry belongs to Delivery, not Kafka consumer retry. Delivery first persists the job and commits its Kafka offset, then calls the destination using a durable retry schedule.

| Webhook result | Handling |
| --- | --- |
| 2xx | Mark success and emit `echo.delivery.succeeded.v1` |
| Timeout, network error, 408, 429, 5xx | Record attempt; use exponential backoff, jitter, and safe `Retry-After` support |
| Other 4xx or invalid destination | Terminal failure except documented provider-specific exceptions |
| Budget exhausted | Persist delivery dead-letter state, emit `echo.delivery.dead-lettered.v1`, and permit authorized replay |

Initial policy uses several attempts over a bounded window, a maximum job age, and per-destination concurrency limits. Exact values require design-partner validation. An unhealthy destination must not starve healthy destinations.

## Dead Letter Queues

Each consumed business topic has a `<source-topic>.dlq` owned by the failing consumer. A DLQ record includes the original message, source topic/partition/offset, consumer group, retry history, failure class, sanitized error detail, and failure timestamps. Access is limited to platform operators.

1. Alert on entries and monitor volume by service, tenant, and error class.
2. Diagnose schema, code, dependency, data, or configuration cause.
3. Fix the cause without changing the original record.
4. Re-drive through an audited tool that creates a new processing attempt; never manually copy records to a live topic.
5. Verify durable processing before incident closure.

DLQs retain records longer than retries but follow customer-data policy; they are recovery queues, not archives. Webhook delivery dead letters are distinct: Delivery owns the canonical PostgreSQL record linked to job, source event, destination, attempts, and terminal reason. `echo.delivery.dead-lettered.v1` informs Operations Query and Replay; replay creates a new delivery job and keeps original history.

## Operational Controls

- Kafka uses private networking, encryption in transit and at rest, broker authentication, and least-privilege service ACLs.
- Monitor producer errors, outbox lag, broker health, consumer lag, retry depth, DLQ volume, acceptance-to-delivery latency, and topic storage growth.
- Maintain tested runbooks for outbox failures, sustained lag, DLQ entries, schema failures, and destination outage storms.
