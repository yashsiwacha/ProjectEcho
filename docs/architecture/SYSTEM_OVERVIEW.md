# ProjectEcho System Overview

**Status:** Draft

**Scope:** MVP high-level architecture

## Architectural Intent

ProjectEcho is an event delivery platform. It accepts authenticated events from
customer systems, persistently records them, evaluates routing rules, delivers
them to configured destinations, and exposes the complete lifecycle to authorized
operators.

The architecture separates the synchronous acceptance path from asynchronous
delivery. An event is acknowledged only after it has been validated and durably
recorded; slow or unavailable destinations must not delay ingestion. Delivery is
therefore at-least-once, and destination handlers must use the supplied event ID to
implement idempotency where a repeated side effect would be harmful.

## System Context

```text
Event Producers                  ProjectEcho                         Destinations
┌─────────────────┐       ┌─────────────────────────┐       ┌──────────────────┐
│ Customer apps   │ HTTPS │ Ingestion & Control APIs │       │ Customer webhooks│
│ Services/devices├──────►│ Durable event pipeline   ├──────►│ Future connectors│
└─────────────────┘       │ Operations console      │       └──────────────────┘
                          └───────────┬─────────────┘
                                      │
                              ┌───────▼────────┐
                              │ Operators      │
                              │ Admins/support │
                              └────────────────┘
```

## Components

| Component | Primary responsibility | MVP notes |
| --- | --- | --- |
| Edge / API gateway | Terminates TLS, applies request-size and rate limits, routes public requests, and emits access telemetry. | The only public entry point for APIs and the console. |
| Identity and access service | Authenticates API keys and console users; authorizes requests by organization, environment, and role. | API-key ingestion and basic workspace roles are required. |
| Event ingestion service | Validates the versioned event envelope, assigns/carries correlation metadata, persists an accepted event, and publishes work for routing. | Returns an acceptance result only after the event is durable. |
| Event store | Holds immutable event records and lifecycle references. | Retention is configurable policy, not unbounded archival. |
| Event bus / work queue | Decouples ingestion, routing, delivery, and notifications; provides durable asynchronous work distribution. | Topics or queues are internal implementation details, not customer contracts. |
| Routing service | Loads the active routing configuration, evaluates rules against an accepted event, and creates one delivery job per matched destination. | Supports event type and source predicates in the MVP. |
| Configuration service | Manages organizations, environments, destinations, routing rules, API-key metadata, and policy state. | Changes are authorized and audit-logged. |
| Delivery service | Sends signed outbound webhook requests, records each attempt, applies retry policy, and transitions exhausted jobs to a dead-letter state. | Delivery is independently scalable from ingestion. |
| Replay service | Creates a new, auditable delivery attempt for an eligible historic event or failed delivery. | Replays do not mutate the original event record. |
| Operations API and console | Lets authorized users inspect events and delivery status, manage configuration, and request replay. | A console can be backed by the same control-plane API. |
| Audit and observability pipeline | Captures configuration changes, security-relevant actions, metrics, structured logs, and distributed traces. | Audit data is separate from customer event payload access. |

## Service Communication

### External interfaces

| Interaction | Protocol | Direction | Contract and behavior |
| --- | --- | --- | --- |
| Producer publishes an event | HTTPS + JSON | Producer → Ingestion service | Authenticated request using an API key; response confirms acceptance or provides a validation/authentication error. |
| Operator uses console or control API | HTTPS + JSON | Browser/client → Control-plane services | Session or token authentication; authorization is evaluated for every workspace and environment boundary. |
| ProjectEcho delivers an event | HTTPS webhook | Delivery service → Destination | Signed request with event ID and delivery metadata; destination returns a success or retryable/non-retryable outcome. |

### Internal interactions

```text
Producer
   │ HTTPS
   ▼
Gateway → Ingestion Service → Event Store
                                 │ transactionally records acceptance
                                 ▼
                              Event Bus
                                 │
                                 ▼
                         Routing Service ───► Configuration Store
                                 │
                          delivery jobs
                                 ▼
                            Work Queue
                                 │
                                 ▼
                         Delivery Service ───► Destination webhook
                                 │
                   attempt/status updates
                                 ▼
                    Event Store + Observability pipeline
```

Internal service calls should use authenticated service identities, encrypted
transport, explicit timeouts, and correlation IDs. Long-running or retryable work
travels through the event bus or work queue rather than synchronous request chains.
Configuration reads may be cached, but a routing decision must record the rule and
configuration version used so its outcome can be explained later.

## Responsibilities and Data Ownership

Each service owns the write model for its domain and exposes data to other services
through versioned APIs or durable domain events. Direct cross-service database
writes are prohibited.

| Domain | System of record | Owner | Key records |
| --- | --- | --- | --- |
| Tenant and configuration | Configuration store | Configuration service | Organizations, environments, destinations, routing rules, API-key metadata |
| Event lifecycle | Event store | Ingestion and lifecycle services | Accepted events, routing decisions, delivery jobs, attempts, replay references |
| Authentication material | Managed secrets / identity store | Identity service | Hashed API-key credentials, session identity, role assignments |
| Operational evidence | Observability and audit stores | Platform operations | Metrics, logs, traces, immutable audit records |

Customer event payloads are treated as sensitive application data. Access to them
is scoped to the owning organization and environment, minimized in logs, and
governed by retention and deletion policies. Destination secrets are encrypted and
only made available to the delivery service at send time.

## Technology Choices

These are intentional defaults for the MVP, not a commitment to a particular cloud
vendor. Equivalent managed services may be selected after cost, team expertise,
data-residency, and design-partner requirements are assessed.

| Concern | Recommended choice | Rationale |
| --- | --- | --- |
| Service runtime | Containerized stateless services | Independent scaling, repeatable deployments, and clear operational boundaries. |
| Public API | REST over HTTPS with versioned JSON schemas | Low-friction integration for early customers and an explicit compatibility boundary. |
| Relational data | PostgreSQL | Strong transactional guarantees for configuration and lifecycle records, mature tooling, and suitable early scale. |
| Durable asynchronous work | Managed queue or streaming platform | Absorbs bursts, isolates delivery latency, and supports retry/replay workflows. Choose a queue first unless ordered streams are proven necessary. |
| Cache / rate-limit coordination | Redis or managed equivalent | Efficient ephemeral rate-limit counters, short-lived configuration caches, and distributed coordination where necessary. |
| Secrets and keys | Cloud key-management and secrets service | Encryption, rotation, least privilege, and audit integration. |
| Object storage | Encrypted object storage for large payload spillover and exports | Keeps oversized data outside hot transactional tables; defer until required. |
| Observability | OpenTelemetry with managed metrics, logs, and tracing backends | Vendor-neutral instrumentation and end-to-end correlation. |
| Infrastructure delivery | Infrastructure as code and CI/CD | Reviewable, reproducible environments and controlled promotion. |

The MVP should begin as a modular service deployment, not an uncontrolled set of
microservices. Ingestion, routing, delivery, configuration, and console/API may
share a deployable codebase initially if their logical boundaries and data ownership
remain intact. Extract independently deployed services when load, isolation, team
ownership, or release cadence justifies the operational cost.

## Deployment Overview

### Environment topology

ProjectEcho operates separate development, staging, and production environments.
Production workspaces are logically isolated by organization and environment;
development environments must never share credentials, event data, or destination
secrets with production.

```text
Internet
   │
   ▼
CDN/WAF → Load Balancer / API Gateway
                   │
                   ▼
          Private application network
          ├─ Stateless API and worker containers
          ├─ Managed PostgreSQL (private access, backups)
          ├─ Managed queue / event bus
          ├─ Managed cache
          ├─ Secrets and key-management service
          └─ Observability and audit sinks
                   │
                   ▼
          Controlled outbound egress → Customer webhook endpoints
```

### Availability and operations

- Run stateless API and worker workloads across at least two availability zones
  where the selected provider supports it.
- Use managed database backups, point-in-time recovery, encrypted storage, and
  regularly tested restoration procedures.
- Autoscale ingestion and delivery workers independently using request rate and
  queue depth. Protect downstream destinations with per-destination concurrency
  and rate limits.
- Route all outbound webhook traffic through controlled egress; enforce DNS,
  address, and redirect protections to reduce server-side request forgery risk.
- Deploy immutable container artifacts through CI/CD with health checks, rollback
  capability, and staged promotion from development to production.
- Define service-level indicators for acceptance durability, queue delay, delivery
  success, and replay completion. Alerts must be actionable and tied to runbooks.

### MVP boundaries

The MVP is single-region and designed for multi-zone resilience within that region.
It does not promise active-active cross-region processing or zero data loss under a
regional provider outage. Multi-region recovery, expanded connectors, and advanced
schema governance are roadmap work and must not complicate the initial delivery
path prematurely.

## Key Architectural Decisions to Validate

1. Required event ordering: global ordering is not assumed; determine whether any
   ordering guarantee is needed per source, event type, or destination.
2. Event retention and deletion: define default retention, customer configuration,
   and handling of personal or regulated data before production onboarding.
3. Queue semantics: select a managed queue versus streaming platform based on
   measured throughput, routing complexity, replay needs, and ordering requirements.
4. Console scope: decide whether the MVP requires a full web console or an
   API-first operational interface for design partners.
5. Reliability targets: confirm the availability and latency targets in the Product
   Vision with design partners before they become service-level commitments.
