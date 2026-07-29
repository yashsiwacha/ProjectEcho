# Dashboard Specification

> Status: Draft
> Version: 1.0.0
> Owner: Product Team
> Last Updated: 2026-07-29

---

# Purpose

The Dashboard is the primary landing page after authentication.

Its purpose is to provide users with an immediate understanding of their current career status, recent progress, AI-generated insights, and recommended next actions.

The Dashboard should answer three questions within the first 30 seconds:

- Where am I today?
- What changed since my last visit?
- What should I do next?

---

# Target Users

- Students
- Early Career Professionals
- Experienced Professionals

---

# Entry Points

- Login
- Registration Completion
- Logo Click
- Navigation Sidebar

---

# Exit Points

- Career Passport
- Competencies
- Evidence
- Learning
- Recommendations
- Goals
- Settings

---

# Page Layout

Desktop layout consists of:

- Top Navigation Bar
- Left Sidebar
- Main Dashboard Content
- Right Context Panel (Future)

---

# Dashboard Sections

## Welcome Section

Displays:

- Greeting
- Current Career Goal
- Career Readiness Score
- Last Updated Timestamp

---

## Career Readiness Overview

Displays:

- Overall Readiness Score
- Trend Indicator
- Weekly Progress
- Monthly Progress

Actions:

- View Details
- Generate New Assessment

---

## Competency Snapshot

Displays:

- Top Competencies
- Improving Competencies
- Weak Competencies
- Confidence Levels

Actions:

- View Competencies

---

## AI Insights

Displays:

- Latest AI Observation
- Career Strength
- Largest Improvement Opportunity
- Confidence Level

Actions:

- View Full Analysis

---

## Recommendations

Displays:

- Top Priority Recommendation
- Estimated Impact
- Estimated Completion Time

Actions:

- View All Recommendations

---

## Recent Evidence

Displays:

- Latest Uploaded Evidence
- Processing Status
- Competencies Updated

Actions:

- Upload More Evidence

---

## Goals Progress

Displays:

- Active Goals
- Completion Percentage
- Upcoming Milestones

Actions:

- Manage Goals

---

## Learning Progress

Displays:

- Active Learning Paths
- Completed Resources
- Suggested Next Lesson

Actions:

- Continue Learning

---

# Components

The Dashboard may use:

- Metric Cards
- Progress Bars
- Line Charts
- Competency Radar Chart
- Timeline Cards
- Recommendation Cards
- AI Insight Cards
- Tables
- Empty States
- Loading Skeletons

---

# Filters

Future Support:

- Date Range
- Competency
- Evidence Type
- Goal

---

# Empty States

If no data exists:

Show onboarding guidance with:

- Complete Profile
- Upload Evidence
- Set Career Goal
- Generate First Passport

---

# Loading States

Use skeleton placeholders for:

- Charts
- Cards
- Tables
- AI Insights

---

# Error States

Possible errors:

- Network Failure
- AI Service Unavailable
- Authentication Expired

Every error should provide:

- Explanation
- Retry Option
- Support Link

---

# AI Features

The Dashboard AI should provide:

- Career Readiness Analysis
- Weekly Summary
- Skill Gap Detection
- Improvement Suggestions
- Goal Progress Evaluation

Every AI response must include:

- Confidence
- Evidence Used
- Reasoning
- Suggested Next Action

---

# Backend Dependencies

Required APIs:

- Authentication
- User Profile
- Career Passport
- Competencies
- Evidence
- Recommendations
- Goals
- Learning
- Analytics

---

# Permissions

User:

- Read Own Dashboard
- Update Personal Data

Administrator:

- Platform Analytics (Future)

---

# Analytics Events

Track:

- Dashboard Viewed
- Recommendation Clicked
- Evidence Uploaded
- Goal Created
- Learning Started
- Passport Viewed

---

# Success Metrics

The Dashboard succeeds when users can:

- Understand career status within 30 seconds.
- Identify their next recommended action.
- View measurable progress.
- Navigate to deeper insights effortlessly.

---

# Future Enhancements

- Recruiter Dashboard
- Team Dashboard
- AI Chat Assistant
- Predictive Career Forecasts
- Live Market Trends
- Personalized Widgets
- Custom Dashboard Layouts
