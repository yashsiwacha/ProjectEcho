# Testing Rules

## Purpose

This rule defines testing practices for ProjectEcho.

Testing exists to verify correctness, protect architecture, and prevent
regressions.

Tests are engineering assets and should be maintained with the same care as
production code.

---

# Testing Philosophy

Every meaningful change should be verifiable.

Testing should provide confidence rather than simply increase code coverage.

Prefer testing observable behavior instead of implementation details.

---

# Test Pyramid

Maintain a balanced testing strategy.

Prefer:

- Unit Tests
- Integration Tests
- End-to-End Tests (where appropriate)

Avoid relying exclusively on one testing level.

---

# Unit Tests

Unit tests should:

- execute quickly
- remain deterministic
- isolate the component under test
- avoid unnecessary infrastructure

Mock only external collaborators.

Do not mock the class being tested.

---

# Integration Tests

Integration tests verify:

- Spring configuration
- persistence
- transactions
- messaging
- API behavior

Use integration tests when multiple components interact.

---

# Test Design

Every test should clearly express:

- setup
- action
- expected outcome

Each test should verify one primary behavior.

Prefer readability over cleverness.

---

# Naming

Use descriptive test names.

A reader should understand the behavior being verified without reading the
implementation.

---

# Assertions

Assert observable outcomes.

Avoid excessive assertions in a single test.

Verify behavior that matters to users or the domain.

---

# Test Data

Keep test data:

- minimal
- readable
- deterministic

Avoid unnecessary fixtures.

---

# Regression Testing

Whenever fixing a defect:

1. Write a test that reproduces the issue.
2. Verify the test fails.
3. Implement the fix.
4. Verify the test passes.

---

# Maintainability

Tests should be:

- readable
- independent
- repeatable
- easy to maintain

Avoid duplicated test logic.

Extract common utilities only when they improve clarity.

---

# Performance

Tests should execute efficiently.

Avoid unnecessary delays, sleeps, or dependence on external systems unless
explicitly required.

---

# Testing Checklist

Before completing work, verify:

- Critical behavior tested.
- Existing tests still pass.
- New functionality covered.
- Regression tests added when appropriate.
- Tests remain deterministic.
- Test names are descriptive.
- No unnecessary duplication.
- Test suite remains maintainable.

