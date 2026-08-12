package com.projectecho.intelligence.infrastructure.ai;

/** Record holding parameters for executing requests against AI providers (FD-0020). */
public record AiRequest(
        String prompt,
        String systemInstruction,
        String jsonSchema,
        double temperature,
        int maxTokens) {
    public AiRequest(final String prompt, final String systemInstruction) {
        this(prompt, systemInstruction, null, 0.7, 1000);
    }
}
