package com.projectecho.identity.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.projectecho.identity.domain.EmailAddress;
import com.projectecho.identity.domain.User;
import com.projectecho.identity.domain.UserRepository;
import com.projectecho.identity.exception.IdentityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("AuthService Unit Tests (Standard Fakes)")
class AuthServiceTest {

    private FakeUserRepository userRepository;
    private FakePasswordEncoder passwordEncoder;
    private FakeTokenProvider tokenProvider;
    private FakeMfaService mfaService;
    private AuthService authService;

    private static final EmailAddress EMAIL = new EmailAddress("candidate@test.com");
    private static final String PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encodedPassword123";
    private static final String ROLE = "ROLE_CANDIDATE";

    @BeforeEach
    void setUp() {
        userRepository = new FakeUserRepository();
        passwordEncoder = new FakePasswordEncoder();
        tokenProvider = new FakeTokenProvider();
        mfaService = new FakeMfaService();
        authService = new AuthService(userRepository, passwordEncoder, tokenProvider, mfaService);
    }

    @Test
    @DisplayName("register successfully saves user with hashed password")
    void register_savesHashedPassword() {
        passwordEncoder.configuredHash = ENCODED_PASSWORD;

        final User saved = authService.register(EMAIL, PASSWORD, ROLE);

        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(EMAIL);
        assertThat(saved.getPasswordHash()).isEqualTo(ENCODED_PASSWORD);
        assertThat(saved.getRole()).isEqualTo(ROLE);

        final Optional<User> fetched = userRepository.findByEmail(EMAIL);
        assertThat(fetched).isPresent();
    }

    @Test
    @DisplayName("register throws exception if email is already registered")
    void register_throwsIfEmailExists() {
        final User user = new User(UUID.randomUUID(), EMAIL, ENCODED_PASSWORD, ROLE);
        userRepository.save(user);

        assertThatThrownBy(() -> authService.register(EMAIL, PASSWORD, ROLE))
                .isInstanceOf(IdentityException.class)
                .hasMessageContaining("already registered");
    }

    @Test
    @DisplayName("authenticate issues JWT when MFA is disabled")
    void authenticate_issuesJwt() {
        final User user = new User(UUID.randomUUID(), EMAIL, ENCODED_PASSWORD, ROLE);
        userRepository.save(user);
        passwordEncoder.matchesExpected = true;
        tokenProvider.issuedTokenToReturn = "jwt-token";

        final AuthService.AuthResponse response = authService.authenticate(EMAIL, PASSWORD);

        assertThat(response.mfaRequired()).isFalse();
        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.refreshToken()).isEqualTo(user.getRefreshToken());
    }

    @Test
    @DisplayName("authenticate returns mfaRequired flag when MFA is enabled")
    void authenticate_returnsMfaRequired() {
        final User user = new User(UUID.randomUUID(), EMAIL, ENCODED_PASSWORD, ROLE);
        user.setMfaEnabled(true);
        user.setMfaSecret("SECRET32KEY");
        userRepository.save(user);
        passwordEncoder.matchesExpected = true;

        final AuthService.AuthResponse response = authService.authenticate(EMAIL, PASSWORD);

        assertThat(response.mfaRequired()).isTrue();
        assertThat(response.accessToken()).isNull();
    }

    @Test
    @DisplayName("setupMfa generates secret key and enables MFA")
    void setupMfa_enablesMfaAndGeneratesSecret() {
        final User user = new User(UUID.randomUUID(), EMAIL, ENCODED_PASSWORD, ROLE);
        userRepository.save(user);
        mfaService.secretToReturn = "MFASECRETKEY123";

        final String secret = authService.setupMfa(EMAIL);

        assertThat(secret).isEqualTo("MFASECRETKEY123");
        assertThat(user.getMfaEnabled()).isTrue();
        assertThat(user.getMfaSecret()).isEqualTo("MFASECRETKEY123");
    }

    @Test
    @DisplayName("verifyMfa returns token for correct TOTP code")
    void verifyMfa_returnsTokenForCorrectCode() {
        final User user = new User(UUID.randomUUID(), EMAIL, ENCODED_PASSWORD, ROLE);
        user.setMfaEnabled(true);
        user.setMfaSecret("MFASECRETKEY123");
        userRepository.save(user);
        mfaService.codeVerificationResult = true;
        tokenProvider.issuedTokenToReturn = "jwt-token";

        final AuthService.AuthResponse response = authService.verifyMfa(EMAIL, 123456);

        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.mfaRequired()).isFalse();
    }

    @Test
    @DisplayName("verifyMfa throws exception for invalid TOTP code")
    void verifyMfa_throwsForInvalidCode() {
        final User user = new User(UUID.randomUUID(), EMAIL, ENCODED_PASSWORD, ROLE);
        user.setMfaEnabled(true);
        user.setMfaSecret("MFASECRETKEY123");
        userRepository.save(user);
        mfaService.codeVerificationResult = false;

        assertThatThrownBy(() -> authService.verifyMfa(EMAIL, 999999))
                .isInstanceOf(IdentityException.class)
                .hasMessageContaining("MFA verification failed");
    }

    @Test
    @DisplayName("rotateToken issues new JWT for valid refresh token")
    void rotateToken_issuesNewTokens() {
        final User user = new User(UUID.randomUUID(), EMAIL, ENCODED_PASSWORD, ROLE);
        user.setRefreshToken("old-refresh-token", Instant.now().plus(1, ChronoUnit.HOURS));
        userRepository.save(user);
        tokenProvider.issuedTokenToReturn = "new-jwt-token";

        final AuthService.AuthResponse response = authService.rotateToken("old-refresh-token");

        assertThat(response.accessToken()).isEqualTo("new-jwt-token");
        assertThat(response.refreshToken()).isNotEqualTo("old-refresh-token");
    }

    // Fakes Implementation

    private static class FakeUserRepository implements UserRepository {
        private final Map<UUID, User> users = new HashMap<>();

        @Override
        public Optional<User> findByEmail(EmailAddress email) {
            return users.values().stream()
                    .filter(u -> u.getEmail().value().equalsIgnoreCase(email.value()))
                    .findFirst();
        }

        @Override
        public Optional<User> findByRefreshToken(String token) {
            return users.values().stream()
                    .filter(u -> token.equals(u.getRefreshToken()))
                    .findFirst();
        }

        @Override
        public Optional<User> findByEmailVerificationToken(String token) {
            return users.values().stream()
                    .filter(u -> token.equals(u.getEmailVerificationToken()))
                    .findFirst();
        }

        @Override
        public Optional<User> findByResetToken(String token) {
            return users.values().stream().filter(u -> token.equals(u.getResetToken())).findFirst();
        }

        @Override
        public <S extends User> S save(S entity) {
            users.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public Optional<User> findById(UUID id) {
            return Optional.ofNullable(users.get(id));
        }

        @Override
        public boolean existsById(UUID id) {
            return users.containsKey(id);
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(users.values());
        }

        @Override
        public List<User> findAllById(Iterable<UUID> ids) {
            return null;
        }

        @Override
        public long count() {
            return users.size();
        }

        @Override
        public void deleteById(UUID id) {
            users.remove(id);
        }

        @Override
        public void delete(User entity) {
            users.remove(entity.getId());
        }

        @Override
        public void deleteAllById(Iterable<? extends UUID> ids) {}

        @Override
        public void deleteAll(Iterable<? extends User> entities) {}

        @Override
        public void deleteAll() {
            users.clear();
        }

        @Override
        public void flush() {}

        @Override
        public <S extends User> S saveAndFlush(S entity) {
            return save(entity);
        }

        @Override
        public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<User> entities) {}

        @Override
        public void deleteAllByIdInBatch(Iterable<UUID> ids) {}

        @Override
        public void deleteAllInBatch() {}

        @Override
        public User getOne(UUID id) {
            return users.get(id);
        }

        @Override
        public User getById(UUID id) {
            return users.get(id);
        }

        @Override
        public User getReferenceById(UUID id) {
            return users.get(id);
        }

        @Override
        public <S extends User> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends User> List<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends User> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends User> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends User, R> R findBy(
                Example<S> example,
                java.util.function.Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public List<User> findAll(Sort sort) {
            return null;
        }

        @Override
        public Page<User> findAll(Pageable pageable) {
            return null;
        }

        @Override
        public <S extends User> List<S> saveAll(Iterable<S> entities) {
            return null;
        }
    }

    private static class FakePasswordEncoder implements PasswordEncoder {
        String configuredHash = "hashedPassword";
        boolean matchesExpected = false;

        @Override
        public String encode(CharSequence rawPassword) {
            return configuredHash;
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return matchesExpected;
        }
    }

    private static class FakeTokenProvider implements TokenProvider {
        String issuedTokenToReturn = "token";

        @Override
        public String issueToken(UUID userId, String email, String role) {
            return issuedTokenToReturn;
        }

        @Override
        public String extractSubject(String token) {
            return UUID.randomUUID().toString();
        }

        @Override
        public boolean isTokenValid(String token) {
            return true;
        }

        @Override
        public String extractRole(String token) {
            return "ROLE_CANDIDATE";
        }
    }

    private static class FakeMfaService implements MfaService {
        String secretToReturn = "SECRET";
        boolean codeVerificationResult = true;

        @Override
        public String generateSecretKey() {
            return secretToReturn;
        }

        @Override
        public boolean verifyCode(String secret, int code) {
            return codeVerificationResult;
        }
    }
}
