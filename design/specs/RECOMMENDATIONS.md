# Recommendations Specification

> Status: Draft
> Version: 1.0.0
> Owner: Product Team
> Last Updated: 2026-07-29

---

# Purpose

The Recommendations module provides personalized, evidence-backed guidance to help users improve their career readiness.

Recommendations are generated using AI by analyzing competencies, evidence, career goals, progress, and historical activity.

Every recommendation must be explainable, actionable, and traceable to supporting evidence.

---

# Objectives

The Recommendations module should enable users to:

- Understand what to improve next
- Prioritize career development
- Receive personalized learning suggestions
- Close competency gaps
- Continuously improve their Career Passport

---

# Target Users

- Students
- Early Career Professionals
- Experienced Professionals

---

# Entry Points

Users can access Recommendations from:

- Dashboard
- Career Passport
- Competencies
- Learning
- AI Notifications

---

# Exit Points

Users can navigate to:

- Learning
- Evidence
- Competencies
- Goals
- Dashboard

---

# Recommendation Categories

The MVP supports:

## Skill Improvement

Examples:

- Learn Spring Boot
- Improve SQL
- Practice System Design
- Strengthen REST API Development

---

## Project Recommendations

Examples:

- Build a Kafka-based notification system
- Create a RESTful backend
- Contribute to open source
- Develop a cloud-native application

---

## Learning Recommendations

Examples:

- Complete a course
- Read documentation
- Follow a learning path
- Practice coding challenges

---

## Career Recommendations

Examples:

- Prepare for backend interviews
- Strengthen resume evidence
- Improve GitHub profile
- Apply for internship roles

---

## Evidence Recommendations

Examples:

- Upload recent projects
- Add certifications
- Connect GitHub
- Verify work experience

---

# Recommendation Detail

Each recommendation contains:

- Title
- Description
- Category
- Priority
- Estimated Impact
- Estimated Effort
- Confidence Score
- Supporting Evidence
- Reasoning
- Suggested Actions

---

# Recommendation Lifecycle

Generated
↓

Prioritized
↓

Presented

↓

Accepted / Dismissed

↓

Progress Tracked

↓

Completed

↓

Archived

---

# Priority Levels

Recommendations are classified as:

- Critical
- High
- Medium
- Low

Priority should consider:

- Career Goals
- Skill Gaps
- Market Demand
- User Progress
- AI Confidence

---

# Components

The Recommendations module may include:

- Recommendation Cards
- Priority Badges
- AI Explanation Panel
- Progress Tracker
- Action Buttons
- Filters
- Search
- Timeline

---

# Search

Search recommendations by:

- Skill
- Technology
- Career Goal
- Category
- Project

---

# Filters

Filter by:

- Priority
- Category
- Status
- Estimated Effort
- Estimated Impact

---

# Sorting

Sort by:

- Highest Priority
- Highest Impact
- Lowest Effort
- Most Recent
- AI Confidence

---

# Empty States

If no recommendations exist:

Display:

"Your profile is up to date. Continue adding evidence to receive new personalized recommendations."

---

# Loading States

Skeleton placeholders for:

- Recommendation Cards
- AI Explanations
- Progress Indicators

---

# Error States

Possible errors:

- Recommendation generation failed
- AI unavailable
- Network error
- Evidence insufficient

Every error should provide:

- Explanation
- Retry option
- Support link

---

# AI Features

The AI system should:

- Detect competency gaps
- Recommend learning resources
- Suggest portfolio projects
- Estimate recommendation impact
- Explain recommendation reasoning
- Recalculate priorities as user data changes

Every AI output must include:

- Confidence Score
- Supporting Evidence
- Explanation
- Suggested Next Action

---

# Backend Dependencies

Required APIs:

- AI Gateway
- Competency Service
- Evidence Service
- Learning Service
- Goals Service
- User Profile
- Analytics

---

# Permissions

Owner:

- View Recommendations
- Accept Recommendation
- Dismiss Recommendation
- Track Progress

Administrator:

- Recommendation Framework Management (Future)

---

# Analytics Events

Track:

- Recommendation Viewed
- Recommendation Accepted
- Recommendation Dismissed
- Recommendation Completed
- AI Explanation Expanded

---

# Success Metrics

The Recommendations module succeeds when users can:

- Clearly understand why a recommendation exists
- Trust AI-generated guidance
- Complete recommended actions
- Improve competencies over time
- Increase Career Readiness Score

---

# Future Enhancements

- Adaptive Recommendation Engine
- Market Trend Integration
- Salary-based Recommendations
- Personalized Weekly AI Reports
- Recruiter Feedback Loop
- Multi-step Career Roadmaps

---

# Design Principles

The Recommendations experience must always be:

- Explainable
- Evidence-backed
- Actionable
- Personalized
- Transparent
- Trustworthy
- Continuously evolving
