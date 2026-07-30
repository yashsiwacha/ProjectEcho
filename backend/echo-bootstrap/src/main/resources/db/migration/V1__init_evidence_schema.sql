CREATE TABLE evidence_lineage (
    id UUID PRIMARY KEY,
    person_id UUID NOT NULL,
    capability_id UUID NOT NULL,
    version INT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_evidence_lineage_owner ON evidence_lineage (person_id, capability_id);

CREATE TABLE evidence_version (
    id UUID PRIMARY KEY,
    lineage_id UUID NOT NULL,
    sequence_number INT NOT NULL,
    provenance VARCHAR(50) NOT NULL,
    confidence NUMERIC(3,2) NOT NULL,
    artifact_url VARCHAR(1024) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_evidence_version_lineage FOREIGN KEY (lineage_id) REFERENCES evidence_lineage (id) ON DELETE CASCADE,
    CONSTRAINT chk_sequence_positive CHECK (sequence_number > 0),
    CONSTRAINT chk_confidence_bounds CHECK (confidence >= 0.0 AND confidence <= 1.0),
    CONSTRAINT uq_lineage_sequence UNIQUE (lineage_id, sequence_number)
);

CREATE INDEX idx_evidence_version_lineage ON evidence_version (lineage_id, sequence_number DESC);
