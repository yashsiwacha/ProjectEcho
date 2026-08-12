package com.projectecho.intelligence.application.ai;

/** Strategy interface defining operational contracts for concrete AI models (FD-0020). */
public interface AiProvider {

    /**
     * Executes the parameter prompt against the model.
     *
     * @param request parameter block mapping system prompt, inputs, and schemas.
     * @return model execution content and cost diagnostics.
     */
    AiResponse execute(AiRequest request);

    /** Returns true if this provider is currently available (healthcheck). */
    boolean isAvailable();

    /** Unique identifier for this provider. */
    String name();
}
