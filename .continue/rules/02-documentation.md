# Documentation Rules

## Purpose

This rule defines how AI assistants should create, modify, and maintain
documentation within ProjectEcho.

Documentation is a first-class engineering artifact.

Its primary purpose is to communicate decisions, architecture, intent,
constraints, and implementation guidance in a maintainable and traceable
manner.

This rule defines AI behaviour.

It does NOT replace the repository's documentation.

---

# Documentation Philosophy

ProjectEcho follows Documentation-Driven Engineering.

Documentation precedes implementation.

Architecture precedes code.

Governance precedes architecture.

Implementation must never contradict approved documentation.

---

# One Authoritative Source

Every concept must have exactly one authoritative document.

Before creating documentation:

1. Search the repository.
2. Identify the document that owns the concept.
3. Update that document whenever possible.
4. Create a new document only when introducing a genuinely new concept.

Never create competing explanations.

Never duplicate repository knowledge.

---

# Navigation vs Authority

Understand the difference between navigation and governance.

Navigation documents:

- README
- PROJECT_MANIFEST
- PROJECT_STATUS
- INDEX

Navigation documents summarize and guide readers.

They are not decision-making documents.

Governance documents:

- ADRs
- ARBRs
- Framework documents
- Standards

These define repository decisions.

Architecture documents describe system design.

Implementation documents describe engineering details.

Always modify the document that owns the concept.

---

# AI Documentation

The `.ai` directory exists to improve AI navigation.

It should:

- guide AI assistants
- provide repository context
- define AI behaviour
- reference authoritative documentation

It should NOT become a second documentation system.

Whenever possible:

Reference existing documentation instead of rewriting it.

---

# Updating Documentation

When implementation changes documentation:

- update only the affected authoritative documents
- preserve existing links
- keep terminology consistent
- avoid unrelated edits

Do not modify documentation without understanding its role in the repository.

---

# Documentation Quality

Good documentation is:

- accurate
- concise
- traceable
- maintainable
- deterministic

Avoid:

- duplicated explanations
- conflicting terminology
- speculative documentation
- implementation details in governance documents
- governance decisions inside navigation documents

---

# Versioning

Documentation is repository history.

Prefer updating or archiving documents over deleting them.

Maintain historical traceability whenever practical.

---

# Before Creating New Documentation

Always ask:

- Does this concept already exist?
- Which document owns it?
- Can I extend an existing document?
- Will this introduce duplication?
- Does this belong in governance, architecture, engineering, or navigation?

Only create new documentation when the answer is clearly justified.

---

# Documentation Checklist

Before completing documentation work, verify:

- One authoritative source remains.
- No duplicate concepts were introduced.
- Terminology is consistent.
- Cross-references remain valid.
- Repository navigation is preserved.
- Governance is respected.
- Documentation matches implementation.
- Documentation does not contradict approved decisions.

