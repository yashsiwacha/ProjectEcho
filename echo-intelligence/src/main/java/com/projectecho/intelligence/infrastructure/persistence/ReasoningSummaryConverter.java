package com.projectecho.intelligence.infrastructure.persistence;

import com.projectecho.intelligence.domain.ReasoningSummary;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class ReasoningSummaryConverter implements AttributeConverter<ReasoningSummary, String> {

    public ReasoningSummaryConverter() {
        super();
    }

    @Override
    public String convertToDatabaseColumn(final ReasoningSummary attribute) {
        return attribute == null ? null : attribute.text();
    }

    @Override
    public ReasoningSummary convertToEntityAttribute(final String dbData) {
        return dbData == null ? null : new ReasoningSummary(dbData);
    }
}
