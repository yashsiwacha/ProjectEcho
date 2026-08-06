package com.projectecho.identity.infrastructure;

import com.projectecho.identity.domain.CareerPassport;
import com.projectecho.identity.domain.EmailAddress;
import java.util.Optional;
import java.util.UUID;

public interface CareerPassportRepository {
    void save(CareerPassport passport);

    Optional<CareerPassport> findById(UUID id);

    Optional<CareerPassport> findByEmail(EmailAddress email);
}
