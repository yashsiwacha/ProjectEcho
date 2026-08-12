CREATE TABLE identity_organizations (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    billing_plan VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE identity_teams (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_teams_organization FOREIGN KEY (organization_id) REFERENCES identity_organizations(id)
);

ALTER TABLE identity_users ADD COLUMN organization_id UUID;
ALTER TABLE identity_users ADD COLUMN team_id UUID;
ALTER TABLE identity_users ADD CONSTRAINT fk_users_organization FOREIGN KEY (organization_id) REFERENCES identity_organizations(id);
ALTER TABLE identity_users ADD CONSTRAINT fk_users_team FOREIGN KEY (team_id) REFERENCES identity_teams(id);
