package com.projectecho.identity.presentation;

import com.projectecho.identity.application.AuthService;
import com.projectecho.identity.domain.EmailAddress;
import com.projectecho.identity.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposing endpoints for registration, login, and multi-factor validation (MFA)
 * (FD-0018).
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = Objects.requireNonNull(authService);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody final RegisterRequest request) {
        final User user =
                authService.register(
                        new EmailAddress(request.email()), request.password(), request.role());
        return ResponseEntity.ok(
                new UserResponse(user.getId().toString(), user.getEmail().value(), user.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody final LoginRequest request,
            final HttpServletRequest servletRequest,
            final HttpServletResponse servletResponse) {
        final AuthService.AuthResponse response =
                authService.authenticate(new EmailAddress(request.email()), request.password());
        if (!response.mfaRequired()) {
            setCookies(
                    servletRequest,
                    servletResponse,
                    response.accessToken(),
                    response.refreshToken());
        }
        return ResponseEntity.ok(
                new AuthResponseDto(
                        response.accessToken(),
                        response.refreshToken(),
                        response.mfaRequired(),
                        response.email()));
    }

    @PostMapping("/mfa/setup")
    public ResponseEntity<MfaSetupResponse> setupMfa(
            @Valid @RequestBody final MfaSetupRequest request) {
        final String secret = authService.setupMfa(new EmailAddress(request.email()));
        return ResponseEntity.ok(new MfaSetupResponse(secret));
    }

    @PostMapping("/mfa/verify")
    public ResponseEntity<AuthResponseDto> verifyMfa(
            @Valid @RequestBody final MfaVerifyRequest request,
            final HttpServletRequest servletRequest,
            final HttpServletResponse servletResponse) {
        final AuthService.AuthResponse response =
                authService.verifyMfa(new EmailAddress(request.email()), request.code());
        setCookies(
                servletRequest, servletResponse, response.accessToken(), response.refreshToken());
        return ResponseEntity.ok(
                new AuthResponseDto(
                        response.accessToken(),
                        response.refreshToken(),
                        response.mfaRequired(),
                        response.email()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(
            @Valid @RequestBody final TokenRefreshRequest request,
            final HttpServletRequest servletRequest,
            final HttpServletResponse servletResponse) {
        final AuthService.AuthResponse response = authService.rotateToken(request.refreshToken());
        setCookies(
                servletRequest, servletResponse, response.accessToken(), response.refreshToken());
        return ResponseEntity.ok(
                new AuthResponseDto(
                        response.accessToken(),
                        response.refreshToken(),
                        response.mfaRequired(),
                        response.email()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) {
        String refreshToken = null;
        if (servletRequest.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : servletRequest.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken != null) {
            authService.logout(refreshToken);
        }

        // Clear cookies
        setCookies(servletRequest, servletResponse, null, null);
        // Specifically expire them
        if (servletResponse.getHeader("Set-Cookie") == null) {
            final jakarta.servlet.http.Cookie accessCookie =
                    new jakarta.servlet.http.Cookie("accessToken", "");
            accessCookie.setMaxAge(0);
            accessCookie.setPath("/");
            servletResponse.addCookie(accessCookie);

            final jakarta.servlet.http.Cookie refreshCookie =
                    new jakarta.servlet.http.Cookie("refreshToken", "");
            refreshCookie.setMaxAge(0);
            refreshCookie.setPath("/");
            servletResponse.addCookie(refreshCookie);
        }

        return ResponseEntity.noContent().build();
    }

    private void setCookies(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final String accessToken,
            final String refreshToken) {
        if (accessToken != null) {
            final jakarta.servlet.http.Cookie cookie =
                    new jakarta.servlet.http.Cookie("accessToken", accessToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(request.isSecure());
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 1 day
            response.addCookie(cookie);
        }
        if (refreshToken != null) {
            final jakarta.servlet.http.Cookie cookie =
                    new jakarta.servlet.http.Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(request.isSecure());
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            response.addCookie(cookie);
        }
    }

    // DTO records
    public record RegisterRequest(
            @NotBlank @Email String email, @NotBlank String password, @NotBlank String role) {}

    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}

    public record MfaSetupRequest(@NotBlank @Email String email) {}

    public record MfaVerifyRequest(@NotBlank @Email String email, @NotNull Integer code) {}

    public record TokenRefreshRequest(@NotBlank String refreshToken) {}

    public record UserResponse(String id, String email, String role) {}

    public record AuthResponseDto(
            String accessToken, String refreshToken, boolean mfaRequired, String email) {}

    public record MfaSetupResponse(String secret) {}
}
