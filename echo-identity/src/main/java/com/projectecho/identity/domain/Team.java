package com.projectecho.identity.domain;

import com.projectecho.shared.domain.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "identity_teams")
public class Team extends AggregateRoot {

    @Column(nullable = false)
    private UUID organizationId;

    @Column(nullable = false)
    private String name;

    protected Team() {
        super();
    }

    public Team(final UUID id, final UUID organizationId, final String name) {
        super(id);
        this.organizationId =
                Objects.requireNonNull(organizationId, "Organization ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
        markUpdated();
    }
}
