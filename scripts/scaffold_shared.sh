#!/bin/bash
set -e
BASE_DIR="/Users/yash/Yash-Workspace/projects/active/project-echo/echo-shared/src/main/java/com/projectecho/shared"

mkdir -p "$BASE_DIR/domain"
mkdir -p "$BASE_DIR/events"
mkdir -p "$BASE_DIR/exception"

# Aggregate Root
cat << JAVA > "$BASE_DIR/domain/AggregateRoot.java"
package com.projectecho.shared.domain;

import java.time.Instant;
import java.util.UUID;

public abstract class AggregateRoot {
    private final UUID id;
    private Long version;
    private final Instant createdAt;
    private Instant updatedAt;

    protected AggregateRoot(UUID id) {
        this.id = id;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() { return id; }
    public Long getVersion() { return version; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    protected void markUpdated() {
        this.updatedAt = Instant.now();
    }
}
JAVA

# Exceptions
cat << JAVA > "$BASE_DIR/exception/DomainException.java"
package com.projectecho.shared.exception;

public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }
}
JAVA

cat << JAVA > "$BASE_DIR/exception/ResourceNotFoundException.java"
package com.projectecho.shared.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
JAVA

# Value Objects
cat << JAVA > "$BASE_DIR/domain/PassportId.java"
package com.projectecho.shared.domain;

import java.util.UUID;
import java.util.Objects;

public record PassportId(UUID value) {
    public PassportId {
        Objects.requireNonNull(value, "PassportId cannot be null");
    }
}
JAVA

cat << JAVA > "$BASE_DIR/domain/SkillId.java"
package com.projectecho.shared.domain;

import java.util.UUID;
import java.util.Objects;

public record SkillId(UUID value) {
    public SkillId {
        Objects.requireNonNull(value, "SkillId cannot be null");
    }
}
JAVA

cat << JAVA > "$BASE_DIR/domain/TrustTier.java"
package com.projectecho.shared.domain;

public sealed interface TrustTier permits High, Medium, Low {
    String name();
}

final class High implements TrustTier {
    @Override public String name() { return "HIGH"; }
}

final class Medium implements TrustTier {
    @Override public String name() { return "MEDIUM"; }
}

final class Low implements TrustTier {
    @Override public String name() { return "LOW"; }
}
JAVA

# Events (ADR-014 Compliance)
cat << JAVA > "$BASE_DIR/events/IntegrationEvent.java"
package com.projectecho.shared.events;

import java.time.Instant;
import java.util.UUID;

public interface IntegrationEvent {
    UUID eventId();
    int eventVersion();
    UUID correlationId();
    UUID causationId();
    Instant timestamp();
    UUID aggregateId();
}
JAVA

cat << JAVA > "$BASE_DIR/events/TrustTierAssessedEvent.java"
package com.projectecho.shared.events;

import java.time.Instant;
import java.util.UUID;
import com.projectecho.shared.domain.PassportId;

public record TrustTierAssessedEvent(
    UUID eventId,
    int eventVersion,
    UUID correlationId,
    UUID causationId,
    Instant timestamp,
    UUID aggregateId,
    PassportId passportId,
    String trustTier
) implements IntegrationEvent {}
JAVA

cat << JAVA > "$BASE_DIR/events/IntelligenceScoreCalculatedEvent.java"
package com.projectecho.shared.events;

import java.time.Instant;
import java.util.UUID;
import com.projectecho.shared.domain.PassportId;

public record IntelligenceScoreCalculatedEvent(
    UUID eventId,
    int eventVersion,
    UUID correlationId,
    UUID causationId,
    Instant timestamp,
    UUID aggregateId,
    PassportId passportId,
    int score
) implements IntegrationEvent {}
JAVA

echo "Shared Kernel scaffolded."
