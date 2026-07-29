# Evidence Wireframe

> Status: Draft
> Version: 1.0.0
> Owner: UX Team
> Last Updated: 2026-07-29

---

# Purpose

The Evidence page is the source of truth for all professional evidence within ProjectEcho.

Every competency, recommendation, readiness score, and AI insight should ultimately be traceable back to evidence stored on this page.

The page should answer:

> "What evidence supports my professional capabilities?"

---

# Desktop Layout


┌──────────────────────────────────────────────────────────────────────────────┐
│ Top Navigation │
├──────────────┬───────────────────────────────────────────────────────────────┤
│ Sidebar │ │
│ │ Evidence │
│ Dashboard │ Search ___________________________________________ [+ Upload] │
│ Passport │ │
│ Evidence │ Filters: Type | Status | Competency | Date | Source │
│ Competencies │────────────────────────────────────────────────────────────── │
│ Learning │ │
│ Goals │ Evidence Timeline │
│ Settings │ │
│ │ ┌──────────────────────────────────────────────────────────┐ │
│ │ │ Project: Real-Time Notification System │ │
│ │ │ Verified │ Backend │ Spring Boot │ Kafka │ │
│ │ │ Linked Competencies: REST APIs, Kafka, Docker │ │
│ │ └──────────────────────────────────────────────────────────┘ │
│ │ │
│ │ ┌──────────────────────────────────────────────────────────┐ │
│ │ │ Certification │ │
│ │ └──────────────────────────────────────────────────────────┘ │
│ │ │
│ │ ┌──────────────────────────────────────────────────────────┐ │
│ │ │ Work Experience │ │
│ │ └──────────────────────────────────────────────────────────┘ │
│ │ │
└──────────────┴───────────────────────────────────────────────────────────────┘


---

# Page Header

Display:

- Page Title
- Search Bar
- Upload Evidence Button

Primary CTA:

Upload Evidence

---

# Filters

Support filtering by:

- Evidence Type
- Verification Status
- Competency
- Source
- Date Added
- Tags

---

# Search

Search by:

- Project
- Technology
- Competency
- Organization
- Certification
- Keywords

---

# Evidence Timeline

Display evidence chronologically.

Each card should include:

- Title
- Evidence Type
- Verification Status
- Date
- Technologies
- Related Competencies
- AI Summary

---

# Evidence Card

Each evidence item contains:

- Title
- Description
- Source
- Evidence Type
- Uploaded Date
- Verification Status
- Confidence
- Linked Competencies

Actions:

- View Details
- Edit
- Archive
- Delete

---

# Verification Status

Visual indicators:

- Verified
- Pending Review
- AI Processed
- Requires Attention

---

# AI Summary

Every evidence item includes:

- AI-generated summary
- Competencies inferred
- Confidence level
- Suggested improvements

---

# Evidence Detail Drawer

Opening an evidence item displays:

- Complete description
- Attachments
- Metadata
- Linked competencies
- Related recommendations
- AI reasoning
- Activity history

---

# Upload Flow

Users can upload:

- Projects
- Certifications
- Work Experience
- GitHub Repositories
- Documents
- Assessment Results

Supported actions:

- Drag & Drop
- File Picker
- External Integrations (Future)

---

# Empty State

If no evidence exists:

Display:

"Your Career Passport starts with evidence.

Upload your first project, certification, or work experience to begin building your professional profile."

Primary CTA:

Upload Evidence

---

# Loading State

Use skeleton loaders for:

- Evidence Cards
- Timeline
- Metadata
- AI Summaries

---

# Error State

Display:

- Upload failure
- Processing failure
- Verification failure
- Network issues

Every error should provide:

- Explanation
- Retry option
- Support guidance

---

# Responsive Behaviour

Desktop:

- Multi-column layout
- Timeline view

Tablet:

- Single-column timeline
- Collapsible filters

Mobile:

- Bottom navigation
- Floating Upload button
- Simplified cards

---

# Primary User Journey

Open Evidence

↓

Search / Filter

↓

Review Evidence

↓

Upload New Evidence

↓

AI Processing

↓

Competencies Updated

↓

Recommendations Refreshed

---

# Design Priorities

1. Evidence visibility
2. Explainability
3. Trust
4. Efficient management
5. Easy uploading

---

# Success Criteria

The Evidence page succeeds when users can quickly locate, understand, manage, and expand the evidence supporting their Career Passport while maintaining complete transparency into how each item contributes to competency assessment.
