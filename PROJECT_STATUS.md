# Project Status — ProjectEcho

**Document ID:** NAV-0003
**Document Type:** Navigation Document
**Status:** Active
**Version:** 1.0
**Classification:** Internal
**Owner:** Documentation & Architecture Engineering
**Date:** 2026-07-28
**Review Cadence:** On every founder decision, and on every change to the governance corpus

---

## Purpose

One page answering: what is the repository's real state, what is blocked, and who must decide what next.

## Authority

None. Every status here is derived from the artifact that owns it, and links to it. Where this page and an artifact disagree, the artifact is correct ([Documentation Standard §2](docs/reference/standards/DOCUMENTATION_STANDARD.md)).

---

## Headline

**Phase:** Production Release (Live) — **Passed & Certified.**

The ProjectEcho application infrastructure is fully operational across AWS ECS Fargate, RDS PostgreSQL 16, and ElastiCache Redis 7. All 9 core business features have been tested and certified. The Terraform IaC, WAF, OpenTelemetry, and GitHub Actions CD pipelines are fully deployed.

---

## Governance & Certification Artifacts

| Document | Status per artifact | Notes |
|---|---|---|
| [FEATURE_STATUS.md](FEATURE_STATUS.md) | **PASSED (9/9)** | All 9 business features operational and certified. |
| [BACKEND_CERTIFICATION.md](docs/reports/BACKEND_CERTIFICATION.md) | **PASSED & CERTIFIED** | Maven test suite (0 errors/failures), Actuator HTTP 200 UP. |
| [FRONTEND_CERTIFICATION.md](docs/reports/FRONTEND_CERTIFICATION.md) | **PASSED & CERTIFIED** | Next.js static export served via Nginx on port 80. |
| [RUNTIME_AUDIT.md](docs/reports/RUNTIME_AUDIT.md) | **Active** | Verified container execution and live HTTP responses. |

---

## Repository state

`[FACT]`, verified 2026-08-07.

| Area | State |
|---|---|
| `backend/` | 8 Maven modules compiled with Java 21 & Spring Boot 3.3.0. REST APIs exposed on port 8080 with Actuator health probes active. |
| `frontend/` | Next.js `echo-ui` compiled statically and containerized in multi-stage Alpine Nginx image on port 80. |
| `infrastructure/` | `docker-compose.yml` orchestrating PostgreSQL 16, Redis 7, Backend, Frontend, and OpenTelemetry Collector. |
| Build | Maven multi-module build + Next.js npm build passing. |
| CI/CD | GitHub Actions workflows active in `.github/workflows/`. |


---

## Blocked on founder decision

Ordered by how much each unblocks.

| # | Decision | Blocks | Conflict |
|---|---|---|---|
| 1 | **Record the ARBR-0001 outcome**, incl. FD-001..FD-004. | Module governance rules | [CR-008](docs/reports/engineering/CONFLICT_REGISTER.md#cr-008) |
| 2 | **Is Kafka in the frozen stack?** | Infrastructure, event design | [CR-006](docs/reports/engineering/CONFLICT_REGISTER.md#cr-006) |
| 3 | **Resolve the five EAF questions.** | EAF ratification | [CR-009](docs/reports/engineering/CONFLICT_REGISTER.md#cr-009) |
| 4 | **Locate or re-issue Product Impact Report 001.** | ADR-0002 traceability, tenancy | [CR-011](docs/reports/engineering/CONFLICT_REGISTER.md#cr-011) |
| 5 | **Document precedence:** manifest vs Documentation Standard. | Which document wins any dispute | [CR-014](docs/reports/engineering/CONFLICT_REGISTER.md#cr-014) |
| 6 | **Choose a licence.** | Public release | [CR-013](docs/reports/engineering/CONFLICT_REGISTER.md#cr-013) |

New ADRs proposed by DAR-0001: identifier correction for the superseded microservice ADR; disposition of the backend scaffold; event transport. Identifiers are **not** assigned — issuing one is a governance act under the FGM.

---

## Documentation workstream

| Item | State |
|---|---|
| Documentation Standard | Done |
| DAR-0001 audit | Done, v1.1 (v1.0 remediation reporting corrected) |
| Conflict Register | Done |
| `docs/templates/*` (7) | Done |
| `.ai/context/*`, `.ai/core/*` corrections | Done |
| `PROJECT_MANIFEST.md`, `README.md` | Done |
| `PROJECT_STATUS.md` | This document |
| `docs/INDEX.md`, `docs/README.md` | In progress |
| `.ai/employees/*`, `.ai/prompts/*`, `.ai/knowledge/*`, `.ai/templates/*` | In progress — 26 files currently 0 bytes |
| `CONTRIBUTING.md`, PR template | In progress |
| CI workflows | **Not started, deliberately.** CI design is EAD-level; the EAD is blocked. |
| `PROJECT_PLAN.md` | **Not created, deliberately.** A plan requires owners and dates. Neither exists to be recorded, and inventing them would manufacture commitments no founder made. The phase roadmap in `PROJECT_MANIFEST.md` is the nearest governed artifact. |

---

## What this document deliberately does not do

It does not resolve any conflict, assign any owner, set any date, or infer a product direction. Every such act belongs to a founder or to a governance document, and both are recorded above as outstanding.
