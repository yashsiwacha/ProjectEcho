package com.projectecho.identity.application;

import com.projectecho.identity.domain.CareerPassport;
import com.projectecho.identity.domain.CareerPassportRepository;
import com.projectecho.identity.domain.EmailAddress;
import com.projectecho.identity.domain.JobTitle;
import com.projectecho.identity.domain.Name;
import com.projectecho.identity.exception.DuplicatePassportException;
import com.projectecho.shared.domain.PassportId;
import com.projectecho.shared.events.CareerPassportInitializedEvent;
import com.projectecho.shared.events.DomainEventPublisher;
import com.projectecho.shared.events.IntegrationEvent;
import com.projectecho.shared.exception.ResourceNotFoundException;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IdentityApplicationService implements InitializeCareerPassportUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityApplicationService.class);
    private final CareerPassportRepository repository;
    private final DomainEventPublisher eventPublisher;

    public IdentityApplicationService(
            final CareerPassportRepository repository, final DomainEventPublisher eventPublisher) {
        this.repository = Objects.requireNonNull(repository);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public PassportId initialize(
            final Name name, final EmailAddress email, final JobTitle jobTitle) {
        repository
                .findByEmail(email)
                .ifPresent(
                        existing -> {
                            throw new DuplicatePassportException(email.value());
                        });

        final UUID id = UUID.randomUUID();
        final CareerPassport passport = new CareerPassport(id, name, email, jobTitle);
        repository.save(passport);

        if (LOG.isInfoEnabled()) {
            LOG.info("Career Passport initialized: {}", id);
        }

        final IntegrationEvent event =
                new CareerPassportInitializedEvent(
                        UUID.randomUUID(),
                        1,
                        id,
                        id,
                        Instant.now(),
                        id,
                        name.value(),
                        email.value(),
                        jobTitle.value());
        eventPublisher.publish(event);

        return new PassportId(id);
    }

    @Transactional(readOnly = true)
    public CareerPassport findById(final UUID passportId) {
        return repository
                .findById(passportId)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Career Passport not found: " + passportId));
    }

    @Transactional(readOnly = true)
    public CareerPassport findByEmail(final EmailAddress email) {
        return repository
                .findByEmail(email)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Career Passport not found for email: " + email.value()));
    }

    @Transactional(readOnly = true)
    public Page<CareerPassport> findAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<CareerPassport> searchByName(final String name, final Pageable pageable) {
        return repository.searchByName(name, pageable);
    }
}
