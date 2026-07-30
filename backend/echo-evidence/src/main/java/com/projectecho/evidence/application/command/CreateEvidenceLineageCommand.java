package com.projectecho.evidence.application.command;

import java.util.UUID;

/**
 * Command to create a new EvidenceLineage. Uses primitive/standard Java types to prevent domain
 * logic leakage into the application boundary.
 */
public record CreateEvidenceLineageCommand(
    UUID personId, UUID capabilityId, String provenance, double confidence, String artifactUrl) {}
