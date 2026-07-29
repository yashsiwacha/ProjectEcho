# Career Passport Specification

> Status: Draft
> Version: 1.0.0
> Owner: Product Team
> Last Updated: 2026-07-29

---

# Purpose

The Career Passport is the central feature of ProjectEcho.

It replaces the traditional static resume with a dynamic, evidence-backed, continuously evolving professional profile that reflects a user's demonstrated competencies, achievements, career progress, and AI-generated insights.

The Career Passport should serve as the single source of truth for a user's professional identity within the platform.

---

# Objectives

The Career Passport should enable users to:

- Understand their professional strengths
- View evidence supporting every competency
- Track career growth over time
- Identify skill gaps
- Share a trusted professional profile
- Receive explainable AI insights

---

# Target Users

- Students
- Early Career Professionals
- Experienced Professionals
- Recruiters (Future)
- Hiring Managers (Future)

---

# Entry Points

Users can access the Career Passport from:

- Dashboard
- Sidebar Navigation
- AI Recommendations
- Competency Details
- Public Share Link (Future)

---

# Exit Points

Users can navigate to:

- Dashboard
- Competencies
- Evidence
- Timeline
- Recommendations
- Learning
- Goals

---

# Layout

Desktop Layout:

- Header
- Passport Summary
- Navigation Tabs
- Main Content
- Right Insight Panel (Future)

---

# Sections

## Passport Summary

Displays:

- Name
- Professional Title
- Career Goal
- Experience Level
- Location (Optional)
- Profile Completion
- Last Updated

Actions:

- Edit Profile
- Share Passport
- Export (Future)

---

## Career Readiness

Displays:

- Career Readiness Score
- Confidence Level
- Historical Trend

Actions:

- View Assessment

---

## Competencies

Displays:

- Competency Categories
- Competency Scores
- Confidence Levels
- Evidence Count

Actions:

- View Details
- Explore Evidence

---

## Evidence

Displays:

- Projects
- Certifications
- Work Experience
- Assessments
- GitHub Repositories
- Achievements

Actions:

- Add Evidence
- Edit Evidence
- Archive Evidence

---

## AI Insights

Displays:

- Professional Strengths
- Improvement Opportunities
- Career Summary
- Suggested Next Steps

Every insight must include:

- Confidence
- Supporting Evidence
- Reasoning

---

## Timeline

Displays:

- Competency Growth
- New Evidence
- Career Milestones
- Recommendation History

---

## Goals

Displays:

- Active Goals
- Progress
- Estimated Completion

Actions:

- View Goal
- Update Progress

---

# Components

The Career Passport uses:

- Hero Summary Card
- Competency Cards
- Timeline
- Progress Bars
- AI Insight Cards
- Evidence Cards
- Badges
- Charts
- Expandable Sections
- Tabs

---

# Search

Future capability:

Search within Passport by:

- Competency
- Evidence
- Project
- Certification
- Skill

---

# Filters

Users can filter by:

- Competency Category
- Evidence Type
- Date
- Confidence
- Project

---

# Empty States

When no data exists:

Guide the user to:

- Complete Profile
- Upload Evidence
- Connect GitHub
- Define Career Goals

---

# Loading States

Skeleton placeholders for:

- Competencies
- Charts
- Evidence
- Timeline
- AI Insights

---

# Error States

Possible errors:

- Unable to load passport
- Missing competency data
- AI unavailable
- Network failure

Every error should provide:

- Clear explanation
- Retry option
- Support link

---

# AI Features

The AI system should generate:

- Career Summary
- Competency Analysis
- Growth Trends
- Skill Gap Detection
- Career Recommendations
- Readiness Assessment

Every AI-generated result must include:

- Confidence Score
- Supporting Evidence
- Explanation
- Suggested Action

---

# Sharing

Future versions may support:

- Public Passport URL
- Recruiter View
- PDF Export
- QR Code
- Private Sharing Links

---

# Backend Dependencies

Required APIs:

- User Profile
- Competencies
- Evidence
- Recommendations
- Goals
- Timeline
- AI Gateway
- Analytics

---

# Permissions

Owner:

- View
- Edit
- Share
- Archive Evidence

Recruiter (Future):

- Read-only Access

Administrator:

- Moderation Tools

---

# Analytics Events

Track:

- Passport Viewed
- Passport Shared
- Evidence Added
- Competency Opened
- AI Insight Viewed
- Export Requested

---

# Success Metrics

The Career Passport succeeds when users can:

- Understand their professional profile quickly
- Trust AI-generated insights
- Trace every competency back to evidence
- Monitor long-term career growth
- Confidently share their professional identity

---

# Future Enhancements

- Recruiter Verification
- Skill Benchmarking
- Industry Comparison
- Portfolio Embeds
- Live GitHub Synchronization
- Public Profiles
- Resume Generation
- AI Career Narrative

---

# Design Principles

The Career Passport must always be:

- Evidence-first
- Explainable
- Trustworthy
- Continuously updated
- Easy to understand
- Professional in appearance
- Mobile-friendly
