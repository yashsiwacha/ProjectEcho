# Competencies Wireframe

> Status: Draft
> Version: 1.0.0
> Owner: UX Team
> Last Updated: 2026-07-29

---

# Purpose

The Competencies page enables users to understand, explore, and improve their professional capabilities through evidence-backed competency analysis.

Every competency displayed on this page must be explainable, measurable, and directly linked to supporting evidence.

The page should answer:

> "What am I good at, how do we know, and what should I improve next?"

---

# Desktop Layout


┌──────────────────────────────────────────────────────────────────────────────┐
│ Top Navigation │
├──────────────┬───────────────────────────────────────────────────────────────┤
│ Sidebar │ │
│ │ Competencies │
│ Dashboard │ Search _______________________ Filter ▼ Sort ▼ │
│ Passport │ │
│ Evidence │ ┌────────────┐ ┌────────────┐ ┌────────────┐ │
│ Competencies │ │Verified │ │Avg Score │ │Top Category│ │
│ Learning │ └────────────┘ └────────────┘ └────────────┘ │
│ Goals │ │
│ Settings │ Competency Categories │
│ │────────────────────────────────────────────────────────────── │
│ │ Backend Development │
│ │ Score: 88 Confidence: High │
│ │ Progress ██████████░░ │
│ │ Linked Evidence (12) │
│ │ │
│ │ Database Engineering │
│ │ Score: 82 Confidence: Medium │
│ │ │
│ │ Cloud & DevOps │
│ │ │
│ │ System Design │
│ │ │
│ │ Trend & Growth Charts │
└──────────────┴───────────────────────────────────────────────────────────────┘


---

# Page Header

Display:

- Page Title
- Search
- Filter
- Sort

---

# KPI Cards

Display:

- Verified Competencies
- Average Competency Score
- Highest Competency
- Fastest Growing Competency

---

# Competency Categories

Group competencies into logical domains.

Example:

- Backend Development
- Programming Languages
- Databases
- Cloud & DevOps
- Software Engineering
- Testing
- Security
- AI & Data

---

# Competency Card

Each competency includes:

- Name
- Category
- Score (0–100)
- Confidence Level
- Growth Trend
- Evidence Count
- Last Updated

Actions:

- View Details
- Explore Evidence
- View Recommendations

---

# Competency Detail Panel

Selecting a competency opens a detailed panel showing:

## Overview

- Description
- Current Score
- Confidence
- Maturity Level

---

## Supporting Evidence

Display all linked:

- Projects
- Work Experience
- Certifications
- Assessments
- GitHub Repositories

Each evidence item links directly to its detail page.

---

## Growth Timeline

Display:

- Historical scores
- Milestones
- Recently added evidence
- Improvement trend

---

## AI Explanation

Every competency includes:

Observation

↓

Supporting Evidence

↓

Confidence

↓

Reasoning

↓

Suggested Next Step

Users should understand exactly how the competency score was derived.

---

# Competency Hierarchy

Display expandable hierarchy.

Example:

Backend Development

├── REST APIs

├── Spring Boot

├── Authentication

├── Microservices

└── API Security

---

# Filters

Support:

- Category
- Confidence
- Score Range
- Growth Trend
- Evidence Count

---

# Search

Search by:

- Competency
- Technology
- Framework
- Language

---

# Sorting

Support:

- Highest Score
- Lowest Score
- Highest Confidence
- Recently Updated
- Fastest Growth

---

# Empty State

If no competencies exist:

Display:

"No competencies have been inferred yet.

Upload evidence to begin building your competency profile."

Primary CTA:

Upload Evidence

---

# Loading State

Skeleton loaders for:

- Competency Cards
- Charts
- Evidence Lists
- Detail Panel

---

# Error State

Possible errors:

- AI analysis unavailable
- Competencies failed to load
- Network issue

Display:

- Explanation
- Retry option
- Help link

---

# Responsive Behaviour

Desktop:

- Two-column layout
- Detail side panel

Tablet:

- Single-column
- Expandable detail section

Mobile:

- Accordion cards
- Bottom navigation
- Full-screen detail view

---

# Primary User Journey

Open Competencies

↓

Review Scores

↓

Select Competency

↓

Inspect Supporting Evidence

↓

Understand AI Reasoning

↓

Follow Recommendation

↓

Improve Competency

---

# Design Priorities

1. Explainability
2. Evidence traceability
3. Transparency
4. Growth visualization
5. Actionability

---

# Success Criteria

The Competencies page succeeds when users can understand every competency, trust its score, inspect the supporting evidence, and identify the next action needed to improve.
