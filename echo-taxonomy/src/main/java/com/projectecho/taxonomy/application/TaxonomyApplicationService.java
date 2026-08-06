package com.projectecho.taxonomy.application;

import com.projectecho.shared.domain.SkillId;
import com.projectecho.shared.events.DomainEventPublisher;
import com.projectecho.shared.events.SkillRegisteredEvent;
import com.projectecho.shared.exception.ResourceNotFoundException;
import com.projectecho.taxonomy.domain.Skill;
import com.projectecho.taxonomy.domain.SkillCategory;
import com.projectecho.taxonomy.domain.SkillName;
import com.projectecho.taxonomy.domain.SkillRepository;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaxonomyApplicationService implements RegisterSkillUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(TaxonomyApplicationService.class);
    private final SkillRepository repository;
    private final DomainEventPublisher eventPublisher;

    public TaxonomyApplicationService(
            final SkillRepository repository, final DomainEventPublisher eventPublisher) {
        this.repository = Objects.requireNonNull(repository);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public SkillId register(
            final SkillName name,
            final SkillCategory category,
            final Optional<SkillId> parentSkillId) {

        if (repository.existsByName(name)) {
            throw new IllegalArgumentException("Skill already exists: " + name.value());
        }

        final UUID id = UUID.randomUUID();
        final Skill skill = new Skill(id, name, category, parentSkillId.orElse(null));
        repository.save(skill);

        if (LOG.isInfoEnabled()) {
            LOG.info("Skill registered: {} ({})", name.value(), id);
        }

        eventPublisher.publish(
                new SkillRegisteredEvent(
                        UUID.randomUUID(),
                        1,
                        id,
                        id,
                        Instant.now(),
                        id,
                        name.value(),
                        category.value(),
                        parentSkillId.map(SkillId::value).orElse(null)));

        return new SkillId(id);
    }

    @Transactional(readOnly = true)
    public Skill findById(final UUID skillId) {
        return repository
                .findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + skillId));
    }

    @Transactional(readOnly = true)
    public Page<Skill> findAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Skill> searchByName(final String name, final Pageable pageable) {
        return repository.searchByName(name, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Skill> findByCategory(final SkillCategory category, final Pageable pageable) {
        return repository.findByCategory(category, pageable);
    }
}
