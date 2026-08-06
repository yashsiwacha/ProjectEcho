package com.projectecho.evidence.infrastructure.persistence;

import com.projectecho.evidence.domain.ValidationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class ValidationStatusConverter implements AttributeConverter<ValidationStatus, String> {

    public ValidationStatusConverter() {
        super();
    }

    @Override
    public String convertToDatabaseColumn(final ValidationStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public ValidationStatus convertToEntityAttribute(final String dbData) {
        return dbData == null ? null : ValidationStatus.valueOf(dbData);
    }
}
