package com.projectecho.identity.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtTokenValidatorTest {

  private JwtTokenValidator validator;

  @BeforeEach
  void setUp() {
    // Use a test secret
    validator = new JwtTokenValidator("testSecretKeyThatIsAtLeast32BytesLong12345!");
  }

  @Test
  void shouldExtractSubjectFromValidToken() {
    String token = Jwts.builder().subject("user123").signWith(validator.getKey()).compact();

    String subject = validator.validateAndGetSubject(token);

    assertNotNull(subject);
    assertEquals("user123", subject);
  }

  @Test
  void shouldThrowForInvalidSignature() {
    String token =
        Jwts.builder()
            .subject("user123")
            // Sign with a different key
            .signWith(
                new JwtTokenValidator("differentSecretKeyThatIsAtLeast32BytesLong12345!").getKey())
            .compact();

    assertThrows(JwtException.class, () -> validator.validateAndGetSubject(token));
  }

  @Test
  void shouldThrowForExpiredToken() {
    String token =
        Jwts.builder()
            .subject("user123")
            .expiration(new Date(System.currentTimeMillis() - 10000)) // Expired 10 seconds ago
            .signWith(validator.getKey())
            .compact();

    assertThrows(JwtException.class, () -> validator.validateAndGetSubject(token));
  }
}
