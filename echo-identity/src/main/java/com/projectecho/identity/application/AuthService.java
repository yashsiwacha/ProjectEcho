package com.projectecho.identity.application;

import com.projectecho.identity.domain.EmailAddress;
import com.projectecho.identity.domain.User;
import com.projectecho.identity.domain.UserRepository;
import com.projectecho.identity.exception.IdentityException;
import com.projectecho.shared.exception.ResourceNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Orchestrator service managing User registrations, logins, JWT refreshes, and multi-factor
 * validation (MFA) routines (FD-0018).
 */
@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MfaService mfaService;

    public AuthService(
            final UserRepository userRepository,
            final PasswordEncoder passwordEncoder,
            final TokenProvider tokenProvider,
            final MfaService mfaService) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder);
        this.tokenProvider = Objects.requireNonNull(tokenProvider);
        this.mfaService = Objects.requireNonNull(mfaService);
    }

    public User register(final EmailAddress email, final String password, final String role) {
        userRepository
                .findByEmail(email)
                .ifPresent(
                        existing -> {
                            throw new IdentityException(
                                    "Email is already registered: " + email.value());
                        });

        final UUID id = UUID.randomUUID();
        final String hash = passwordEncoder.encode(password);

        final User user = new User(id, email, hash, role);
        user.setEmailVerificationToken(UUID.randomUUID().toString());
        // Generate initial refresh token
        rotateRefreshToken(user);

        final User saved = userRepository.save(user);

        // Mock email sending
        System.out.println(
                "MOCK EMAIL: Verification link sent to "
                        + email.value()
                        + " with token: "
                        + saved.getEmailVerificationToken());

        return saved;
    }

    public AuthResponse authenticate(final EmailAddress email, final String password) {
        final User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("User credentials not found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IdentityException("Authentication failed: Invalid credentials");
        }

        if (Boolean.TRUE.equals(user.getMfaEnabled())) {
            return new AuthResponse(null, null, true, user.getEmail().value());
        }

        rotateRefreshToken(user);
        userRepository.save(user);

        final String token =
                tokenProvider.issueToken(user.getId(), user.getEmail().value(), user.getRole());
        return new AuthResponse(token, user.getRefreshToken(), false, user.getEmail().value());
    }

    public String setupMfa(final EmailAddress email) {
        final User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        final String secret = mfaService.generateSecretKey();
        user.setMfaSecret(secret);
        user.setMfaEnabled(true);
        userRepository.save(user);

        return secret;
    }

    public AuthResponse verifyMfa(final EmailAddress email, final int code) {
        final User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!mfaService.verifyCode(user.getMfaSecret(), code)) {
            throw new IdentityException("MFA verification failed: Invalid TOTP token");
        }

        rotateRefreshToken(user);
        userRepository.save(user);

        final String token =
                tokenProvider.issueToken(user.getId(), user.getEmail().value(), user.getRole());
        return new AuthResponse(token, user.getRefreshToken(), false, user.getEmail().value());
    }

    public AuthResponse rotateToken(final String refreshToken) {
        final User user =
                userRepository
                        .findByRefreshToken(refreshToken)
                        .orElseThrow(() -> new IdentityException("Invalid refresh token"));

        if (user.getTokenExpiry().isBefore(Instant.now())) {
            throw new IdentityException("Refresh token has expired");
        }

        rotateRefreshToken(user);
        userRepository.save(user);

        final String token =
                tokenProvider.issueToken(user.getId(), user.getEmail().value(), user.getRole());
        return new AuthResponse(token, user.getRefreshToken(), false, user.getEmail().value());
    }

    public void logout(final String refreshToken) {
        userRepository
                .findByRefreshToken(refreshToken)
                .ifPresent(
                        user -> {
                            user.setRefreshToken(null, null);
                            userRepository.save(user);
                        });
    }

    public void requestPasswordReset(final EmailAddress email) {
        userRepository
                .findByEmail(email)
                .ifPresent(
                        user -> {
                            final String token = UUID.randomUUID().toString();
                            user.setResetToken(token, Instant.now().plus(1, ChronoUnit.HOURS));
                            userRepository.save(user);
                            System.out.println(
                                    "MOCK EMAIL: Password reset link sent to "
                                            + email.value()
                                            + " with token: "
                                            + token);
                        });
    }

    public void resetPassword(final String token, final String newPassword) {
        final User user =
                userRepository
                        .findByResetToken(token)
                        .orElseThrow(() -> new IdentityException("Invalid or expired reset token"));

        if (user.getResetTokenExpiry() == null
                || user.getResetTokenExpiry().isBefore(Instant.now())) {
            throw new IdentityException("Reset token has expired");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setResetToken(null, null);
        userRepository.save(user);
    }

    public void verifyEmail(final String token) {
        final User user =
                userRepository
                        .findByEmailVerificationToken(token)
                        .orElseThrow(() -> new IdentityException("Invalid verification token"));

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        userRepository.save(user);
    }

    private void rotateRefreshToken(final User user) {
        final String newRefreshToken = UUID.randomUUID().toString();
        // Refresh token valid for 7 days
        final Instant expiry = Instant.now().plus(7, ChronoUnit.DAYS);
        user.setRefreshToken(newRefreshToken, expiry);
    }

    /** Authentication result payload. */
    public record AuthResponse(
            String accessToken, String refreshToken, boolean mfaRequired, String email) {}
}
