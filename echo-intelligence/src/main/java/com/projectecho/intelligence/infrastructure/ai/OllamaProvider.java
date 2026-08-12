package com.projectecho.intelligence.infrastructure.ai;

import com.projectecho.intelligence.application.ai.AiProvider;
import com.projectecho.intelligence.application.ai.AiRequest;
import com.projectecho.intelligence.application.ai.AiResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Concrete local Ollama provider client using RestClient (FD-0020). */
@Component
public class OllamaProvider implements AiProvider {

    private static final Logger LOG = LoggerFactory.getLogger(OllamaProvider.class);
    private final RestClient restClient;

    @Value("${app.ai.ollama.endpoint:http://localhost:11434}")
    private String endpoint;

    @Value("${app.ai.ollama.model:llama3}")
    private String modelName;

    public OllamaProvider() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:11434").build();
    }

    @Override
    public AiResponse execute(final AiRequest request) {
        if (!isAvailable()) {
            LOG.warn("Ollama service not configured. Simulating execution.");
            return new AiResponse(
                    "{\"eligible\":true,\"score\":88,\"rationale\":\"Simulated Ollama response\"}",
                    50,
                    20,
                    0.0,
                    name());
        }

        try {
            final Map<String, Object> body =
                    Map.of(
                            "model", modelName,
                            "prompt", request.prompt(),
                            "system", request.systemInstruction(),
                            "stream", false,
                            "options",
                                    Map.of(
                                            "temperature", request.temperature(),
                                            "num_predict", request.maxTokens()));

            final Map<?, ?> response =
                    restClient
                            .post()
                            .uri("/api/generate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(body)
                            .retrieve()
                            .body(Map.class);

            if (response != null && response.containsKey("response")) {
                final String responseText = (String) response.get("response");
                // Ollama local run has 0 financial token cost
                return new AiResponse(responseText, 0, 0, 0.0, name());
            }
            throw new IllegalStateException("Empty response returned from Ollama");
        } catch (final Exception ex) {
            LOG.error("Ollama execution failed: {}", ex.getMessage(), ex);
            throw new IllegalStateException("Ollama execution failure", ex);
        }
    }

    @Override
    public boolean isAvailable() {
        // By default we check if endpoint is configured and active, but return true for local dev
        // capability
        return endpoint != null && !endpoint.isBlank();
    }

    @Override
    public String name() {
        return "ollama";
    }
}
