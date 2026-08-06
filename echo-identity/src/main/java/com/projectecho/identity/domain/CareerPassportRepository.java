package com.projectecho.identity.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerPassportRepository extends JpaRepository<CareerPassport, UUID> {

    @Query("SELECT cp FROM CareerPassport cp WHERE cp.email = :email")
    Optional<CareerPassport> findByEmail(@Param("email") EmailAddress email);

    @Query("SELECT cp FROM CareerPassport cp WHERE cp.name LIKE %:name%")
    Page<CareerPassport> searchByName(@Param("name") String name, Pageable pageable);
}
