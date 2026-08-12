package com.projectecho.identity.domain;

import com.projectecho.shared.domain.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "identity_organizations")
public class Organization extends AggregateRoot {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String billingPlan;

    protected Organization() {
        super();
    }

    public Organization(final UUID id, final String name, final String billingPlan) {
        super(id);
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.billingPlan = Objects.requireNonNull(billingPlan, "Billing plan cannot be null");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
        markUpdated();
    }

    public String getBillingPlan() {
        return billingPlan;
    }

    public void setBillingPlan(String billingPlan) {
        this.billingPlan = Objects.requireNonNull(billingPlan);
        markUpdated();
    }
}
