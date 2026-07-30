CREATE TABLE outbox_message (
    id UUID PRIMARY KEY,
    aggregate_id UUID NOT NULL,
    aggregate_type VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    payload TEXT NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_outbox_message_processed ON outbox_message (processed) WHERE processed = false;
CREATE INDEX idx_outbox_message_aggregate ON outbox_message (aggregate_id);
