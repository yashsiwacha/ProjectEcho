package com.projectecho.identity.presentation;

import com.projectecho.identity.application.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller exposing endpoints for email verification (FD-0021). */
@RestController
@RequestMapping("/api/v1/auth/email")
public class EmailVerificationController {

    private final AuthService authService;

    public EmailVerificationController(final AuthService authService) {
        this.authService = Objects.requireNonNull(authService);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody final VerifyEmailRequest request) {
        authService.verifyEmail(request.token());
        return ResponseEntity.noContent().build();
    }

    public record VerifyEmailRequest(@NotBlank String token) {}
}
