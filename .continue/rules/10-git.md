# Git Rules

## Purpose

This rule defines Git and version control practices for ProjectEcho.

Version control preserves engineering history.

Every commit should improve repository quality.

---

# Commit Philosophy

Commits should be:

- focused
- atomic
- understandable
- reversible

Avoid mixing unrelated changes in a single commit.

---

# Commit Messages

Write clear commit messages.

The subject should describe what changed.

Examples:

feat(auth): add JWT authentication

fix(api): handle invalid user IDs

docs(adr): clarify modular monolith decision

refactor(user): simplify service layer

Avoid vague messages such as:

- update
- fixes
- changes
- work
- misc

---

# Branching

Prefer feature branches.

Keep branches focused on a single objective.

Avoid long-lived branches whenever practical.

---

# Before Committing

Review:

- modified files
- formatting
- tests
- documentation
- accidental files

Never commit:

- IDE files
- secrets
- credentials
- generated build output
- temporary files

---

# Documentation

If implementation changes repository behavior:

Determine whether documentation should also be updated.

Keep implementation and documentation synchronized.

---

# Pull Requests

A pull request should explain:

- what changed
- why it changed
- architectural impact
- documentation impact
- testing performed

---

# Reviews

Treat review comments as engineering discussion.

Prefer evidence over opinion.

Preserve repository consistency.

---

# History

Do not rewrite shared history unless explicitly required.

Preserve traceability whenever possible.

---

# Git Checklist

Before considering work complete, verify:

- Commit scope is focused.
- Commit message is meaningful.
- No secrets committed.
- Documentation updated if needed.
- Tests executed where appropriate.
- Repository remains buildable.
- Unrelated changes excluded.
- History remains clean.

