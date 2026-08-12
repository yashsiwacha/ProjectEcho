package com.projectecho.intelligence.infrastructure.ai;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Concrete OpenAI client using RestClient to call ChatGPT completions (FD-0020). */
@Component
public class OpenAiProvider implements AiProvider {

    private static final Logger LOG = LoggerFactory.getLogger(OpenAiProvider.class);
    private final RestClient restClient;

    @Value("${app.ai.openai.api-key:}")
    private String apiKey;

    public OpenAiProvider() {
        this.restClient = RestClient.builder().baseUrl("https://api.openai.com/v1").build();
    }

    @Override
    public AiResponse execute(final AiRequest request) {
        if (!isAvailable()) {
            // Simulator fallback if key is missing (for local test pipelines)
            LOG.warn("OpenAI API key missing. Simulating execution.");
            return new AiResponse(
                    "{\"eligible\":true,\"score\":95,\"rationale\":\"Simulated OpenAI response\"}",
                    100,
                    50,
                    0.003,
                    name());
        }

        try {
            final Map<String, Object> body =
                    Map.of(
                            "model", "gpt-4-turbo",
                            "messages",
                                    List.of(
                                            Map.of(
                                                    "role",
                                                    "system",
                                                    "content",
                                                    request.systemInstruction()),
                                            Map.of("role", "user", "content", request.prompt())),
                            "temperature", request.temperature(),
                            "max_tokens", request.maxTokens());

            final Map<?, ?> response =
                    restClient
                            .post()
                            .uri("/chat/completions")
                            .header("Authorization", "Bearer " + apiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(body)
                            .retrieve()
                            .body(Map.class);

            if (response != null && response.containsKey("choices")) {
                final List<?> choices = (List<?>) response.get("choices");
                if (!choices.isEmpty()) {
                    final Map<?, ?> choice = (Map<?, ?>) choices.get(0);
                    final Map<?, ?> message = (Map<?, ?>) choice.get("message");
                    final String content = (String) message.get("content");

                    final Map<?, ?> usage = (Map<?, ?>) response.get("usage");
                    final int inputTokens =
                            usage != null ? ((Number) usage.get("prompt_tokens")).intValue() : 0;
                    final int outputTokens =
                            usage != null
                                    ? ((Number) usage.get("completion_tokens")).intValue()
                                    : 0;
                    final double cost =
                            (inputTokens * 0.01 + outputTokens * 0.03)
                                    / 1000.0; // GPT-4 approximation

                    return new AiResponse(content, inputTokens, outputTokens, cost, name());
                }
            }
            throw new IllegalStateException("Empty choices returned from OpenAI");
        } catch (final Exception ex) {
            LOG.error("OpenAI execution failed: {}", ex.getMessage(), ex);
            throw new IllegalStateException("OpenAI execution failure", ex);
        }
    }

    @Override
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isBlank();
    }

    @Override
    public String name() {
        return "openai";
    }
}
