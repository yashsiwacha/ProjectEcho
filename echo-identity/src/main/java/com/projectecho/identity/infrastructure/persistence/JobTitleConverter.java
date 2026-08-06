package com.projectecho.identity.infrastructure.persistence;

import com.projectecho.identity.domain.JobTitle;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class JobTitleConverter implements AttributeConverter<JobTitle, String> {

    public JobTitleConverter() {
        super();
    }

    @Override
    public String convertToDatabaseColumn(final JobTitle attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public JobTitle convertToEntityAttribute(final String dbData) {
        return dbData == null ? null : new JobTitle(dbData);
    }
}
