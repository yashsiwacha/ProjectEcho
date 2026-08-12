#!/bin/bash
set -e

echo "Starting deployment of Project Echo..."
docker compose build
docker compose up -d --wait --wait-timeout 60 backend frontend

if [ $? -eq 0 ]; then
  echo "Deployment successful. Health checks passed."
else
  echo "Deployment failed health checks! Rolling back..."
  docker compose down backend frontend
  docker compose up -d backend frontend
  exit 1
fi
