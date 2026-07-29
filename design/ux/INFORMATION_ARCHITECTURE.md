# ProjectEcho Information Architecture

> Status: Draft
> Version: 1.0.0
> Owner: Product Team
> Last Updated: 2026-07-29

---

# Purpose

This document defines the structural organization of ProjectEcho.

It establishes how information, features, pages, and navigation are organized to ensure users can easily discover, understand, and interact with the platform.

The Information Architecture (IA) serves as the blueprint for navigation design, frontend routing, and future wireframing.

---

# Design Goals

The architecture should:

- Minimize cognitive load
- Prioritize discoverability
- Support progressive disclosure
- Scale without major navigation changes
- Keep evidence central to every workflow

---

# Primary Navigation

The desktop application uses a persistent left sidebar.


Dashboard

Career Passport
Overview
Competencies
Evidence
Timeline

Learning

Recommendations

Projects

Goals

Settings


---

# Dashboard

Purpose:

Provide users with a high-level overview of their career progress.

Contains:

- Career Readiness Score
- Competency Summary
- AI Insights
- Recent Evidence
- Recommended Actions
- Progress Overview

---

# Career Passport

The Career Passport is the core feature of ProjectEcho.

It combines all evidence, competencies, and AI analysis into a unified professional profile.

Subsections:

## Overview

Summary of the user's professional profile.

## Competencies

Evidence-backed competency graph.

## Evidence

Uploaded projects, certifications, work experience, and assessments.

## Timeline

Historical view of career growth and competency evolution.

---

# Learning

Purpose:

Help users improve identified skill gaps.

Contains:

- Recommended learning paths
- Courses
- Certifications
- Personalized roadmap

---

# Recommendations

Purpose:

Present explainable AI-generated career recommendations.

Contains:

- Skill improvements
- Missing competencies
- Suggested projects
- Career opportunities
- Next best actions

Every recommendation must include:

- Reason
- Supporting evidence
- Confidence level
- Expected impact

---

# Projects

Purpose:

Manage project portfolio.

Users can:

- Add projects
- Update projects
- Link GitHub repositories
- Track project maturity

---

# Goals

Purpose:

Track long-term career objectives.

Users can:

- Create goals
- Measure progress
- Link competencies
- Receive milestone recommendations

---

# Settings

Contains:

- Account
- Privacy
- Notifications
- AI Preferences
- Connected Accounts
- Security

---

# Navigation Principles

Every page should answer three questions:

1. Where am I?
2. What can I do here?
3. What should I do next?

---

# Global Search

Future capability.

Search should eventually include:

- Competencies
- Projects
- Evidence
- Recommendations
- Learning Resources

---

# Cross-Page Components

The following components should remain consistent across the application:

- Sidebar Navigation
- Header
- Search
- Breadcrumbs
- Notifications
- User Menu
- AI Assistant Entry Point

---

# Mobile Information Architecture

The mobile application should prioritize quick interactions.

Primary navigation:

- Home
- Passport
- Evidence
- Recommendations
- Profile

Complex analytics remain desktop-first.

---

# Future Expansion

The IA should support future additions without restructuring navigation.

Potential additions:

- Recruiter Portal
- Team Dashboard
- Interview Coach
- Organization Insights
- Marketplace
- Browser Extension
- VS Code Integration

---

# Information Architecture Principles

The IA should always prioritize:

- Simplicity
- Scalability
- Explainability
- Accessibility
- Evidence-first workflows
- Consistent navigation
