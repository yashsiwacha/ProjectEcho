package com.projectecho.identity.domain;

import com.projectecho.shared.domain.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * User aggregate root representing system registration credentials, security roles, and
 * Multi-Factor Authentication (MFA) parameters (SEC-02, AUTH-01).
 */
@Entity
@Table(name = "identity_users")
public class User extends AggregateRoot {

    @Column(nullable = false, unique = true)
    private EmailAddress email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role;

    @Column(name = "organization_id")
    private UUID organizationId;

    @Column(name = "team_id")
    private UUID teamId;

    @Column(nullable = false)
    private Boolean mfaEnabled;

    @Column private String mfaSecret;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "token_expiry")
    private Instant tokenExpiry;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private Instant resetTokenExpiry;

    protected User() {
        super();
        // JPA
    }

    public User(
            final UUID id, final EmailAddress email, final String passwordHash, final String role) {
        super(id);
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash cannot be null");
        this.role = Objects.requireNonNull(role, "Role cannot be null");
        this.mfaEnabled = false;
        this.emailVerified = false;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
        markUpdated();
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
        markUpdated();
    }

    public EmailAddress getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(final String passwordHash) {
        this.passwordHash = Objects.requireNonNull(passwordHash);
        markUpdated();
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = Objects.requireNonNull(role);
        markUpdated();
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(final Boolean mfaEnabled) {
        this.mfaEnabled = Objects.requireNonNull(mfaEnabled);
        markUpdated();
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public void setMfaSecret(final String mfaSecret) {
        this.mfaSecret = mfaSecret;
        markUpdated();
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken, final Instant expiry) {
        this.refreshToken = refreshToken;
        this.tokenExpiry = expiry;
        markUpdated();
    }

    public Instant getTokenExpiry() {
        return tokenExpiry;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(final Boolean emailVerified) {
        this.emailVerified = emailVerified;
        markUpdated();
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(final String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
        markUpdated();
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(final String resetToken, final Instant expiry) {
        this.resetToken = resetToken;
        this.resetTokenExpiry = expiry;
        markUpdated();
    }

    public Instant getResetTokenExpiry() {
        return resetTokenExpiry;
    }
}
