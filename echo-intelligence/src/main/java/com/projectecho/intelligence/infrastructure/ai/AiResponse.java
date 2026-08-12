package com.projectecho.intelligence.infrastructure.ai;

/** Record representing response metadata from an AI provider execution (FD-0020). */
public record AiResponse(
        String content, int inputTokens, int outputTokens, double costUsd, String providerName) {}
