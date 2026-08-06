import os
import glob
import re
from datetime import datetime

docs_dir = "/Users/yash/Yash-Workspace/projects/active/project-echo/docs"

# 1. Update ADRs with metadata
adr_files = glob.glob(os.path.join(docs_dir, "adr", "*.md"))
for adr_file in adr_files:
    with open(adr_file, "r") as f:
        content = f.read()
    
    if not content.startswith("---"):
        filename = os.path.basename(adr_file)
        doc_id = filename.split('-')[0] + "-" + filename.split('-')[1]
        
        frontmatter = f"""---
Document ID: {doc_id}
Title: {filename.replace('.md', '').replace('-', ' ').title()}
Version: 1.0
Status: Frozen
Classification: Architecture
Owner: Principal Architect
Authority Level: 4
Primary Audience: Engineers
Governed By: CIF-0001
Review Cadence: N/A
Last Updated: 2026-08-04
Next Review: N/A
---
"""
        with open(adr_file, "w") as f:
            f.write(frontmatter + content)

# 2. Process EAF-v2.0
eaf_v1_path = os.path.join(docs_dir, "eaf", "EAF-v1.0-revision-2.md")
eaf_v2_path = os.path.join(docs_dir, "eaf", "EAF-v2.0.md")

with open(eaf_v1_path, "r") as f:
    eaf_content = f.read()

# Inject frontmatter
eaf_frontmatter = """---
Document ID: EAF-0001
Title: Engineering Architecture Framework
Version: 2.0
Status: Approved
Classification: Architecture
Owner: Principal Architect
Authority Level: 3
Primary Audience: Engineers
Governed By: FGM-0001, CIF-0001
Review Cadence: Bi-Annually
Last Updated: 2026-08-04
---
"""

eaf_content = eaf_content.replace(
    "# ENGINEERING ARCHITECTURE FRAMEWORK (EAF)",
    "# ENGINEERING ARCHITECTURE FRAMEWORK (EAF-v2.0)"
)

# Terminology Replacements
eaf_content = re.sub(r'\bUser Profile\b', 'Career Passport', eaf_content, flags=re.IGNORECASE)
eaf_content = re.sub(r'\bJob\b', 'Mission', eaf_content, flags=re.IGNORECASE)

# Tenancy Architecture Fix (Visibility Scope)
eaf_content = re.sub(
    r'### 29\. Tenant \(Multi-tenancy\).*?(?=\n###|\Z)', 
    """### 29. Visibility Scope (Access Boundaries)

- **Purpose:** Represent a logical isolation boundary for data and configuration governed by the User.
- **Definition:** An access delegation granted by the User to an Organization (Employer). Organizations do not own Passports; they merely operate within granted Visibility Scopes (per FD-006).
- **Responsibilities:** Provide the scope within which data access, configuration overrides, and rate limiting apply.
- **Constraints:** Isolation must be enforced at a named layer (Repository-level guard or row-level security). No business component may bypass the Visibility Scope.
- **Relationships:** Replaces traditional B2B multi-tenancy.
- **Non-goals:** Does not select a specific row-level security implementation.
""", 
    eaf_content, 
    flags=re.DOTALL
)

# Authority Re-alignment (Remove business definitions)
eaf_content = eaf_content.replace(
    "**Explicitly NOT:** An Engineering Architecture Document (EAD), an ADR, an API spec, a package layout, a library choice, or code.",
    "**Explicitly NOT:** An Engineering Architecture Document (EAD), an ADR, an API spec, a package layout, a library choice, or code.\n**Authority Note:** The EAF defers all business logic to CIF-0001 and all governance to FGM-0001. Any domain definitions herein are structural placeholders mapping to CIF-0001."
)

with open(eaf_v2_path, "w") as f:
    f.write(eaf_frontmatter + eaf_content)

print("Updated ADRs and created EAF-v2.0.md")
