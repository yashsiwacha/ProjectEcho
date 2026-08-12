CREATE TABLE evidence_claims (
    id UUID NOT NULL,
    passport_id UUID NOT NULL,
    skill_id UUID NOT NULL,
    source_uri VARCHAR(2048) NOT NULL,
    validation_status VARCHAR(50) NOT NULL,
    trust_tier VARCHAR(50) NOT NULL,
    version BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
