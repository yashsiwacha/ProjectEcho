package com.projectecho.shared.infrastructure.persistence;

import com.projectecho.shared.domain.MissionId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class MissionIdConverter implements AttributeConverter<MissionId, UUID> {

    public MissionIdConverter() {
        super();
    }

    @Override
    public UUID convertToDatabaseColumn(final MissionId attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public MissionId convertToEntityAttribute(final UUID dbData) {
        return dbData == null ? null : new MissionId(dbData);
    }
}
