# ADR-012: Containerization and Deployment Topology

**Status:** Proposed
**Date:** 2026-07-29
**Category:** Deployment

## Context
We need a standard mechanism to deploy the unified `echo-bootstrap` monolithic JAR across environments (Local, Staging, Prod).

## Decision
We will deploy the application as a **Docker Container** running an Alpine Linux/Java 21 JRE base image. We will use `docker-compose` for local orchestration of the app, Postgres, and Redis.

## Consequences
- **Positive:** Identical runtime environments from developer laptops to production servers.
- **Negative:** Increases local memory overhead compared to bare-metal execution.
