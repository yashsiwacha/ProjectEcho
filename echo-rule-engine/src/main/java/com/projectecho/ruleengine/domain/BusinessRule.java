package com.projectecho.ruleengine.domain;

import java.util.UUID;

public interface BusinessRule {
    UUID getRuleId();

    String getDescription();

    DecisionGraph evaluate(PassportStateSnapshot passport, MissionStateSnapshot mission);
}
