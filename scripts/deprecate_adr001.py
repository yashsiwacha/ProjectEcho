with open("/Users/yash/Yash-Workspace/projects/active/project-echo/docs/adr/ADR-001-career-intelligence-framework-foundations.md", "r") as f:
    content = f.read()

deprecation_notice = """
> [!WARNING]
> **DEPRECATED: SUPERSEDED BY CIF-0001**
> All business definitions, vocabulary, and domain rules defined in this historical ADR are explicitly superseded by the ratified `CIF-0001` Career Intelligence Framework. This document is retained for historical provenance only. It holds no authority over the domain model.

"""

content = content.replace("## Career Intelligence Framework Foundations", "## Career Intelligence Framework Foundations\n" + deprecation_notice)

with open("/Users/yash/Yash-Workspace/projects/active/project-echo/docs/adr/ADR-001-career-intelligence-framework-foundations.md", "w") as f:
    f.write(content)
print("Deprecated ADR-001")
