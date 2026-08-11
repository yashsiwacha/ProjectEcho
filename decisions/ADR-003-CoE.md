---
Document ID: ADR-003
Title: External Centers of Excellence (CoE)
Version: 1.0
Status: Accepted
Classification: Architecture | Governance
Owner: Principal Architect / CTO
Authority Level: 5
Primary Audience: All Engineers
Governed By: Founder Directive FD-0013
Review Cadence: Quarterly
Last Updated: 2026-08-11
Next Review: 2026-11-11
---

# ADR-003: External Centers of Excellence (CoE)

**Status:** Accepted
**Date:** 2026-08-11
**Category:** Governance | Engineering Constitution
**Directive:** Founder Directive FD-0013

---

## Context

Project Echo requires a consistent standard of excellence across five critical engineering disciplines: motion design, design systems, data visualization, interaction engineering, and autonomous AI. Without formal governance, individual teams risk diverging implementations, accessibility violations, and performance regressions.

To prevent this, the company permanently adopts best practices from five external reference ecosystems and encodes them as mandatory evaluation gates in the engineering workflow.

---

## Decision

We adopt five External Centers of Excellence, each backed by a reference ecosystem. All CoE evaluation gates are mandatory before any feature implementation begins. The gates are encoded in `AGENTS.md` (Section 6) and enforced at the Delivery Office orchestration layer.

---

## CoE Definitions

### 1. Motion Engineering CoE
**Reference:** Motion.dev, Motion AI Kit

**Responsibilities:**
- Production-grade animations and page transitions
- Shared layout animations and gesture systems
- Scroll-linked animation and parallax
- Spring physics, exit animations, micro-interactions
- Timeline orchestration
- prefers-reduced-motion compliance
- MotionScore optimisation and animation performance

**Mandatory Rules:**
- Every frontend feature must evaluate whether meaningful motion improves usability.
- Animations must never reduce accessibility or performance.

---

### 2. Design System CoE
**Reference:** KokonutUI

**Responsibilities:**
- Premium component design and composition
- Design tokens and Tailwind CSS architecture
- Loading states, empty states, error states
- Responsive design and dark mode
- WCAG 2.1 AA compliance

**Mandatory Rules:**
- Do not build custom components if an existing design-system pattern satisfies the requirement.
- New components must be reusable and token-driven.

---

### 3. Data Visualization CoE
**Reference:** Bklit UI

**Responsibilities:**
- Dashboard design and chart composition
- KPI visualization and analytics surfaces
- Monitoring, executive reporting, real-time dashboards

**Mandatory Rules:**
- Every dashboard must expose meaningful business KPIs.
- Charts must remain accessible (ARIA) and responsive.

---

### 4. Interaction Engineering CoE
**Reference:** Anime.js

**Responsibilities:**
- Advanced animation timelines and SVG animation
- Motion paths and scroll triggers
- Complex interaction systems and interactive storytelling
- High-performance GPU-composited animation

**Mandatory Rules:**
- Use only where interactions improve understanding or usability.
- Never animate for decoration alone.

---

### 5. Autonomous AI CoE
**Reference:** Manus

**Responsibilities:**
- Autonomous planning and multi-step task execution
- Research workflows and long-running task orchestration
- Agent collaboration, tool orchestration, self-verification
- Continuous execution loops

**Mandatory Rules:**
- Every large Founder request must first be decomposed into executable work packages before implementation begins.
- Use AI automation only where it improves quality, speed, or reliability.

---

## Consequences

### Positive
- Consistent quality across all product surfaces.
- Faster onboarding: engineers have clear reference ecosystems.
- Prevents ad-hoc implementations that violate accessibility or performance standards.
- Enables continuous learning and pattern reuse.

### Negative
- Additional evaluation time per feature (estimated 15-30 minutes).
- Engineers must stay current with reference ecosystem updates.

### Mitigations
- Quarterly CoE review to update guidelines.
- `docs/CoE_OVERVIEW.md` provides a quick-reference checklist.

---

## Continuous Learning Protocol

When a new pattern, component, animation technique, interaction model, dashboard design, or AI workflow is discovered from a reference ecosystem:

1. Evaluate against existing company standards.
2. Extract reusable knowledge into the Knowledge Platform.
3. Update `docs/CoE_OVERVIEW.md` and relevant templates.
4. Never duplicate functionality; never copy blindly.
5. Adapt patterns to YES architecture and DDD principles.

---

## Related Documents

- Founder Directive FD-0013 (source of authority)
- `AGENTS.md` Section 6 (enforcement in workflow)
- `docs/CoE_OVERVIEW.md` (developer quick-reference)
- ADR-006 (Identity and Authentication)
- ADR-011 (Secrets Management)
