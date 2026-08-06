package com.projectecho.shared.domain;

public sealed interface TrustTier permits High, Medium, Low {
    String name();
}

@SuppressWarnings({"PMD.ShortClassName", "PMD.AtLeastOneConstructor"})
final class High implements TrustTier {
    @Override
    public String name() {
        return "HIGH";
    }
}

@SuppressWarnings({"PMD.AtLeastOneConstructor"})
final class Medium implements TrustTier {
    @Override
    public String name() {
        return "MEDIUM";
    }
}

@SuppressWarnings({"PMD.ShortClassName", "PMD.AtLeastOneConstructor"})
final class Low implements TrustTier {
    @Override
    public String name() {
        return "LOW";
    }
}
