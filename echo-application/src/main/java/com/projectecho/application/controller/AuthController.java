package com.projectecho.application.controller;

import com.projectecho.application.security.JwtTokenProvider;
import com.projectecho.identity.domain.UserAccount;
import com.projectecho.identity.domain.UserRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            final JwtTokenProvider tokenProvider,
            final UserRepository userRepository,
            final PasswordEncoder passwordEncoder) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final Map<String, String> credentials) {
        final String email =
                credentials.containsKey("email")
                        ? credentials.get("email")
                        : credentials.get("username");
        final String password = credentials.get("password");

        final ResponseEntity<?> response;
        final Optional<UserAccount> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()
                && passwordEncoder.matches(password, userOpt.get().getPasswordHash())) {
            final UserAccount user = userOpt.get();
            final String token = tokenProvider.generateToken(user.getEmail());

            final org.springframework.http.ResponseCookie jwtCookie =
                    org.springframework.http.ResponseCookie.from("echo_jwt", token)
                            .httpOnly(true)
                            .secure(false) // local dev, set true for prod
                            .path("/")
                            .maxAge(86_400)
                            .build();

            response =
                    ResponseEntity.ok()
                            .header(
                                    org.springframework.http.HttpHeaders.SET_COOKIE,
                                    jwtCookie.toString())
                            .body(
                                    Map.of(
                                            "userId",
                                            user.getId().toString(),
                                            "name",
                                            user.getName()));
        } else {
            response =
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("error", "Invalid credentials"));
        }
        return response;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody final Map<String, String> request) {
        final String email = request.get("email");
        final String password = request.get("password");
        final String name = request.get("name");

        final ResponseEntity<?> response;
        if (email == null || password == null || name == null) {
            response = ResponseEntity.badRequest().body(Map.of("error", "Missing fields"));
        } else if (userRepository.findByEmail(email).isPresent()) {
            response = ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        } else {
            final UserAccount newUser =
                    new UserAccount(
                            UUID.randomUUID(), email, passwordEncoder.encode(password), name);
            userRepository.save(newUser);

            final String token = tokenProvider.generateToken(newUser.getEmail());
            final org.springframework.http.ResponseCookie jwtCookie =
                    org.springframework.http.ResponseCookie.from("echo_jwt", token)
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(86_400)
                            .build();

            response =
                    ResponseEntity.ok()
                            .header(
                                    org.springframework.http.HttpHeaders.SET_COOKIE,
                                    jwtCookie.toString())
                            .body(
                                    Map.of(
                                            "userId",
                                            newUser.getId().toString(),
                                            "name",
                                            newUser.getName()));
        }
        return response;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        final org.springframework.http.ResponseCookie clearCookie =
                org.springframework.http.ResponseCookie.from("echo_jwt", "")
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(0)
                        .build();

        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.SET_COOKIE, clearCookie.toString())
                .body(Map.of("message", "Logged out successfully"));
    }
}
