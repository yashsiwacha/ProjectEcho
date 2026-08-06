package com.projectecho.shared.infrastructure.persistence;

import com.projectecho.shared.domain.PassportId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class PassportIdConverter implements AttributeConverter<PassportId, UUID> {

    public PassportIdConverter() {
        super();
    }

    @Override
    public UUID convertToDatabaseColumn(final PassportId attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public PassportId convertToEntityAttribute(final UUID dbData) {
        return dbData == null ? null : new PassportId(dbData);
    }
}
