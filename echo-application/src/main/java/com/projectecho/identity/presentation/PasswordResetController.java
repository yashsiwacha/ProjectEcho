package com.projectecho.identity.presentation;

import com.projectecho.identity.application.AuthService;
import com.projectecho.identity.domain.EmailAddress;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller exposing endpoints for password reset flows (FD-0021). */
@RestController
@RequestMapping("/api/v1/auth/password")
public class PasswordResetController {

    private final AuthService authService;

    public PasswordResetController(final AuthService authService) {
        this.authService = Objects.requireNonNull(authService);
    }

    @PostMapping("/forgot")
    public ResponseEntity<Void> forgotPassword(
            @Valid @RequestBody final ForgotPasswordRequest request) {
        authService.requestPasswordReset(new EmailAddress(request.email()));
        // Always return 200/204 to prevent email enumeration attacks
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody final ResetPasswordRequest request) {
        authService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.noContent().build();
    }

    public record ForgotPasswordRequest(@NotBlank @Email String email) {}

    public record ResetPasswordRequest(@NotBlank String token, @NotBlank String newPassword) {}
}
