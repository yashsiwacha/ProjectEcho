# Recommendations Wireframe

> Status: Draft
> Version: 1.0.0
> Owner: UX Team
> Last Updated: 2026-07-29

---

# Purpose

The Recommendations page transforms competency analysis into personalized, explainable, and actionable guidance.

Every recommendation should help users improve their Career Passport through meaningful actions backed by evidence.

The page should answer:

> "What should I do next, and why?"

---

# Desktop Layout


┌──────────────────────────────────────────────────────────────────────────────┐
│ Top Navigation │
├──────────────┬───────────────────────────────────────────────────────────────┤
│ Sidebar │ │
│ │ Recommendations │
│ Dashboard │ Search __________________ Filter ▼ Sort ▼ │
│ Passport │ │
│ Evidence │ ┌────────────┐ ┌────────────┐ ┌────────────┐ │
│ Competencies │ │High Priority││In Progress ││Completed │ │
│ Learning │ └────────────┘ └────────────┘ └────────────┘ │
│ Goals │ │
│ Settings │ AI Recommendation Feed │
│ │────────────────────────────────────────────────────────────── │
│ │ 🔥 Improve REST API Testing │
│ │ Priority: High │
│ │ Impact: High │
│ │ Effort: Medium │
│ │ Why: Limited testing evidence detected │
│ │ [Start] [View Reasoning] │
│ │────────────────────────────────────────────────────────────── │
│ │ ☁ Learn Docker Compose │
│ │ Priority: Medium │
│ │ Impact: Medium │
│ │ Effort: Low │
│ │ [Start] │
│ │────────────────────────────────────────────────────────────── │
│ │ Recommendation Timeline │
└──────────────┴───────────────────────────────────────────────────────────────┘


---

# Page Header

Display:

- Page Title
- Search
- Filter
- Sort

Primary CTA:

Refresh Recommendations

---

# KPI Cards

Display:

- High Priority Recommendations
- Recommendations In Progress
- Completed Recommendations
- Estimated Career Impact

---

# Recommendation Feed

Each recommendation card displays:

- Title
- Category
- Priority
- Estimated Impact
- Estimated Effort
- Confidence Level
- Estimated Completion Time
- Related Competencies

Actions:

- Start
- Save for Later
- Dismiss
- View Explanation

---

# AI Explanation Panel

Selecting "View Explanation" opens a detail panel.

Display:

## Observation

Example:

Backend testing competency confidence is lower than comparable backend competencies.

---

## Supporting Evidence

Display linked:

- Projects
- Certifications
- GitHub Activity
- Assessments

---

## Reasoning

Explain:

- Why this recommendation exists
- Which competency gap it addresses
- Why it is prioritized now

---

## Expected Outcome

Estimate:

- Competency improvement
- Career Readiness improvement
- Related goals advanced

---

## Suggested Actions

Display a checklist such as:

- Complete Spring Boot Testing module
- Add integration tests
- Upload updated project evidence

---

# Recommendation Categories

Support:

- Skills
- Projects
- Learning
- Evidence
- Career Planning
- Certifications

---

# Filters

Support:

- Priority
- Category
- Status
- Estimated Effort
- Expected Impact
- Confidence

---

# Search

Search by:

- Technology
- Competency
- Recommendation Title
- Category

---

# Sorting

Support:

- Highest Priority
- Highest Impact
- Lowest Effort
- Newest
- AI Confidence

---

# Recommendation Status

Supported states:

- New
- In Progress
- Completed
- Saved
- Dismissed

---

# Empty State

If no recommendations exist:

Display:

"You're currently on track.

Continue adding evidence to receive personalized recommendations."

Primary CTA:

Upload Evidence

---

# Loading State

Skeleton loaders for:

- Recommendation Cards
- KPI Cards
- AI Explanation Panel

---

# Error State

Possible errors:

- AI unavailable
- Recommendation generation failed
- Network issue

Display:

- Explanation
- Retry button
- Help link

---

# Responsive Behaviour

Desktop:

- Two-column layout
- Recommendation list + explanation panel

Tablet:

- Single-column
- Expandable recommendation details

Mobile:

- Card list
- Full-screen recommendation details

---

# Primary User Journey

Open Recommendations

↓

Review Priority Items

↓

Understand AI Reasoning

↓

Start Recommendation

↓

Complete Action

↓

Upload Evidence

↓

Competency Updated

↓

Recommendation Completed

---

# Design Priorities

1. Explainability
2. Actionability
3. Personalization
4. Transparency
5. Measurable impact

---

# Success Criteria

The Recommendations page succeeds when users understand why each recommendation exists, trust the AI reasoning, complete meaningful actions, and observe measurable improvements in their competencies and Career Passport.
