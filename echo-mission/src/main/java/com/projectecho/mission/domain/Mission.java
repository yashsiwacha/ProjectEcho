package com.projectecho.mission.domain;

import com.projectecho.shared.domain.AggregateRoot;
import com.projectecho.shared.events.MissionCreatedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "missions")
public class Mission extends AggregateRoot {

    @Column(nullable = false, length = 200)
    private MissionTitle title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MissionStatus status;

    protected Mission() {
        super(null);
    }

    private Mission(final UUID id, final MissionTitle title, final MissionStatus status) {
        super(id);
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    public static Mission draft(final UUID id, final String title) {
        final Mission mission = new Mission(id, new MissionTitle(title), MissionStatus.DRAFT);
        mission.registerEvent(
                new MissionCreatedEvent(
                        UUID.randomUUID(),
                        1,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        Instant.now(),
                        id,
                        title));
        return mission;
    }

    public void activate() {
        if (this.status != MissionStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT missions can be activated");
        }
        this.status = MissionStatus.ACTIVE;
    }

    public void archive() {
        if (this.status == MissionStatus.ARCHIVED) {
            throw new IllegalStateException("Mission is already ARCHIVED");
        }
        this.status = MissionStatus.ARCHIVED;
    }

    public MissionTitle getTitle() {
        return title;
    }

    public MissionStatus getStatus() {
        return status;
    }
}
