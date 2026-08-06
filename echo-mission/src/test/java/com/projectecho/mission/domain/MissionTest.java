package com.projectecho.mission.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class MissionTest {

    @Test
    void shouldCreateDraftMission() {
        final UUID id = UUID.randomUUID();
        final String title = "Senior Software Engineer";

        final Mission mission = Mission.draft(id, title);

        assertThat(mission.getId()).isEqualTo(id);
        assertThat(mission.getTitle().value()).isEqualTo(title);
        assertThat(mission.getStatus()).isEqualTo(MissionStatus.DRAFT);
        assertThat(mission.getDomainEvents()).hasSize(1);
    }

    @Test
    void shouldActivateMission() {
        final Mission mission = Mission.draft(UUID.randomUUID(), "Test");
        mission.activate();
        assertThat(mission.getStatus()).isEqualTo(MissionStatus.ACTIVE);
    }

    @Test
    void shouldFailToActivateIfArchived() {
        final Mission mission = Mission.draft(UUID.randomUUID(), "Test");
        mission.archive();

        assertThatThrownBy(mission::activate)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Only DRAFT missions can be activated");
    }

    @Test
    void shouldFailToCreateWithBlankTitle() {
        assertThatThrownBy(() -> Mission.draft(UUID.randomUUID(), "  "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
