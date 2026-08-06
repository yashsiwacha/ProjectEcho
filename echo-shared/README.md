# ProjectEcho Shared Kernel (`echo-shared`)

The Shared Kernel is the authoritative, immutable baseline that all other ProjectEcho modules depend on. It enforces the foundational Domain-Driven Design constructs and Integration Event contracts.

## 1. Package Boundaries

### `com.projectecho.shared.domain`
Contains the absolute primitives for the domain model:
- `AggregateRoot`: Abstract base class enforcing Identity, Optimistic Locking (Version), and Auditing (Timestamps).
- `TrustTier`: Sealed interface defining the 3 permissible trust levels (`High`, `Medium`, `Low`) governed by BR-01.
- `ValueObjects`: Standardized identifiers (`PassportId`, `SkillId`) to prevent primitive obsession within bounded contexts.

### `com.projectecho.shared.events`
Contains cross-boundary Integration Events.
- **Rule**: Integration Events must rely **exclusively on language primitives** (e.g., `UUID`, `String`, `int`).
- **Rule**: Integration Events must never reference Java Domain Value Objects from `com.projectecho.shared.domain` to maximize serialization safety and structural decoupling (ADR-014).
- **Rule**: Every event must implement the `IntegrationEvent` interface to guarantee standard headers (eventId, version, correlationId, causationId, timestamp, aggregateId).

### `com.projectecho.shared.exception`
Contains the standard error hierarchy (`DomainException`, `ResourceNotFoundException`) ensuring uniform HTTP 4xx mapping across all REST presentations.

## 2. Dependency Rules
- `echo-shared` **MUST NOT** depend on any other ProjectEcho module.
- All domain modules (`echo-identity`, `echo-taxonomy`, etc.) **MUST** depend on `echo-shared`.
- The `echo-shared` module does not contain Hexagonal layers (`application`, `infrastructure`, `presentation`).

## 3. Extension Guidelines
Do not add arbitrary utility classes (e.g., `StringUtils`, `DateUtils`) to the Shared Kernel. The Shared Kernel is strictly reserved for Domain Contracts and Event Contracts.

## 4. Versioning Policy
Any change to `echo-shared` fundamentally affects the entire repository. Changes must be strictly backward compatible or coordinated across all modules simultaneously.
