# ProjectEcho Design Tokens

> Status: Draft
> Version: 1.0.0
> Owner: Design System Team
> Last Updated: 2026-07-29

---

# Purpose

Design Tokens define the visual foundation of ProjectEcho.

These tokens provide a single source of truth for colors, typography, spacing, borders, elevation, motion, and responsive behavior. They should be consumed by Figma, Tailwind CSS, and frontend components.

---

# Design Principles

- Accessibility First
- Evidence Over Decoration
- Calm & Professional
- Consistent Components
- Responsive by Default

---

# Color System

## Primary

| Token | Value | Usage |
|--------|-------|-------|
| primary-50 | #EFF6FF | Background |
| primary-100 | #DBEAFE | Hover |
| primary-500 | #2563EB | Primary Actions |
| primary-600 | #1D4ED8 | Active |
| primary-700 | #1E40AF | Focus |

---

## Neutral

| Token | Value |
|--------|-------|
| gray-50 | #F9FAFB |
| gray-100 | #F3F4F6 |
| gray-200 | #E5E7EB |
| gray-300 | #D1D5DB |
| gray-400 | #9CA3AF |
| gray-500 | #6B7280 |
| gray-600 | #4B5563 |
| gray-700 | #374151 |
| gray-800 | #1F2937 |
| gray-900 | #111827 |

---

## Semantic Colors

| Token | Value | Purpose |
|--------|-------|---------|
| success | #22C55E | Success |
| warning | #F59E0B | Warning |
| error | #EF4444 | Error |
| info | #3B82F6 | Information |

---

# Typography

## Font Family

Primary

Inter

Fallback

System UI

---

## Font Sizes

| Token | Size |
|--------|------|
| xs | 12px |
| sm | 14px |
| base | 16px |
| lg | 18px |
| xl | 20px |
| 2xl | 24px |
| 3xl | 30px |
| 4xl | 36px |

---

## Font Weight

300 Light

400 Regular

500 Medium

600 SemiBold

700 Bold

---

# Spacing

| Token | Value |
|--------|-------|
| 1 | 4px |
| 2 | 8px |
| 3 | 12px |
| 4 | 16px |
| 5 | 20px |
| 6 | 24px |
| 8 | 32px |
| 10 | 40px |
| 12 | 48px |
| 16 | 64px |

---

# Border Radius

| Token | Value |
|--------|-------|
| sm | 4px |
| md | 8px |
| lg | 12px |
| xl | 16px |
| full | 9999px |

---

# Shadows

| Token | Value |
|--------|-------|
| sm | 0 1px 2px rgba(0,0,0,.05) |
| md | 0 4px 8px rgba(0,0,0,.08) |
| lg | 0 10px 20px rgba(0,0,0,.12) |
| xl | 0 20px 40px rgba(0,0,0,.15) |

---

# Motion

| Token | Value |
|--------|-------|
| fast | 150ms |
| normal | 250ms |
| slow | 400ms |

Easing:

ease-in-out

---

# Breakpoints

| Device | Width |
|---------|-------|
| Mobile | 390px |
| Tablet | 768px |
| Laptop | 1024px |
| Desktop | 1440px |
| Wide | 1920px |

---

# Grid

Desktop

12 Columns

Tablet

8 Columns

Mobile

4 Columns

Gutter

24px

---

# Icon Sizes

16px

20px

24px

32px

48px

---

# Accessibility

Minimum contrast ratio: WCAG AA (4.5:1)

Minimum touch target: 44 × 44 px

Keyboard accessible

Visible focus indicators

Screen reader friendly

---

# Naming Convention

Design tokens should map directly to Tailwind and CSS variables.

Example:

--color-primary-500

--spacing-4

--radius-lg

--shadow-md

---

# Future Extensions

- Dark theme tokens
- High contrast theme
- Motion reduction tokens
- Brand themes
- Enterprise themes

