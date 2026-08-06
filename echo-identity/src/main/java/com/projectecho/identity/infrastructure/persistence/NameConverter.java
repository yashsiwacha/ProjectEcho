package com.projectecho.identity.infrastructure.persistence;

import com.projectecho.identity.domain.Name;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class NameConverter implements AttributeConverter<Name, String> {

    public NameConverter() {
        super();
    }

    @Override
    public String convertToDatabaseColumn(final Name attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public Name convertToEntityAttribute(final String dbData) {
        return dbData == null ? null : new Name(dbData);
    }
}
