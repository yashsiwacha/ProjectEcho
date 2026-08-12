package com.projectecho.identity.application;

/** Port interface for multi-factor authentication verifiers (FD-0018). */
public interface MfaService {
    String generateSecretKey();

    boolean verifyCode(String secret, int code);
}
