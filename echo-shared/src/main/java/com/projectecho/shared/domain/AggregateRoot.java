package com.projectecho.shared.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AggregateRoot {

    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Version private Long version;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Transient private final transient List<Object> domainEvents = new ArrayList<>();

    protected AggregateRoot() {
        // JPA
    }

    protected AggregateRoot(final UUID id) {
        Objects.requireNonNull(id, "Aggregate ID cannot be null");
        this.id = id;
        this.version = 0L;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    protected void markUpdated() {
        this.updatedAt = Instant.now();
    }

    protected void registerEvent(final Object event) {
        Objects.requireNonNull(event, "Domain event must not be null");
        this.domainEvents.add(event);
    }

    public Collection<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
