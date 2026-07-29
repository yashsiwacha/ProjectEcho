# Settings Wireframe

> Status: Draft
> Version: 1.0.0
> Owner: UX Team
> Last Updated: 2026-07-29

---

# Purpose

The Settings page gives users complete control over their ProjectEcho experience, including account management, privacy, AI behavior, connected services, notifications, and data ownership.

The page should answer:

> "How can I customize and control my Career Intelligence Platform?"

---

# Desktop Layout


┌──────────────────────────────────────────────────────────────────────────────┐
│ Top Navigation │
├──────────────┬───────────────────────────────────────────────────────────────┤
│ Sidebar │ Settings │
│ │ │
│ Dashboard │ Account │
│ Passport │ ┌──────────────────────────────────────────────────────────┐ │
│ Evidence │ │ Profile Information │ │
│ Competencies │ │ Email │ │
│ Learning │ │ Password │ │
│ Goals │ │ Two-Factor Authentication │ │
│ Settings │ └──────────────────────────────────────────────────────────┘ │
│ │ │
│ │ Privacy │
│ │ AI Preferences │
│ │ Notifications │
│ │ Connected Accounts │
│ │ Career Passport Sharing │
│ │ Data & Export │
│ │ Appearance │
│ │ Security │
│ │ About │
└──────────────┴───────────────────────────────────────────────────────────────┘


---

# Navigation

Settings are grouped into categories:

- Account
- Privacy
- AI Preferences
- Notifications
- Connected Accounts
- Career Passport Sharing
- Data & Export
- Appearance
- Security
- About

---

# Account

Display:

- Profile Picture
- Name
- Email
- Username
- Password
- Two-Factor Authentication

Actions:

- Edit
- Change Password
- Enable 2FA

---

# Privacy

Controls:

- Public Profile
- Passport Visibility
- Recruiter Access
- Analytics Consent
- AI Training Consent

---

# AI Preferences

Users can configure:

- AI Recommendation Frequency
- Preferred Career Roles
- Preferred Technologies
- Learning Preferences
- Recommendation Sensitivity
- Explanation Detail Level

---

# Notifications

Support:

- Email
- Push
- In-App

Notification Types:

- Recommendations
- Goal Milestones
- Competency Updates
- Learning Reminders
- Evidence Processing

---

# Connected Accounts

Supported Integrations (Current & Future):

- GitHub
- LinkedIn
- Google
- Microsoft
- GitLab
- LeetCode
- HackerRank
- Coursera
- Udemy

Display:

- Connection Status
- Last Sync
- Sync Frequency

---

# Career Passport Sharing

Options:

- Private
- Public Link
- Recruiter Link
- Password Protected
- QR Code (Future)

Actions:

- Copy Link
- Disable Sharing
- Regenerate Link

---

# Data & Export

Allow users to:

- Export Passport (PDF)
- Export JSON
- Download Evidence
- Download Competencies
- Request Account Archive
- Delete Account

---

# Appearance

Support:

- Light Mode
- Dark Mode
- System Theme

Future:

- Accessibility Themes
- High Contrast
- Font Scaling

---

# Security

Display:

- Recent Logins
- Active Sessions
- Connected Devices
- API Tokens (Future)
- Security Alerts

Actions:

- Sign Out Everywhere
- Revoke Device
- Rotate Tokens

---

# About

Display:

- ProjectEcho Version
- Terms
- Privacy Policy
- Licenses
- Support
- Feedback

---

# Loading State

Skeleton loaders for:

- Forms
- Connected Accounts
- Preferences

---

# Error State

Possible errors:

- Failed to save settings
- Authentication expired
- Sync failed
- Network issue

Display:

- Explanation
- Retry
- Help

---

# Responsive Behaviour

Desktop:

- Left navigation
- Right content panel

Tablet:

- Collapsible navigation

Mobile:

- Accordion sections
- Bottom navigation
- Full-screen forms

---

# Primary User Journey

Open Settings

↓

Update Preferences

↓

Save Changes

↓

Immediate Synchronization

↓

Confirmation

---

# Design Priorities

1. User control
2. Privacy by default
3. Security
4. Transparency
5. Simplicity

---

# Success Criteria

The Settings page succeeds when users can confidently manage their account, privacy, AI preferences, integrations, and exported data with minimal effort while maintaining full trust in the platform.
