package com.projectecho.identity.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Externalized JWT configuration properties.
 *
 * <p>All values are loaded from environment variables or Spring configuration — never from
 * hard-coded source code. See ADR-011 and ADR-003 (SEC-01).
 *
 * <pre>
 * Required environment variable:
 *   JWT_SECRET  – HS256 signing secret, minimum 256-bit (32-character) Base64-encoded string.
 *
 * Optional (with defaults):
 *   JWT_EXPIRATION_MS – token lifetime in milliseconds (default: 86400000 = 24 h)
 *   JWT_ISSUER        – token issuer claim (default: project-echo)
 * </pre>
 */
@ConfigurationProperties(prefix = "echo.security.jwt")
public final class JwtProperties {

    /**
     * HS256 signing secret. Must be provided via the {@code ECHO_SECURITY_JWT_SECRET} environment
     * variable (or {@code echo.security.jwt.secret} Spring property). No default — application
     * startup will fail if unset.
     */
    private String secret;

    /** Token lifetime in milliseconds. Defaults to 24 hours. */
    private long expirationMs = 86_400_000L;

    /** Issuer claim embedded in every token. */
    private String issuer = "project-echo";

    // ------------------------------------------------------------------ getters / setters

    public String getSecret() {
        return secret;
    }

    public void setSecret(final String secret) {
        this.secret = secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(final long expirationMs) {
        this.expirationMs = expirationMs;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }
}
