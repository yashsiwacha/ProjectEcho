# ProjectEcho User Flows

> Status: Draft
> Version: 1.0.0
> Owner: Product Team
> Last Updated: 2026-07-29

---

# Purpose

This document defines the primary user journeys within ProjectEcho.

User flows describe how users accomplish tasks, how screens connect, and how decisions influence navigation.

These flows serve as the foundation for wireframes, interaction design, frontend routing, backend APIs, and AI-assisted experiences.

---

# Design Principles

Every user flow should:

- Minimize friction
- Reduce cognitive load
- Be explainable
- Encourage progress
- End with a clear next action

---

# Flow 1 — User Onboarding

Goal:
Enable a new user to create an account and receive an initial Career Passport.

Flow:

Landing Page
↓

Sign Up
↓

Email Verification
↓

Create Profile
↓

Define Career Goals
↓

Add Initial Skills
↓

Connect External Accounts (Optional)
↓

Upload Initial Evidence
↓

Generate Initial Career Passport
↓

Dashboard

Success Criteria:

- User completes onboarding
- Career Passport is generated
- Dashboard displays initial insights

---

# Flow 2 — Upload Evidence

Goal:
Allow users to continuously strengthen their Career Passport.

Flow:

Dashboard
↓

Evidence
↓

Upload Evidence
↓

Choose Evidence Type

- Project
- Certification
- Work Experience
- GitHub Repository
- Assessment

↓

AI Processing
↓

Competency Analysis
↓

Evidence Verification
↓

Career Passport Updated

Success Criteria:

- Evidence stored
- Competencies updated
- Timeline updated
- AI recommendations refreshed

---

# Flow 3 — Career Passport Exploration

Goal:
Allow users to understand their professional profile.

Flow:

Dashboard
↓

Career Passport
↓

Overview
↓

Competencies
↓

Evidence
↓

Timeline

↓

Recommendations

Success Criteria:

- User understands strengths
- User identifies improvement areas
- User explores supporting evidence

---

# Flow 4 — AI Recommendations

Goal:
Help users improve career readiness.

Flow:

Dashboard
↓

Recommendations
↓

Recommendation Detail
↓

Reasoning
↓

Supporting Evidence
↓

Suggested Actions
↓

Learning Resources
↓

Progress Tracking

Success Criteria:

- Recommendation understood
- User begins improvement plan

---

# Flow 5 — Learning Journey

Goal:
Guide users toward competency growth.

Flow:

Recommendation
↓

Learning Path
↓

Learning Resource
↓

Complete Learning
↓

Upload Evidence
↓

AI Reassessment
↓

Competency Improvement

Success Criteria:

- Skill gap reduced
- Competency score increases
- Career readiness improves

---

# Flow 6 — Goal Management

Goal:
Allow users to define and monitor career objectives.

Flow:

Dashboard
↓

Goals
↓

Create Goal
↓

Assign Target Competencies
↓

Track Progress
↓

Receive AI Guidance
↓

Goal Achieved

Success Criteria:

- Goal tracked successfully
- Progress remains visible
- AI adapts recommendations

---

# Flow 7 — Returning User

Goal:
Provide immediate visibility into progress.

Flow:

Login
↓

Dashboard

↓

Recent Activity

↓

New AI Insights

↓

Updated Recommendations

↓

Continue Journey

Success Criteria:

- User immediately understands changes
- User has a clear next action

---

# Error Handling

Every flow should gracefully handle:

- Network failures
- AI processing delays
- Invalid uploads
- Missing information
- Authentication failures

Users should always receive:

- Clear explanation
- Recovery options
- Retry capability

---

# UX Guidelines

Every screen should communicate:

1. Where am I?
2. What has changed?
3. What should I do next?

---

# Future User Flows

Future releases may introduce:

- Recruiter Review Flow
- Organization Dashboard Flow
- Interview Preparation Flow
- Mentor Collaboration Flow
- Mobile Quick Capture Flow
- Browser Extension Flow
- VS Code Integration Flow

These should integrate with the existing architecture without requiring major structural changes.

---

# Flow Governance

New features should extend existing user journeys whenever possible rather than introducing isolated workflows.

Every new flow must:

- Begin with a clear user goal
- Minimize unnecessary steps
- Preserve navigation consistency
- Maintain evidence-first principles
- Support explainable AI interactions
