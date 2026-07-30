package com.projectecho.evidence.application.command;

import java.util.UUID;

/** Command to append a new version to an existing EvidenceLineage. */
public record AppendEvidenceCommand(
    UUID lineageId, String provenance, double confidence, String artifactUrl) {}
