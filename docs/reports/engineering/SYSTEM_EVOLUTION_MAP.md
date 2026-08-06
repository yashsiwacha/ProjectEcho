# SYSTEM EVOLUTION MAP

## 1. Current Architecture (Version 1 Target)
- **Paradigm**: Modular Monolith.
- **Goal**: Strict domain boundaries (bounded contexts) communicating via domain events (`echo-shared`) using an Asynchronous Infrastructure Backbone.
- **Components**: `echo-identity`, `echo-mission`, `echo-evidence`, `echo-rule-engine`, `echo-taxonomy`, `echo-intelligence`.

## 2. Version 2 Possibilities (1–3 Years)
- **Paradigm**: Event-Driven Microservices / Macroservices.
- **Potential Module Extraction**:
  - `echo-intelligence` (Recommendation and AI Reasoning Layer) will likely require separate scaling and specialized hardware (GPUs), making it the prime candidate for microservice extraction.
  - `echo-rule-engine` may be extracted into a standalone highly-available cluster to process massive amounts of rules in parallel.

## 3. Future Bounded Contexts (3–5 Years)
- **Analytics & Reporting Context**: For deriving organizational insights from anonymized Career Passports.
- **Federation Context**: To support cross-organizational verifiable credentials (B2B2C portability).
- **Gamification / Engagement Context**: Evolving "Missions" into active progression systems.

## 4. Aggregate & Event Evolution
- **Event Versioning**: The `echo-shared` event catalog will require Upcasters (Event Sourcing pattern) to handle version evolution without breaking backward compatibility.
- **ReadinessAssessment Evolution**: From a boolean/score output to a temporal trajectory (predicting *when* someone will be ready based on velocity).

## 5. Rule Engine & DecisionTrace Evolution (5–10 Years)
- **DecisionTrace -> Decision Graph**: As the AI Reasoning Layer takes over recommendations, a linear `DecisionTrace` will bottleneck. AI decisions are not linear; they are graphs of weighted probabilities. The system must evolve `DecisionTrace` into a `DecisionGraph` to provide Explainability for AI.
- **Persistence Evolution**: Moving from relational tables (`readiness_assessments`) to a specialized Graph Database (Neo4j) for `DecisionGraph` or an Event Store for raw Rule Engine outputs.

## 6. Architecture Evolution Review (Bottleneck Forecasting)

### In 6 Months:
- **Bottleneck**: The `echo-shared` module becoming a dumping ground for all events, leading to massive merge conflicts.
- **Mitigation**: Introduce bounded-context specific event contracts instead of a single shared JAR.

### In 1 Year:
- **Bottleneck**: Synchronous database transactions slowing down `echo-rule-engine`.
- **Mitigation**: Move entirely to CQRS.

### In 3 Years:
- **Bottleneck**: PII regulations (GDPR/CCPA) complicating the retention of `DecisionTrace` records if they include raw user data.
- **Mitigation**: Strict cryptographic separation between identity markers and evaluation outcomes.

### In 5 Years:
- **Bottleneck**: AI reasoning complexity outgrowing standard Java microservices.
- **Mitigation**: Python/Go specialized services strictly communicating over gRPC with the Java Modular Monolith.

### In 10 Years:
- **Bottleneck**: Global Career Passport portability causing identity collision and data sovereignty issues across different geopolitical regions.
- **Mitigation**: Decentralized Identity (DID) and zero-knowledge proofs (ZKP) for Evidence and Readiness.
