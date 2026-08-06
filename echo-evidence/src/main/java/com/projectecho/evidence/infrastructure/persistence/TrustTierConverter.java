package com.projectecho.evidence.infrastructure.persistence;

import com.projectecho.evidence.domain.TrustTier;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@SuppressWarnings("PMD.UnnecessaryConstructor")
public class TrustTierConverter implements AttributeConverter<TrustTier, Integer> {

    public TrustTierConverter() {
        super();
    }

    @Override
    public Integer convertToDatabaseColumn(final TrustTier attribute) {
        return attribute == null ? null : attribute.getLevel();
    }

    @Override
    public TrustTier convertToEntityAttribute(final Integer dbData) {
        TrustTier result = null;
        if (dbData != null) {
            for (final TrustTier tier : TrustTier.values()) {
                if (tier.getLevel() == dbData) {
                    result = tier;
                }
            }
            if (result == null) {
                throw new IllegalArgumentException("Unknown TrustTier level: " + dbData);
            }
        }
        return result;
    }
}
