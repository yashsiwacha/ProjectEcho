ALTER TABLE evidence_claims ADD CONSTRAINT uq_evidence_passport_skill_uri UNIQUE (passport_id, skill_id, source_uri);
