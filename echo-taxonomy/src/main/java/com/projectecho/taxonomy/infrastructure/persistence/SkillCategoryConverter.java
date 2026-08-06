package com.projectecho.taxonomy.infrastructure.persistence;

import com.projectecho.taxonomy.domain.SkillCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class SkillCategoryConverter implements AttributeConverter<SkillCategory, String> {

    public SkillCategoryConverter() {
        super();
    }

    @Override
    public String convertToDatabaseColumn(final SkillCategory attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public SkillCategory convertToEntityAttribute(final String dbData) {
        return dbData == null ? null : new SkillCategory(dbData);
    }
}
