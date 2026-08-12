CREATE TABLE readiness_assessments (
    id UUID NOT NULL,
    passport_id UUID NOT NULL,
    mission_id UUID NOT NULL,
    is_eligible BOOLEAN NOT NULL,
    score INTEGER NOT NULL,
    graph_id UUID NOT NULL,
    version BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
