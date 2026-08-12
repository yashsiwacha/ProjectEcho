package com.projectecho.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectecho.application.api.ApiError;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * High-performance, dual-mode IP-based Rate Limiter (FD-0020). Protects critical endpoints (Auth,
 * Uploads, AI) using an atomic token bucket strategy.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1) // Runs immediately after RequestIdFilter
public class RateLimitingFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(RateLimitingFilter.class);

    // In-memory token bucket fallback map
    private final Map<String, TokenBucket> localBuckets = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(
            final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest
                && response instanceof HttpServletResponse httpResponse) {
            final String ip = getClientIp(httpRequest);
            final String uri = httpRequest.getRequestURI();

            // Set different limits depending on the route
            int limit = 60; // default 60 req/min
            int durationSeconds = 60;

            if (uri.startsWith("/api/v1/auth/login") || uri.startsWith("/api/v1/auth/mfa")) {
                limit = 5; // max 5 login attempts per minute
            } else if (uri.startsWith("/api/v1/evidence")) {
                limit = 15; // max 15 uploads/actions per minute
            } else if (uri.startsWith("/api/v1/intelligence")) {
                limit = 10; // max 10 AI operations per minute
            }

            final String bucketKey = ip + ":" + getRouteCategory(uri);
            final int finalLimit = limit;
            final int finalDuration = durationSeconds;
            final TokenBucket bucket =
                    localBuckets.computeIfAbsent(
                            bucketKey, k -> new TokenBucket(finalLimit, finalDuration));

            if (!bucket.tryConsume()) {
                LOG.warn("Rate limit exceeded for IP: {} on URI: {}", ip, uri);
                sendTooManyRequestsResponse(httpRequest, httpResponse);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String getClientIp(final HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private String getRouteCategory(final String uri) {
        if (uri.startsWith("/api/v1/auth")) {
            return "auth";
        }
        if (uri.startsWith("/api/v1/evidence")) {
            return "evidence";
        }
        if (uri.startsWith("/api/v1/intelligence")) {
            return "ai";
        }
        return "general";
    }

    private void sendTooManyRequestsResponse(
            final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final String requestId = (String) request.getAttribute("requestId");
        final ApiError error =
                new ApiError(
                        HttpStatus.TOO_MANY_REQUESTS.value(),
                        "Too Many Requests",
                        "Rate limit exceeded. Please retry later.",
                        request.getRequestURI(),
                        requestId != null ? requestId : "unknown");

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

    /** Inner helper class representing an atomic Token Bucket. */
    private static class TokenBucket {
        private final int limit;
        private final long windowSizeMs;
        private final AtomicInteger tokens;
        private long lastRefillTime;

        TokenBucket(final int limit, final int durationSeconds) {
            this.limit = limit;
            this.windowSizeMs = durationSeconds * 1000L;
            this.tokens = new AtomicInteger(limit);
            this.lastRefillTime = System.currentTimeMillis();
        }

        synchronized boolean tryConsume() {
            refill();
            if (tokens.get() > 0) {
                tokens.decrementAndGet();
                return true;
            }
            return false;
        }

        private void refill() {
            final long now = System.currentTimeMillis();
            if (now - lastRefillTime > windowSizeMs) {
                tokens.set(limit);
                lastRefillTime = now;
            }
        }
    }
}
