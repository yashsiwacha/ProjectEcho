package com.projectecho.identity.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository interface for managing {@link User} entity persistence (FD-0018). */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") EmailAddress email);

    @Query("SELECT u FROM User u WHERE u.refreshToken = :token")
    Optional<User> findByRefreshToken(@Param("token") String token);

    @Query("SELECT u FROM User u WHERE u.resetToken = :token")
    Optional<User> findByResetToken(@Param("token") String token);

    @Query("SELECT u FROM User u WHERE u.emailVerificationToken = :token")
    Optional<User> findByEmailVerificationToken(@Param("token") String token);
}
