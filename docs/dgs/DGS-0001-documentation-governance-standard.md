# Documentation Governance Standard (DGS)

**Document ID:** DGS-0001
**Title:** Documentation Governance Standard
**Version:** 1.0
**Status:** Approved
**Classification:** Governance
**Owner:** Documentation & Architecture Engineering
**Authority Level:** 2 (Governs all documentation artifacts)
**Primary Audience:** Architects, Engineers, Product Managers, AI Assistants
**Governed By:** Framework Governance Model (FGM)
**Review Cadence:** Bi-Annually
**Last Updated:** 2026-08-04
**Next Review:** 2027-02-04
**Approved By:** Founder (Pending Ratification)
**Supersedes:** All previous ad-hoc documentation templates
**Superseded By:** N/A
**Related Documents:** FGM, EAF, CIF

---

## 4. Authority Declaration

**Purpose:** 
To define the mandatory writing standard for every governance, architecture, engineering, and repository document within ProjectEcho.

**Scope:** 
Applies to all Markdown (`.md`) files within the ProjectEcho repository, including `.ai/` workspace documents.

**Authority:** 
This document acts as the definitive quality and structural standard. No document shall be ratified if it violates the DGS. 

**Out of Scope:** 
This standard does NOT define architectural decisions, technical implementation details, or business logic. It dictates *how* documents are structured, not *what* they decide.

---

## 1. Document Classification

Every document must be classified into one of the following categories:

- **Governance:** Defines how decisions are made (e.g., FGM, DGS).
- **Architecture:** Defines system structure and constraints (e.g., ADR, EAF).
- **Engineering:** Defines technical implementations (e.g., EAD, EDF).
- **Product:** Defines business requirements and features (e.g., PRD, CIF).
- **Repository:** Defines repository rules (e.g., CONTRIBUTING, README).
- **Research:** Investigates options without deciding (e.g., ARBR, RAR).
- **Reference:** Glossaries, standards, or indices.
- **Templates:** Base formats for future documents.
- **AI Workspace:** Context and prompts for AI assistants.

---

## 2. Document Metadata Standard

Every document MUST begin with a frontmatter or metadata block containing:

- **Document ID:** Unique identifier (e.g., ADR-0001).
- **Title:** Human-readable title.
- **Version:** Semantic versioning (Major.Minor).
- **Status:** Current lifecycle state.
- **Classification:** From Section 1.
- **Owner:** Role or individual responsible.
- **Authority Level:** Integer defining precedence (1 = Supreme).
- **Primary Audience:** Target reader.
- **Governed By:** The upstream document defining this document's existence.
- **Review Cadence:** Frequency of audits.
- **Last Updated:** ISO-8601 Date.
- **Next Review:** ISO-8601 Date.
- **Approved By:** Role or individual who ratified it.
- **Supersedes:** IDs of replaced documents.
- **Superseded By:** IDs of replacing documents (if deprecated).
- **Related Documents:** Direct dependencies.

---

## 3. Document Lifecycle

Documents evolve strictly through the following states:

1. **Draft:** Work in progress. Not authoritative.
2. **Review:** Pending technical/architectural evaluation.
3. **Approved:** Ratified by the owner/founder. Authoritative.
4. **Frozen:** Immutable. Can only be superseded, not amended.
5. **Deprecated:** Scheduled for removal or currently superseded.
6. **Archived:** Moved to `archive/`. Retained for historical context only.

---

## 5. Cross-reference Rules

- **Do Not Duplicate:** Never copy definitions, business logic, or architectural constraints. 
- **Use Hyperlinks:** Reference the authoritative document using relative Markdown links (e.g., `[CIF](docs/cif/CIF-0001.md)`).
- **Link Stability:** If a document is deprecated, update all upstream links to point to the superseding document.

---

## 6. Naming Standards

- **Document IDs:** `[TYPE]-[NUMBER]`. Example: `ADR-0042`, `DGS-0001`.
- **File Names:** Lowercase, kebab-case, prefixed with ID. Example: `adr-0042-kafka-event-bus.md`.
- **Folder Layout:** Documents reside in their respective domain folders (e.g., `docs/adr/`, `docs/eaf/`).
- **Version Numbers:** Use Semantic Versioning (`1.0` for Approved, `1.1` for minor amendments, `2.0` for major rewrites).

---

## 7. Writing Standards

- **Terminology:** Must strictly map to the Career Intelligence Framework (CIF).
- **Headings:** Use ATX-style Markdown headings (`#`, `##`, `###`).
- **Formatting:** Use **bold** for emphasis, `code` for technical terms.
- **Lists:** Use unordered lists (`-`) for collections, ordered lists (`1.`) for sequential workflows.
- **Tables:** Use standard Markdown tables for matrices and structured comparisons.
- **Callouts:** Use GitHub Flavored Markdown alerts (`> [!NOTE]`, `> [!WARNING]`) for critical context.
- **Examples:** Code examples must specify the language block (` ```java `).
- **Diagrams:** Use Mermaid.js natively (see Section 11).

---

## 8. Governance Labels

Every declarative statement in a research, audit, or review document MUST be prefixed with one of the following labels to ensure absolute clarity of intent:

- `[FACT]` - Empirically verifiable truth in the repository.
- `[OBSERVATION]` - Noted behavior or pattern.
- `[INFERENCE]` - Logical deduction derived from facts.
- `[RECOMMENDATION]` - Proposed action requiring approval.
- `[FOUNDER DECISION REQUIRED]` - Action blocked pending Founder authority.
- `[CONTRADICTION]` - Direct conflict between two authoritative sources.
- `[ASSUMPTION]` - Premise taken as true without verification for planning purposes.

---

## 9. Review Checklist

Every document entering the `Review` phase must be evaluated for:

- [ ] **Completeness:** Are all metadata fields populated?
- [ ] **Consistency:** Does this contradict any upstream Frozen document?
- [ ] **Authority:** Does the author have the right to declare these rules?
- [ ] **Ownership:** Is the owner clearly defined?
- [ ] **Traceability:** Can every decision be traced to a business requirement or Founder Decision?
- [ ] **AI Readability:** Are semantic boundaries clear for LLM context windows?
- [ ] **Long-term Maintainability:** Will this document rot?
- [ ] **Cross References:** Are all links valid and relative?

---

## 10. AI Optimization Standard

ProjectEcho relies on autonomous and semi-autonomous AI Assistants (Claude, ChatGPT, Gemini, DeepSeek, Codex, Copilot). Documents must be optimized for context-window ingestion without sacrificing human readability.

- **Semantic Chunking:** Keep paragraphs short. Use explicit headings.
- **Context Priming:** Begin every document with a clear `Purpose` and `Scope` so the LLM immediately understands its role.
- **Explicit Denials:** Explicitly state what a document does *not* do (e.g., `Out of Scope`) to prevent AI hallucination.
- **Machine-Readable Tables:** Prefer tables over bullet points for mapping relationships.
- **Absolute Paths in Prompts:** When writing AI prompts, reference absolute or repository-root-relative paths.

---

## 11. Mermaid Diagram Standards

To ensure rendering compatibility across GitHub, IDEs, and AI tools:

- **Flowcharts:** Use `graph TD` or `graph LR`.
- **Dependency Graphs:** Use standard flowcharts. Avoid overly complex routing; group by subgraphs.
- **Sequence Diagrams:** Use `sequenceDiagram`. Explicitly declare participants.
- **Hierarchy Diagrams:** Use `mindmap` or tree flowcharts.
- **C4 Diagrams:** Use `C4Context`, `C4Container`, and `C4Component` syntaxes if supported, otherwise map them using standard subgraphs with explicit `<<System>>` stereotyping.
- **Syntax Safety:** Never use unescaped special characters in node labels. Quote strings if necessary (e.g., `Node["Label (Info)"]`).

---

## 12. Repository Reading Order

To rapidly build context, new entrants should read documents in the following strict order:

**For Founders:**
1. `PROJECT_MANIFEST.md`
2. Founder Decisions (Ad-hoc)
3. Conflict Register

**For Architects:**
1. `DGS-0001`
2. `FGM`
3. `CIF`
4. `docs/adr/*`
5. `docs/eaf/*`

**For Engineers:**
1. `README.md`
2. `CONTRIBUTING.md`
3. `docs/adr/*`
4. `docs/ead/*`

**For AI Assistants:**
1. `.ai/context/PROJECT_MANIFEST.md` (or equivalent root context)
2. `.ai/employees/<Role>.md`
3. Target Governance Document (FGM/CIF)
4. Relevant `ADR`

---

## 13. Document Quality Gates

Objective criteria for moving between lifecycle states:

- **Draft → Review Complete:** All metadata populated; Markdown lints pass; Mermaid diagrams render; Review Checklist completed by an Architect.
- **Review Complete → Approved:** Formal sign-off by the document Owner.
- **Approved → Production Ready (Frozen):** Fully integrated into the repository; all cross-references from dependent documents are updated.

---

## 14. Change Management

- **Immutability:** Once a document is `Frozen` (e.g., foundational ADRs), it cannot be edited. It must be `Deprecated` and superseded by a new document.
- **Amendments:** `Active` or `Approved` documents can receive minor updates. Version numbers must be incremented.
- **Pull Requests:** All documentation changes must go through the standard PR workflow, requiring at least one approving review from the Governance Lead or Architecture Board.
