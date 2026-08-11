package com.projectecho.identity.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtService}.
 *
 * <p>Validates that JWT issuance and validation behave correctly, that the secret is never
 * hard-coded, and that tampering or expiry is detected (SEC-01).
 */
@DisplayName("JwtService")
class JwtServiceTest {

    private static final String VALID_SECRET =
            "test-secret-that-is-long-enough-for-hmac-sha256-algorithm-32chars";
    private static final String EMAIL = "user@example.com";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        final JwtProperties properties = new JwtProperties();
        properties.setSecret(VALID_SECRET);
        properties.setExpirationMs(3_600_000L); // 1 hour
        properties.setIssuer("project-echo-test");
        jwtService = new JwtService(properties);
    }

    @Test
    @DisplayName("issueToken returns a non-blank JWT")
    void issueToken_returnsNonBlankToken() {
        final UUID passportId = UUID.randomUUID();
        final String token = jwtService.issueToken(passportId, EMAIL);
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("issued token contains correct subject and email claims")
    void issueToken_containsCorrectClaims() {
        final UUID passportId = UUID.randomUUID();
        final String token = jwtService.issueToken(passportId, EMAIL);

        final Claims claims = jwtService.validateAndExtractClaims(token);

        assertThat(claims.getSubject()).isEqualTo(passportId.toString());
        assertThat(claims.get("email", String.class)).isEqualTo(EMAIL);
        assertThat(claims.getIssuer()).isEqualTo("project-echo-test");
    }

    @Test
    @DisplayName("extractSubject returns correct passport UUID string")
    void extractSubject_returnsCorrectValue() {
        final UUID passportId = UUID.randomUUID();
        final String token = jwtService.issueToken(passportId, EMAIL);
        assertThat(jwtService.extractSubject(token)).isEqualTo(passportId.toString());
    }

    @Test
    @DisplayName("extractEmail returns correct email claim")
    void extractEmail_returnsCorrectValue() {
        final UUID passportId = UUID.randomUUID();
        final String token = jwtService.issueToken(passportId, EMAIL);
        assertThat(jwtService.extractEmail(token)).isEqualTo(EMAIL);
    }

    @Test
    @DisplayName("isTokenValid returns true for a valid, non-expired token")
    void isTokenValid_returnsTrueForValidToken() {
        final String token = jwtService.issueToken(UUID.randomUUID(), EMAIL);
        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    @DisplayName("isTokenValid returns false for a tampered token")
    void isTokenValid_returnsFalseForTamperedToken() {
        final String token = jwtService.issueToken(UUID.randomUUID(), EMAIL) + "tampered";
        assertThat(jwtService.isTokenValid(token)).isFalse();
    }

    @Test
    @DisplayName("isTokenValid returns false for a token signed with a different secret")
    void isTokenValid_returnsFalseForDifferentSecret() {
        final JwtProperties otherProps = new JwtProperties();
        otherProps.setSecret("another-secret-that-is-long-enough-for-hmac-sha256-00000000");
        otherProps.setExpirationMs(3_600_000L);
        otherProps.setIssuer("project-echo-test");
        final JwtService otherService = new JwtService(otherProps);

        final String tokenFromOtherService = otherService.issueToken(UUID.randomUUID(), EMAIL);
        assertThat(jwtService.isTokenValid(tokenFromOtherService)).isFalse();
    }

    @Test
    @DisplayName("isTokenValid returns false for an expired token")
    void isTokenValid_returnsFalseForExpiredToken() {
        final JwtProperties expiredProps = new JwtProperties();
        expiredProps.setSecret(VALID_SECRET);
        expiredProps.setExpirationMs(0L); // immediately expired
        expiredProps.setIssuer("project-echo-test");
        final JwtService expiredService = new JwtService(expiredProps);

        final String expiredToken = expiredService.issueToken(UUID.randomUUID(), EMAIL);
        assertThat(jwtService.isTokenValid(expiredToken)).isFalse();
    }

    @Test
    @DisplayName("isTokenValid returns false for blank input")
    void isTokenValid_returnsFalseForBlankInput() {
        assertThat(jwtService.isTokenValid("")).isFalse();
        assertThat(jwtService.isTokenValid("not.a.jwt")).isFalse();
    }

    @Test
    @DisplayName("constructor throws IllegalArgumentException when secret is null")
    void constructor_throwsWhenSecretIsNull() {
        final JwtProperties props = new JwtProperties();
        props.setSecret(null);
        assertThatThrownBy(() -> new JwtService(props))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("echo.security.jwt.secret");
    }

    @Test
    @DisplayName("constructor throws IllegalArgumentException when secret is blank")
    void constructor_throwsWhenSecretIsBlank() {
        final JwtProperties props = new JwtProperties();
        props.setSecret("   ");
        assertThatThrownBy(() -> new JwtService(props))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("echo.security.jwt.secret");
    }

    @Test
    @DisplayName("validateAndExtractClaims throws JwtException for an invalid token")
    void validateAndExtractClaims_throwsForInvalidToken() {
        assertThatThrownBy(() -> jwtService.validateAndExtractClaims("invalid.token.here"))
                .isInstanceOf(JwtException.class);
    }
}
