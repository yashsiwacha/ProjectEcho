# Database Rules

## Purpose

This rule defines database engineering practices for ProjectEcho.

It covers persistence, schema evolution, repository design, and database
performance.

Business rules remain defined by governance and architecture documents.

---

# Database Philosophy

The database is a persistent representation of the domain.

Schema design should prioritize:

- correctness
- consistency
- maintainability
- traceability

Never design the schema around temporary implementation shortcuts.

---

# Database Technology

ProjectEcho uses:

- PostgreSQL
- Spring Data JPA
- Hibernate
- Flyway

Always follow existing repository conventions.

---

# Entity Design

Entities represent persistent domain concepts.

Keep entities:

- cohesive
- focused
- explicit

Avoid:

- massive entities
- unrelated responsibilities
- unnecessary inheritance

Do not expose entities directly through APIs.

---

# Repository Design

Repositories exist only for persistence.

Repositories should:

- load data
- save data
- execute persistence queries

Repositories should NOT contain:

- business rules
- orchestration
- validation
- API logic

---

# Relationships

Prefer explicit relationships.

Avoid unnecessary bidirectional mappings.

Choose fetch strategies intentionally.

Do not rely on default eager loading.

---

# Transactions

Use transactions only where required.

Keep transactions:

- short
- explicit
- predictable

Avoid long-running transactions.

---

# Schema Evolution

All schema changes must be versioned.

Use Flyway migrations.

Never modify an existing migration after it has been applied.

Always create a new migration.

---

# Constraints

Use database constraints to protect data integrity.

Prefer:

- PRIMARY KEY
- FOREIGN KEY
- UNIQUE
- NOT NULL
- CHECK

Application validation complements database constraints.

It does not replace them.

---

# Indexing

Indexes should support real query patterns.

Avoid:

- duplicate indexes
- unnecessary indexes
- indexing every column

Measure before optimizing.

---

# Queries

Prefer readable queries.

Avoid unnecessary complexity.

Prevent N+1 query problems.

Load only the required data.

---

# Performance

Optimize only after identifying a measurable bottleneck.

Prefer:

- proper indexes
- efficient queries
- pagination
- batching

Avoid premature optimization.

---

# Auditing

When appropriate, preserve:

- creation timestamps
- update timestamps
- audit metadata

Avoid deleting valuable historical information without explicit requirements.

---

# Data Integrity

Protect consistency.

Never bypass persistence rules.

Prefer explicit state transitions.

Validate assumptions before writing data.

---

# Database Checklist

Before completing persistence work, verify:

- Entities remain cohesive.
- Repository responsibilities are respected.
- Transactions are appropriate.
- Flyway migration added if schema changed.
- Constraints preserved.
- Queries remain efficient.
- N+1 problems avoided.
- No entity leakage through APIs.

