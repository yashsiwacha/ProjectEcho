# Competencies Specification

> Status: Draft
> Version: 1.0.0
> Owner: Product Team
> Last Updated: 2026-07-29

---

# Purpose

The Competencies module is responsible for measuring, organizing, and presenting a user's professional capabilities based on verified evidence.

Competencies are not self-declared skills. They are evidence-backed capabilities inferred through AI analysis and continuously refined as new evidence is added.

This module serves as the intelligence layer between user evidence and the Career Passport.

---

# Objectives

The Competencies module should enable users to:

- Understand demonstrated strengths
- Identify competency gaps
- Measure growth over time
- Explore evidence supporting each competency
- Receive explainable competency scores

---

# Target Users

- Students
- Early Career Professionals
- Experienced Professionals

---

# Entry Points

Users can access Competencies from:

- Dashboard
- Career Passport
- AI Recommendations
- Evidence Details

---

# Exit Points

Users can navigate to:

- Evidence
- Learning
- Recommendations
- Career Passport
- Dashboard

---

# Competency Structure

Competencies are organized into hierarchical categories.

Example:

Technical Skills
├── Programming
│   ├── Java
│   ├── Python
│   ├── C++
│   └── JavaScript
│
├── Backend Development
│   ├── REST APIs
│   ├── Spring Boot
│   ├── Microservices
│   └── Authentication
│
├── Databases
│   ├── PostgreSQL
│   ├── MySQL
│   ├── Redis
│   └── MongoDB
│
├── Cloud & DevOps
│   ├── Docker
│   ├── Kubernetes
│   ├── CI/CD
│   └── AWS
│
└── Software Engineering
    ├── System Design
    ├── Testing
    ├── Security
    └── Performance

---

# Competency Detail

Every competency contains:

- Name
- Category
- Description
- Competency Score
- Confidence Score
- Growth Trend
- Evidence Count
- Last Updated

---

# Competency Scoring

Each competency should include:

- Score (0–100)
- Confidence (0–100)
- Trend
- Maturity Level

Suggested maturity levels:

- Beginner
- Developing
- Proficient
- Advanced
- Expert

---

# Supporting Evidence

Each competency must link directly to supporting evidence.

Examples:

- Projects
- GitHub Repositories
- Certifications
- Work Experience
- Assessments

Users should never see a competency without supporting evidence.

---

# Growth Tracking

Users should be able to view:

- Historical Score
- Trend Over Time
- Newly Added Evidence
- Competency Milestones

---

# Components

The Competencies module may use:

- Competency Cards
- Skill Matrix
- Progress Bars
- Radar Chart
- Trend Chart
- Evidence Drawer
- Category Filters
- Search

---

# Search

Search by:

- Competency
- Category
- Technology
- Framework

---

# Filters

Filter by:

- Category
- Confidence
- Score
- Growth Trend
- Evidence Count

---

# Sorting

Sort by:

- Highest Score
- Lowest Score
- Most Improved
- Highest Confidence
- Recently Updated

---

# Empty States

If no competencies exist:

Guide the user to:

- Upload Evidence
- Complete Profile
- Connect GitHub
- Add Certifications

---

# Loading States

Skeleton placeholders for:

- Competency Cards
- Charts
- Evidence Lists
- Trends

---

# Error States

Possible errors:

- Competencies unavailable
- AI processing pending
- Network failure
- Evidence missing

Every error should provide:

- Clear explanation
- Retry option
- Support link

---

# AI Features

The AI system should:

- Infer competencies from evidence
- Calculate competency confidence
- Detect emerging skills
- Recommend improvement opportunities
- Explain every competency score

Every AI output must include:

- Confidence Score
- Supporting Evidence
- Reasoning
- Suggested Next Actions

---

# Backend Dependencies

Required APIs:

- Competency Service
- Evidence Service
- AI Gateway
- Analytics
- Career Passport
- User Profile

---

# Permissions

Owner:

- View Competencies
- Explore Evidence

Administrator:

- Competency Framework Management (Future)

Recruiter (Future):

- Read-only access to shared competencies

---

# Analytics Events

Track:

- Competency Viewed
- Evidence Expanded
- Category Filter Applied
- Recommendation Opened
- Growth Chart Viewed

---

# Success Metrics

The Competencies module succeeds when users can:

- Understand strengths quickly
- Trust competency scores
- Trace every competency to evidence
- Track measurable improvement
- Discover clear development opportunities

---

# Future Enhancements

- Industry Benchmarking
- Role-based Competency Models
- Team Competency Heatmaps
- Peer Comparison
- AI Competency Forecasting
- Organization-wide Skill Analytics

---

# Design Principles

The Competencies experience must always be:

- Evidence-backed
- Explainable
- Transparent
- Actionable
- Consistent
- Scalable
- Trustworthy
