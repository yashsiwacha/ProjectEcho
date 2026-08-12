package com.projectecho.identity.application;

import java.util.UUID;

/** Port interface for security token providers (FD-0018). */
public interface TokenProvider {
    String issueToken(UUID userId, String email, String role);

    String extractSubject(String token);

    boolean isTokenValid(String token);

    String extractRole(String token);
}
