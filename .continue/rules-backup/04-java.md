# Java Rules

## Purpose

This rule defines Java engineering practices for ProjectEcho.

It covers Java language usage only.

Framework-specific guidance belongs in Spring rules.

---

# Java Version

Always target:

- Java 21

Use modern language features where they improve readability and maintainability.

Avoid legacy patterns when modern alternatives exist.

---

# Code Style

Write code that is:

- readable
- maintainable
- deterministic
- self-documenting

Prefer expressive names over comments.

Avoid clever code.

Optimize for clarity.

---

# Object-Oriented Design

Prefer:

- composition over inheritance
- immutable objects where practical
- encapsulation
- explicit dependencies
- small focused classes

Avoid:

- god objects
- deep inheritance hierarchies
- mutable shared state

---

# Null Handling

Avoid returning null.

Prefer:

- Optional
- empty collections
- explicit validation

Fail early when invalid input is detected.

---

# Collections

Prefer interfaces:

- List
- Set
- Map

Do not expose mutable internal collections.

Return immutable collections whenever practical.

---

# Exceptions

Use exceptions for exceptional situations.

Do not use exceptions for normal control flow.

Create meaningful exception messages.

Preserve root causes.

---

# Concurrency

Prefer thread-safe designs.

Avoid shared mutable state.

Use Java concurrency utilities instead of manual thread management whenever possible.

---

# Logging

Produce meaningful logs.

Do not log sensitive information.

Use appropriate log levels.

Avoid excessive logging.

---

# Performance

Optimize only after understanding the problem.

Prefer readability over micro-optimizations.

Avoid premature optimization.

---

# Code Review Checklist

Before completing Java work, verify:

- Java 21 compatible.
- Clear naming.
- Small methods.
- Explicit dependencies.
- Proper exception handling.
- No duplicated logic.
- Readable implementation.
- Maintainable design.

