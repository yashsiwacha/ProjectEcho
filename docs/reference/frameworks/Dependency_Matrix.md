# Dependency Matrix

**Governing Document:** EAD-001

The following matrix defines the strictly enforced dependency rules between modules in the ProjectEcho modular monolith. A `✓` indicates a permitted dependency (Module A may depend on Module B). An empty cell indicates a forbidden dependency.

*Rule: Dependencies must form a Directed Acyclic Graph (DAG) pointing inward toward core domain components.*

| Subject Module (Depends On ↘) | `echo-common` | `echo-identity` | `echo-passport` | `echo-competency` | `echo-evidence` | `echo-intelligence` | `echo-bootstrap` |
|-------------------------------|---------------|-----------------|-----------------|-------------------|-----------------|---------------------|------------------|
| **`echo-common`**             | -             |                 |                 |                   |                 |                     |                  |
| **`echo-identity`**           | ✓             | -               |                 |                   |                 |                     |                  |
| **`echo-passport`**           | ✓             |                 | -               |                   |                 |                     |                  |
| **`echo-competency`**         | ✓             |                 |                 | -                 |                 |                     |                  |
| **`echo-evidence`**           | ✓             |                 |                 | ✓                 | -               |                     |                  |
| **`echo-intelligence`**       | ✓             |                 | ✓               | ✓                 | ✓               | -                   |                  |
| **`echo-bootstrap`**          | ✓             | ✓               | ✓               | ✓                 | ✓               | ✓                   | -                |

## Matrix Rules:
1. **Bootstrap acts as the compositor:** `echo-bootstrap` is the only module permitted to depend on all other modules.
2. **Domain modules are isolated:** Domain modules (`passport`, `competency`, `evidence`, `intelligence`) may not depend on infrastructure modules (`bootstrap`, `identity`).
3. **Core domain stability:** `echo-passport` and `echo-competency` sit at the center of the domain and do not depend on other domain modules.
4. **Intelligence sits at the edge:** `echo-intelligence` orchestrates insights across the entire domain, and therefore may depend on the APIs of `passport`, `competency`, and `evidence`.
