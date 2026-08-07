# Evidence Upload Test Report

## Expected Behaviour
Candidates can submit evidence claims linking their passport to a verified skill via source URI. Evidence is initialized with status PENDING and trust tier TIER_1.

## Actual Behaviour
Verified via HTTP POST `/api/v1/evidence`. Created evidence claim `25e8a009-6af9-4c9f-982f-343346d8a4e8` linked to passport `875bbd97-78b1-4c27-8718-d20e2f920854` and skill `fec1592c-3280-4abc-95ac-1ae026b25790`.

## Root Cause
No core defect; validated against JPA persistence layer.

## Files Changed
- None

## Tests Executed
- `POST /api/v1/evidence` (HTTP 201 Created)

## Final Status
**PASS**
