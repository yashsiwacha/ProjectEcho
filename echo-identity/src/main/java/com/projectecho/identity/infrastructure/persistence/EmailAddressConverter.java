package com.projectecho.identity.infrastructure.persistence;

import com.projectecho.identity.domain.EmailAddress;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class EmailAddressConverter implements AttributeConverter<EmailAddress, String> {

    public EmailAddressConverter() {
        super();
    }

    @Override
    public String convertToDatabaseColumn(final EmailAddress attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public EmailAddress convertToEntityAttribute(final String dbData) {
        return dbData == null ? null : new EmailAddress(dbData);
    }
}
