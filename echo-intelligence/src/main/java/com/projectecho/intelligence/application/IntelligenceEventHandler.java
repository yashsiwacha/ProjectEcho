package com.projectecho.intelligence.application;

import com.projectecho.shared.domain.MissionId;
import com.projectecho.shared.domain.PassportId;
import com.projectecho.shared.events.ReadinessAssessmentCompletedEvent;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class IntelligenceEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(IntelligenceEventHandler.class);
    private final IntelligenceService intelligenceService;

    public IntelligenceEventHandler(final IntelligenceService intelligenceService) {
        this.intelligenceService = Objects.requireNonNull(intelligenceService);
    }

    @Async
    @EventListener
    public void handleReadinessAssessmentCompleted(final ReadinessAssessmentCompletedEvent event) {
        if (LOG.isInfoEnabled()) {
            LOG.info(
                    "Received ReadinessAssessmentCompletedEvent for Mission {}", event.missionId());
        }

        intelligenceService.generateReasoningCard(
                new PassportId(event.passportId()),
                new MissionId(event.missionId()),
                event.isEligible(),
                event.score());
    }
}
