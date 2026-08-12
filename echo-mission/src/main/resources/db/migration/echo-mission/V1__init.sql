CREATE TABLE missions (
    id UUID NOT NULL,
    title VARCHAR(200) NOT NULL,
    status VARCHAR(50) NOT NULL,
    version BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
