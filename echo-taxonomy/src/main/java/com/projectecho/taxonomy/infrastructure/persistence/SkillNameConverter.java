package com.projectecho.taxonomy.infrastructure.persistence;

import com.projectecho.taxonomy.domain.SkillName;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class SkillNameConverter implements AttributeConverter<SkillName, String> {

    public SkillNameConverter() {
        super();
    }

    @Override
    public String convertToDatabaseColumn(final SkillName attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public SkillName convertToEntityAttribute(final String dbData) {
        return dbData == null ? null : new SkillName(dbData);
    }
}
