package com.projectecho.shared.infrastructure.persistence;

import com.projectecho.shared.domain.SkillId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class SkillIdConverter implements AttributeConverter<SkillId, UUID> {

    public SkillIdConverter() {
        super();
    }

    @Override
    public UUID convertToDatabaseColumn(final SkillId attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public SkillId convertToEntityAttribute(final UUID dbData) {
        return dbData == null ? null : new SkillId(dbData);
    }
}
