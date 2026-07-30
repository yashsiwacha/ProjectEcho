---
name: ProjectEcho Design System
colors:
  surface: '#f9f9ff'
  surface-dim: '#d3daea'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f0f3ff'
  surface-container: '#e7eefe'
  surface-container-high: '#e2e8f8'
  surface-container-highest: '#dce2f3'
  on-surface: '#151c27'
  on-surface-variant: '#45464c'
  inverse-surface: '#2a313d'
  inverse-on-surface: '#ebf1ff'
  outline: '#76777d'
  outline-variant: '#c6c6cd'
  surface-tint: '#575e70'
  primary: '#000000'
  on-primary: '#ffffff'
  primary-container: '#141b2b'
  on-primary-container: '#7d8497'
  inverse-primary: '#c0c6db'
  secondary: '#7c571a'
  on-secondary: '#ffffff'
  secondary-container: '#ffcc83'
  on-secondary-container: '#795417'
  tertiary: '#000000'
  on-tertiary: '#ffffff'
  tertiary-container: '#261906'
  on-tertiary-container: '#968065'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dce2f7'
  primary-fixed-dim: '#c0c6db'
  on-primary-fixed: '#141b2b'
  on-primary-fixed-variant: '#404758'
  secondary-fixed: '#ffddb1'
  secondary-fixed-dim: '#f0be77'
  on-secondary-fixed: '#291800'
  on-secondary-fixed-variant: '#614002'
  tertiary-fixed: '#f9debf'
  tertiary-fixed-dim: '#dcc2a4'
  on-tertiary-fixed: '#261906'
  on-tertiary-fixed-variant: '#55442d'
  background: '#f9f9ff'
  on-background: '#151c27'
  surface-variant: '#dce2f3'
typography:
  display:
    fontFamily: Inter
    fontSize: 48px
    fontWeight: '600'
    lineHeight: '1.1'
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '600'
    lineHeight: '1.2'
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.2'
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '500'
    lineHeight: '1.3'
    letterSpacing: -0.01em
  body-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '400'
    lineHeight: '1.6'
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.6'
  label-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: '1.4'
    letterSpacing: 0.01em
  label-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: '1.2'
    letterSpacing: 0.03em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  unit: 8px
  container-max: 1280px
  gutter: 24px
  margin-desktop: 48px
  margin-mobile: 20px
  stack-sm: 8px
  stack-md: 16px
  stack-lg: 32px
---

## Brand & Style

The design system is a "Career Operating System" engineered for high-level professionals. The personality is analytical and calm, moving away from the frantic energy of traditional job boards toward a premium, evidence-based intelligence platform.

The visual style is **Corporate / Modern** with heavy influences from **Minimalism**. It prioritizes clarity and focus through expansive whitespace and a structured information hierarchy. The aesthetic is defined by "soft precision"—combining the technical rigor of AI with a human-centric, warm interface. This is achieved through a mix of high-fidelity surfaces, subtle depth, and a restrained application of a luxury accent color.

## Colors

The color palette is split into two distinct modes to maintain the "Career OS" feel across different environments. 

**Light Mode (Default):** Uses a Warm White (`#FAFAF8`) background to reduce eye strain and provide a more "analog" paper-like quality than pure stark white. Surfaces are pure White (`#FFFFFF`) to create subtle elevation.

**Dark Mode:** Transitions to a deep, sophisticated Midnight Blue-Black (`#0B1018`) to maintain the premium feel without losing legibility.

**The Accent:** Muted Champagne is used sparingly (<5% of the UI). It is reserved exclusively for high-value actions, achievement indicators, or verified AI insights. This prevents the interface from feeling "salesy" and maintains a focused, professional atmosphere.

## Typography

This design system utilizes **Inter** exclusively to lean into its systematic, legible, and neutral characteristics. 

The type scale is designed for density and clarity. Headings use tighter letter spacing and heavier weights to create strong visual anchors. Body text is optimized for long-form reading of career data, using a generous line height (1.6) to ensure the technical content remains approachable. 

Labels utilize slightly increased letter spacing and Medium/SemiBold weights to differentiate them from body content, providing clear signposting within complex data tables and AI reasoning cards.

## Layout & Spacing

The layout follows a **Fixed Grid** philosophy for desktop to maintain a professional, "dashboard" feel, while transitioning to a fluid model for mobile devices.

- **Desktop (1280px+):** 12-column grid with 24px gutters. Sidebars are fixed at 280px.
- **Tablet (768px - 1279px):** 8-column grid with 24px gutters.
- **Mobile (<767px):** 4-column grid with 16px gutters and 20px side margins.

Spacing follows a strict 8px base unit. Component-level spacing (stacking) is used to group related information, particularly within "Evidence Cards" where the AI's reasoning is displayed. Use `stack-lg` to separate major content blocks and `stack-sm` for internal label-value pairs.

## Elevation & Depth

Visual hierarchy is established through **Tonal Layers** and **Ambient Shadows**. 

The background is the lowest level. Content resides on white "Surface" cards. These cards use a dual-shadow approach: a very soft, large-radius ambient shadow to create a sense of floating, and a razor-thin 1px border (`#E5E7EB`) to provide definition. 

In Dark Mode, elevation is communicated by lightening the fill color of the surface (e.g., Level 1 is `#111827`, Level 2 is `#1F2937`) rather than using shadows, which ensures the UI remains crisp and high-contrast.

## Shapes

The shape language balances modern software aesthetics with professional sturdiness. 

- **Primary Cards:** Use a 20px corner radius to create a soft, inviting container for complex data.
- **Interactive Elements:** Buttons and input fields use a 12px radius. This differentiation helps users instinctively separate "containers" from "actions."
- **Badges/Chips:** Use a fully rounded (pill) shape to denote status or tags, providing a clear visual departure from the rectangular structure of the rest of the UI.

## Components

### Buttons
Primary buttons use the Champagne accent color with white text. Secondary buttons use a transparent background with a 1px border. All buttons have a height of 44px for a premium, touch-friendly feel.

### Input Fields
Inputs are 44px tall with a 12px radius. In light mode, they use a subtle `#F3F4F6` fill that clears on focus, replaced by a 2px border in the accent color.

### Evidence-Based AI Reasoning Cards
Unique to this design system, these cards feature a 20px radius and a left-accent border (4px) in Champagne. They utilize "Progressive Disclosure"—initial view shows a summary, while clicking expands to show the "Data Evidence" using `label-sm` typography.

### Tables & Lists
Tables should be borderless between rows, using only a subtle horizontal separator. Rows should have a hover state that changes the background to the secondary surface color, signaling interactivity without clutter.

### Sidebar
The navigation sidebar uses a subtle blur (if background elements are present) or a flat off-white fill. Icons are outlined, switching to solid filled versions in the accent color when active.