package com.projectecho.intelligence.infrastructure.persistence;

import com.projectecho.intelligence.domain.ConfidenceScore;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class ConfidenceScoreConverter implements AttributeConverter<ConfidenceScore, Integer> {

    public ConfidenceScoreConverter() {
        super();
    }

    @Override
    public Integer convertToDatabaseColumn(final ConfidenceScore attribute) {
        return attribute == null ? null : attribute.percentage();
    }

    @Override
    public ConfidenceScore convertToEntityAttribute(final Integer dbData) {
        return dbData == null ? null : new ConfidenceScore(dbData);
    }
}
