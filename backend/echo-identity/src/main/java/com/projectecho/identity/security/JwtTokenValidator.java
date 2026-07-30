package com.projectecho.identity.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Utility for parsing and validating JSON Web Tokens (JWT). */
@Component
public class JwtTokenValidator {

  private final SecretKey key;

  public JwtTokenValidator(@Value("${echo.security.jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Parses the JWT and extracts the subject (principalId) if valid.
   *
   * @param token the JWT string
   * @return the subject if valid
   * @throws JwtException if token is invalid, expired, or has a bad signature
   */
  public String validateAndGetSubject(String token) throws JwtException {
    Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    return claims.getSubject();
  }

  // Test helper - package private
  SecretKey getKey() {
    return key;
  }
}
