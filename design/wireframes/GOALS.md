# Goals Wireframe

> Status: Draft
> Version: 1.0.0
> Owner: UX Team
> Last Updated: 2026-07-29

---

# Purpose

The Goals page enables users to define, track, and achieve career objectives through measurable milestones, competency development, evidence generation, and AI-assisted planning.

Goals act as the strategic layer of ProjectEcho, aligning every recommendation, learning activity, and evidence submission with long-term career outcomes.

The page should answer:

> "What am I working toward, and how close am I?"

---

# Desktop Layout


┌──────────────────────────────────────────────────────────────────────────────┐
│ Top Navigation │
├──────────────┬───────────────────────────────────────────────────────────────┤
│ Sidebar │ │
│ │ Career Goals │
│ Dashboard │ Search __________________ Filter ▼ Sort ▼ [+ New Goal] │
│ Passport │ │
│ Evidence │ ┌────────────┐ ┌────────────┐ ┌────────────┐ │
│ Competencies │ │Active Goals│ │Completed │ │On Track │ │
│ Learning │ └────────────┘ └────────────┘ └────────────┘ │
│ Goals │ │
│ Settings │ Primary Career Goal │
│ │ Become Backend Engineer │
│ │ Progress ███████░░░ 72% │
│ │ ETA: 3 Months │
│ │ │
│ │ Milestones │
│ │ ✓ Build Portfolio │
│ │ ✓ Master Spring Boot │
│ │ ○ Improve System Design │
│ │ ○ Deploy Production Project │
│ │ ○ Complete Mock Interviews │
│ │ │
│ │ AI Goal Insights │
│ │ │
└──────────────┴───────────────────────────────────────────────────────────────┘


---

# Page Header

Display:

- Page Title
- Search
- Filter
- Sort

Primary CTA:

Create Goal

---

# KPI Cards

Display:

- Active Goals
- Completed Goals
- Goals On Track
- Career Readiness Progress

---

# Goal Card

Each goal displays:

- Title
- Description
- Progress
- Target Date
- Priority
- Status
- Estimated Completion
- Career Impact

Actions:

- View Goal
- Edit
- Archive
- Complete

---

# Goal Detail

Each goal contains:

## Objective

Overall career objective.

---

## Milestones

Each milestone includes:

- Title
- Status
- Expected Outcome
- Related Competencies
- Related Evidence

---

## Progress Timeline

Display:

- Goal creation
- Milestone completion
- Evidence submitted
- Competencies improved
- Recommendations completed

---

## Related Competencies

Display competencies that contribute toward the goal.

Example:

Backend Development

System Design

Docker

REST APIs

Communication

---

## Required Evidence

Display suggested evidence:

- Projects
- Certifications
- GitHub Repositories
- Work Experience
- Assessments

---

## AI Roadmap

The AI generates:

Current Position

↓

Remaining Milestones

↓

Recommended Learning

↓

Recommended Projects

↓

Expected Timeline

↓

Career Readiness Forecast

---

# Goal Categories

Support:

- Career
- Skills
- Portfolio
- Certification
- Interview Preparation
- Leadership
- Personal Development

---

# Filters

Support:

- Status
- Priority
- Target Date
- Category
- Progress

---

# Search

Search by:

- Goal Name
- Technology
- Career Role
- Competency

---

# Sorting

Support:

- Closest Deadline
- Highest Priority
- Most Progress
- Least Progress
- Recently Created

---

# Empty State

Display:

"You don't have any career goals yet.

Create your first goal to receive personalized recommendations and learning paths."

Primary CTA:

Create Goal

---

# Loading State

Skeleton loaders for:

- Goal Cards
- Milestones
- AI Insights
- Progress Charts

---

# Error State

Possible errors:

- Goal load failed
- AI roadmap unavailable
- Network issue

Display:

- Explanation
- Retry
- Help

---

# Responsive Behaviour

Desktop:

- Two-column layout

Tablet:

- Single-column
- Expandable milestones

Mobile:

- Card-based goals
- Full-screen goal detail
- Floating Create Goal button

---

# Primary User Journey

Create Goal

↓

AI Generates Roadmap

↓

Complete Learning

↓

Upload Evidence

↓

Competencies Improve

↓

Milestones Complete

↓

Goal Achieved

---

# Design Priorities

1. Long-term planning
2. Measurable progress
3. AI guidance
4. Competency alignment
5. Evidence-driven achievement

---

# Success Criteria

The Goals page succeeds when users consistently progress toward career objectives through measurable milestones, AI guidance, competency growth, and verifiable evidence.
