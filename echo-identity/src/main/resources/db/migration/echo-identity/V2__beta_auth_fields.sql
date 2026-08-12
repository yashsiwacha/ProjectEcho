ALTER TABLE identity_users ADD COLUMN email_verified BOOLEAN DEFAULT FALSE;
ALTER TABLE identity_users ADD COLUMN email_verification_token VARCHAR(255);
ALTER TABLE identity_users ADD COLUMN reset_token VARCHAR(255);
ALTER TABLE identity_users ADD COLUMN reset_token_expiry TIMESTAMP;
