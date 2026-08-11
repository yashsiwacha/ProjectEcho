package com.projectecho.identity.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

/**
 * Stateless JWT issuance and validation service.
 *
 * <p>Implements the token strategy defined in ADR-006 (Identity Authentication Mechanism) and the
 * secrets management policy defined in ADR-011. The signing secret is injected via {@link
 * JwtProperties} and is never hard-coded.
 *
 * <p>Token structure (claims):
 *
 * <ul>
 *   <li>{@code sub} – passport UUID (subject)
 *   <li>{@code email}– user email address
 *   <li>{@code iss} – issuer (configured via {@code echo.security.jwt.issuer})
 *   <li>{@code iat} – issued-at timestamp
 *   <li>{@code exp} – expiry timestamp
 * </ul>
 */
@Component
public class JwtService {

    private static final String CLAIM_EMAIL = "email";

    private final SecretKey signingKey;
    private final JwtProperties properties;

    /**
     * Constructs a {@code JwtService} from externalized properties.
     *
     * @param properties JWT configuration (secret, expiry, issuer)
     * @throws IllegalArgumentException if the secret is null or blank
     */
    public JwtService(final JwtProperties properties) {
        if (properties.getSecret() == null || properties.getSecret().isBlank()) {
            throw new IllegalArgumentException(
                    "echo.security.jwt.secret must be set via environment variable. "
                            + "See ADR-011 and .env.example.");
        }
        this.properties = properties;
        this.signingKey =
                Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Issues a signed JWT for the given passport holder.
     *
     * @param passportId the unique passport UUID used as the {@code sub} claim
     * @param email the user's email address embedded as a custom claim
     * @return a compact, URL-safe JWT string
     */
    public String issueToken(final UUID passportId, final String email) {
        final long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(passportId.toString())
                .claim(CLAIM_EMAIL, email)
                .issuer(properties.getIssuer())
                .issuedAt(new Date(now))
                .expiration(new Date(now + properties.getExpirationMs()))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Validates a JWT and extracts its claims.
     *
     * @param token the compact JWT string
     * @return parsed {@link Claims} if the token is valid
     * @throws JwtException if the token is malformed, expired, or has an invalid signature
     */
    public Claims validateAndExtractClaims(final String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }

    /**
     * Extracts the subject (passport UUID) from a validated token.
     *
     * @param token the compact JWT string
     * @return the passport UUID string
     */
    public String extractSubject(final String token) {
        return validateAndExtractClaims(token).getSubject();
    }

    /**
     * Extracts the email claim from a validated token.
     *
     * @param token the compact JWT string
     * @return the email address
     */
    public String extractEmail(final String token) {
        return validateAndExtractClaims(token).get(CLAIM_EMAIL, String.class);
    }

    /**
     * Returns {@code true} if the token is valid and not expired; {@code false} otherwise. Does not
     * throw — use this for filter-layer checks.
     *
     * @param token the compact JWT string
     * @return validity flag
     */
    public boolean isTokenValid(final String token) {
        try {
            validateAndExtractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
