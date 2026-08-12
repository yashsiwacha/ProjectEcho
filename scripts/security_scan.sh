#!/bin/bash
set -e
echo "Running Trivy Vulnerability Scan..."
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ghcr.io/projectecho/backend:latest
echo "Generating SBOM..."
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image --format spdx-json --output /sbom.json ghcr.io/projectecho/backend:latest
echo "Security Scan Complete."
