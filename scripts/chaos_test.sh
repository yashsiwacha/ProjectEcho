#!/bin/bash
set -e

echo "Running Chaos Test: Simulating Redis Outage..."
docker pause project_echo-redis-1

echo "Redis paused. Executing load test to verify graceful degradation..."
node scripts/load_test.js || echo "Load test encountered errors (expected during chaos)"

echo "Restoring Redis..."
docker unpause project_echo-redis-1
echo "Redis restored. Waiting for recovery..."
sleep 5

echo "Running load test after recovery..."
node scripts/load_test.js

echo "Chaos test completed."
