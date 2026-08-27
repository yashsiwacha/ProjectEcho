package com.projectecho.shared.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AggregateRoot {

    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Version private Long version;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy @Column private String lastModifiedBy;

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

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
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
