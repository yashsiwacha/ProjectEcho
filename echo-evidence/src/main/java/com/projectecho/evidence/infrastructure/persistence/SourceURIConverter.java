package com.projectecho.evidence.infrastructure.persistence;

import com.projectecho.evidence.domain.SourceURI;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class SourceURIConverter implements AttributeConverter<SourceURI, String> {

    public SourceURIConverter() {
        super();
    }

    @Override
    public String convertToDatabaseColumn(final SourceURI attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public SourceURI convertToEntityAttribute(final String dbData) {
        return dbData == null ? null : new SourceURI(dbData);
    }
}
