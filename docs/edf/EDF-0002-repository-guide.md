# EDF-0002: Repository Engineering Guide

**Document ID:** EDF-0002
**Status:** FROZEN
**Version:** 1.0

---

## 1. Maven Module Layout
The repository is a single Maven multi-module project to enforce compile-time boundaries (ADR-0002).

```text
project-echo/
├── pom.xml (Parent)
├── echo-shared/        (Shared Kernel: IDs, Events, Exceptions)
├── echo-identity/      (Identity Context)
├── echo-taxonomy/      (Taxonomy Context)
├── echo-evidence/      (Evidence Context)
├── echo-intelligence/  (Intelligence Context - Rule Engine)
├── echo-mission/       (Mission Context - Read Models)
└── echo-application/   (Spring Boot Main App & Configuration)
```

## 2. Package Hierarchy
Every module strictly adheres to Hexagonal / Clean Architecture packages:

```text
com.projectecho.[module].
├── domain/             (Aggregates, Value Objects, Domain Services)
├── application/        (Use Cases, DTOs, Event Listeners)
├── infrastructure/     (JPA Entities, Repositories, REST Clients)
└── presentation/       (REST Controllers)
```

## 3. Shared Kernel Structure
The `echo-shared` module is highly restricted.
- **Allowed**: Base classes (`AggregateRoot`), primitive Value Objects (`PassportId`), and Global Exceptions (`ResourceNotFoundException`).
- **Integration Events**: Owned exclusively by `echo-shared` (ADR-014).
- **Forbidden**: Business logic, domain models, JPA annotations.

## 4. Module Visibility Rules
- All Domain entities and services should be `package-private` where possible to prevent leakage.
- Public visibility is reserved strictly for Application Services (Use Cases) and Integration Events.
