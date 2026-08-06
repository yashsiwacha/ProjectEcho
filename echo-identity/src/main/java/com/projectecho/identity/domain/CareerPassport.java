package com.projectecho.identity.domain;

import com.projectecho.shared.domain.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "identity_passports")
public class CareerPassport extends AggregateRoot {

    @Column(nullable = false)
    private Name name;

    @Column(nullable = false, unique = true)
    private EmailAddress email;

    @Column(nullable = false)
    private JobTitle jobTitle;

    protected CareerPassport() {
        super();
        // JPA
    }

    public CareerPassport(
            final UUID id, final Name name, final EmailAddress email, final JobTitle jobTitle) {
        super(id);
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.jobTitle = Objects.requireNonNull(jobTitle, "Job title cannot be null");
    }

    public Name getName() {
        return name;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }
}
