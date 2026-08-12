package com.projectecho.identity.application;

import com.projectecho.identity.domain.Organization;
import com.projectecho.identity.domain.OrganizationRepository;
import com.projectecho.identity.domain.Team;
import com.projectecho.identity.domain.TeamRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final TeamRepository teamRepository;

    public OrganizationService(
            OrganizationRepository organizationRepository, TeamRepository teamRepository) {
        this.organizationRepository = organizationRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public Organization createOrganization(String name, String billingPlan) {
        if (organizationRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Organization already exists with name: " + name);
        }
        Organization org = new Organization(UUID.randomUUID(), name, billingPlan);
        return organizationRepository.save(org);
    }

    @Transactional
    public Team createTeam(UUID organizationId, String name) {
        Organization org =
                organizationRepository
                        .findById(organizationId)
                        .orElseThrow(() -> new IllegalArgumentException("Organization not found"));
        Team team = new Team(UUID.randomUUID(), org.getId(), name);
        return teamRepository.save(team);
    }
}
