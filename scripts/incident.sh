#!/bin/bash
set -e

INCIDENT_ID="INC-$(date +%s)"
INCIDENT_DIR="docs/incidents/${INCIDENT_ID}"
mkdir -p "$INCIDENT_DIR"

cat << EOF > "${INCIDENT_DIR}/README.md"
# Incident ${INCIDENT_ID}
**Date**: $(date)
**Status**: Investigating
**Severity**: Sev-2

## Timeline
- $(date): Incident declared

## Root Cause Analysis
(To be filled)

## Action Items
(To be filled)
EOF

echo "Incident ${INCIDENT_ID} initialized in ${INCIDENT_DIR}/README.md"
