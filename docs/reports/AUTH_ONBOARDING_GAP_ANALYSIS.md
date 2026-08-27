# ProjectEcho - Auth & Onboarding Gap Analysis

## Overview
This audit evaluates the current state of authentication, onboarding, and dashboard personalization in the ProjectEcho architecture.

## 1. Authentication & Security
- **JWT Implementation**: **PARTIAL**
  - A `JwtTokenProvider` and `JwtAuthenticationFilter` exist.
  - Generates signed tokens.
- **User Domain / Schema**: **MISSING**
  - No `User` entity exists in the database. 
  - `echo-identity` only contains `CareerPassport`.
- **Sign-In Endpoint**: **MOCKED**
  - `AuthController.java` exposes `/api/v1/auth/login`.
  - Hardcoded to `admin` / `adminpwd` properties. Does not validate against a database.
- **Sign-Up Endpoint**: **MISSING**
  - No registration endpoint exists.
- **Password Security**: **MISSING**
  - No BCrypt or password hashing is implemented.
- **Frontend Auth Routes**: **MISSING**
  - No `/sign-in` or `/sign-up` UI exists.

## 2. Onboarding Experience
- **Questionnaire UX**: **MISSING**
  - Does not exist.
- **Career Passport Initialization**: **STUBBED**
  - Currently a basic form on the `/passport` page (`PassportPage.tsx`).
  - Lacks multi-step logic, skill selection, and preference collection.
- **Seamless Transition**: **MISSING**
  - Users are dropped directly into pages without an initialization flow.

## 3. Product & Dashboard
- **Real Backend Integration**: **PARTIAL**
  - APIs exist for Skills, Evidence, and Missions.
  - However, the frontend Dashboard (`page.tsx`) does not contextually link data to the *current authenticated user*.
- **Empty States**: **MOCKED/MISSING**
  - No guided empty states to prompt users to build their passport.
- **Premium Visual System**: **PARTIAL**
  - The 3D elements look great, but the overall UI lacks the strict contrast, typography scale, and layout sophistication required by the executive directive.

## Summary of Required Backend Architecture Additions
1. Create a true `User` aggregate in `echo-identity` (or an `echo-auth` context) with `email` and `hashed_password`.
2. Rewrite `AuthController` to handle real Registration and Database-backed Login using `BCryptPasswordEncoder`.
3. Update `CareerPassport` to be linked to the authenticated `User` context (e.g. `userId` relation).
