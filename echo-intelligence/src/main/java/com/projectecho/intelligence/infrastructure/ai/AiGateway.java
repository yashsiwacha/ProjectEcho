package com.projectecho.intelligence.infrastructure.ai;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Coordinating gateway coordinating AI model selection, circuit-breaker fallback routing, and cost
 * tracking log audits (FD-0020).
 */
@Component
public class AiGateway {

    private static final Logger LOG = LoggerFactory.getLogger(AiGateway.class);
    private final List<AiProvider> providers;

    // Self-contained Circuit Breaker state
    private final AtomicBoolean circuitOpen = new AtomicBoolean(false);
    private long lastStateTransitionTime = System.currentTimeMillis();
    private static final long COOLDOWN_PERIOD_MS = 30000; // 30 seconds

    public AiGateway(final List<AiProvider> providers) {
        this.providers = Objects.requireNonNull(providers);
    }

    /**
     * Routes parameters to the primary available provider with automatic circuit breaker fallback.
     */
    public AiResponse generate(final AiRequest request) {
        final AiProvider primary = getProvider("openai");
        final AiProvider fallback = getProvider("ollama");

        // Check if circuit breaker is open and needs cooldown check
        if (circuitOpen.get()) {
            if (System.currentTimeMillis() - lastStateTransitionTime > COOLDOWN_PERIOD_MS) {
                circuitOpen.set(false);
                LOG.info("Circuit Breaker transitioned to HALF-OPEN. Retrying primary provider.");
            } else {
                LOG.warn(
                        "Circuit Breaker is OPEN. Routing request directly to fallback provider: {}",
                        fallback.name());
                return executeWithObservability(fallback, request);
            }
        }

        try {
            // Attempt executing with primary
            return executeWithObservability(primary, request);
        } catch (final Exception ex) {
            // Open circuit breaker and transition to fallback
            circuitOpen.set(true);
            lastStateTransitionTime = System.currentTimeMillis();
            LOG.error(
                    "Primary provider failed. OPENING circuit breaker. Fallback to: {}",
                    fallback.name(),
                    ex);

            return executeWithObservability(fallback, request);
        }
    }

    private AiResponse executeWithObservability(
            final AiProvider provider, final AiRequest request) {
        final long start = System.currentTimeMillis();
        final AiResponse response = provider.execute(request);
        final long duration = System.currentTimeMillis() - start;

        // Structured observability logging
        MDC.put("ai.provider", provider.name());
        MDC.put("ai.duration_ms", String.valueOf(duration));
        MDC.put("ai.input_tokens", String.valueOf(response.inputTokens()));
        MDC.put("ai.output_tokens", String.valueOf(response.outputTokens()));
        MDC.put("ai.cost_usd", String.valueOf(response.costUsd()));

        try {
            if (LOG.isInfoEnabled()) {
                LOG.info(
                        "AI call metrics: Model={}, Duration={}ms, Input={}, Output={}, Cost=${}",
                        provider.name(),
                        duration,
                        response.inputTokens(),
                        response.outputTokens(),
                        response.costUsd());
            }
        } finally {
            MDC.remove("ai.provider");
            MDC.remove("ai.duration_ms");
            MDC.remove("ai.input_tokens");
            MDC.remove("ai.output_tokens");
            MDC.remove("ai.cost_usd");
        }

        return response;
    }

    private AiProvider getProvider(final String name) {
        return providers.stream()
                .filter(p -> p.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Provider not registered: " + name));
    }
}
