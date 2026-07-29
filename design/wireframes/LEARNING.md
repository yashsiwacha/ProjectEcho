# Learning Wireframe

> Status: Draft
> Version: 1.0.0
> Owner: UX Team
> Last Updated: 2026-07-29

---

# Purpose

The Learning page orchestrates personalized learning journeys based on competency gaps, career goals, and AI recommendations.

Unlike traditional LMS platforms, ProjectEcho focuses on **learning outcomes** rather than course completion. Every learning activity should contribute to stronger evidence and measurable competency growth.

The page should answer:

> "What should I learn next to maximize my career growth?"

---

# Desktop Layout


┌──────────────────────────────────────────────────────────────────────────────┐
│ Top Navigation │
├──────────────┬───────────────────────────────────────────────────────────────┤
│ Sidebar │ │
│ │ Learning │
│ Dashboard │ Search __________________ Filter ▼ Sort ▼ │
│ Passport │ │
│ Evidence │ ┌────────────┐ ┌────────────┐ ┌────────────┐ │
│ Competencies │ │Active Paths│ │Completed │ │Hours Learned│ │
│ Learning │ └────────────┘ └────────────┘ └────────────┘ │
│ Goals │ │
│ Settings │ AI Suggested Learning Path │
│ │────────────────────────────────────────────────────────────── │
│ │ Backend Engineering Roadmap │
│ │ Progress ██████░░░░ 60% │
│ │ Estimated Time: 18 hrs │
│ │ Next Module: API Security │
│ │ [Continue] │
│ │────────────────────────────────────────────────────────────── │
│ │ Recommended Resources │
│ │ • Spring Security Docs │
│ │ • Docker Deep Dive │
│ │ • Kafka Streams Guide │
│ │────────────────────────────────────────────────────────────── │
│ │ Learning History │
└──────────────┴───────────────────────────────────────────────────────────────┘


---

# Page Header

Display:

- Page Title
- Search
- Filter
- Sort

Primary CTA:

Browse Learning Resources

---

# KPI Cards

Display:

- Active Learning Paths
- Completed Paths
- Learning Hours
- Competencies Improved

---

# AI Suggested Learning Path

Display:

- Current Learning Path
- Progress
- Estimated Remaining Time
- Next Module
- Expected Competency Gain

Primary CTA:

Continue Learning

---

# Recommended Resources

Each resource displays:

- Title
- Resource Type
- Provider
- Estimated Duration
- Difficulty
- Related Competencies
- AI Relevance Score

Actions:

- Open Resource
- Save
- Mark Complete

---

# Learning Path

Each path contains:

- Title
- Objective
- Modules
- Completion %
- Estimated Time
- Competencies Covered

Progress visualization:


Introduction
██████████

REST APIs
████████

Authentication
██████

Docker
██

Deployment
░░░░░░░░


---

# Resource Types

Support:

- Documentation
- Video
- Course
- Book
- Tutorial
- GitHub Repository
- Coding Challenge
- Practice Project

---

# Learning History

Display:

- Completed modules
- Completion date
- Competencies improved
- Evidence submitted afterward

---

# AI Insights

Each learning recommendation explains:

Observation

↓

Competency Gap

↓

Recommended Resource

↓

Expected Improvement

↓

Suggested Evidence

---

# Evidence Integration

Every completed learning activity encourages:

- Upload Project
- Upload Notes
- Link GitHub Repository
- Add Certification
- Record Assessment

The goal is converting **learning into evidence**.

---

# Filters

Support:

- Competency
- Difficulty
- Duration
- Provider
- Resource Type

---

# Search

Search by:

- Topic
- Technology
- Provider
- Skill
- Framework

---

# Sorting

Support:

- AI Priority
- Shortest Duration
- Highest Impact
- Most Popular
- Recently Added

---

# Empty State

Display:

"No learning paths available yet.

Upload evidence or define career goals to receive personalized learning recommendations."

Primary CTA:

Define Career Goal

---

# Loading State

Skeleton loaders for:

- Learning Cards
- Learning Paths
- Resources
- AI Insights

---

# Error State

Possible errors:

- Failed to load learning content
- AI recommendation unavailable
- Network issue

Display:

- Explanation
- Retry button
- Help link

---

# Responsive Behaviour

Desktop:

- Two-column layout

Tablet:

- Single-column
- Expandable modules

Mobile:

- Card-based layout
- Bottom navigation
- Full-screen learning details

---

# Primary User Journey

Open Learning

↓

Review AI Path

↓

Complete Module

↓

Apply Learning

↓

Upload Evidence

↓

Competency Updated

↓

Career Readiness Improved

---

# Design Priorities

1. Personalized learning
2. Outcome-based progress
3. Evidence generation
4. Minimal cognitive load
5. Explainable recommendations

---

# Success Criteria

The Learning page succeeds when users consistently complete meaningful learning activities, transform them into verifiable evidence, and observe measurable competency improvements reflected in their Career Passport.
