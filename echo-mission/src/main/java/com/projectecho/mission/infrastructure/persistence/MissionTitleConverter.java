package com.projectecho.mission.infrastructure.persistence;

import com.projectecho.mission.domain.MissionTitle;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class MissionTitleConverter implements AttributeConverter<MissionTitle, String> {

    public MissionTitleConverter() {
        super();
    }

    @Override
    public String convertToDatabaseColumn(final MissionTitle attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public MissionTitle convertToEntityAttribute(final String dbData) {
        return dbData == null ? null : new MissionTitle(dbData);
    }
}
