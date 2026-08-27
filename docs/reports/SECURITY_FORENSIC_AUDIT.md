# ProjectEcho Security Forensic Audit

## Overview
This audit was performed autonomously as part of Phase 1 of the ProjectEcho V1 Product Engineering Quality Assurance sweep.

## Search Methodology
A codebase-wide search was executed for key security anti-patterns: `TODO`, `FIXME`, `HACK`, `mock`, `admin`, `console.log`, `permitAll`, `localStorage`, hardcoded credentials, and missing authorization controls.

## Findings

### P0 (Release Blockers)
1. **Hardcoded Admin Bypass (`AuthController.java:26-29`)**
   - The `login` endpoint explicitly checks against a hardcoded `adminUsername` and `adminPassword` defined via `@Value`, completely bypassing the database hash verification for that user.
2. **Universal Admin Escalation (`JwtTokenProvider.java:67`)**
   - During JWT parsing in `getAuthentication()`, *every single user* is automatically instantiated with `AuthorityUtils.createAuthorityList("ROLE_ADMIN")`. This means any valid JWT grants global admin privileges.
3. **Overly Permissive Actuator (`SecurityConfig.java:54`)**
   - The security filter chain applies `.permitAll()` to `/actuator/**`, exposing internal Spring Boot telemetry, env properties, health, and potentially heap dumps to the public internet.
4. **LocalStorage Token Storage (`AuthContext.tsx` & `api.ts`)**
   - JWT tokens are stored persistently in `localStorage.setItem('echo_auth_user', ...)` in the frontend. This leaves the session highly vulnerable to XSS attacks extracting the token.

### P1 (Serious Defects)
1. **Weak Default Secret Key (`JwtTokenProvider.java:22`)**
   - The JWT secret defaults to a base64 string hardcoded in the source code (`dGhpcy1pcy...`). If not overridden by environment variables, production instances could share a compromised signing key.

### P2 (Technical Debt)
1. **Environment Configuration Leaks (`.env.example`)**
   - Contains default `admin@projectecho.local` pgAdmin credentials.
2. **Hardcoded Test Passwords (`EndToEndApiIntegrationTest.java`)**
   - Integration tests rely on the hardcoded `adminpwd` rather than creating isolated test users with programmatic passwords.

## Next Steps
These findings inform the implementation strategy for Phases 2-9 of the QA mandate. P0 and P1 findings must be resolved immediately before testing cross-user authorization limits.
