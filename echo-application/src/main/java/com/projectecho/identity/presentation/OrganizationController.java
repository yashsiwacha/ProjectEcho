package com.projectecho.identity.presentation;

import com.projectecho.identity.application.OrganizationService;
import com.projectecho.identity.domain.Organization;
import com.projectecho.identity.domain.Team;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public ResponseEntity<Organization> createOrganization(
            @RequestBody CreateOrganizationRequest request) {
        Organization org =
                organizationService.createOrganization(request.name(), request.billingPlan());
        return ResponseEntity.ok(org);
    }

    @PostMapping("/{organizationId}/teams")
    public ResponseEntity<Team> createTeam(
            @PathVariable UUID organizationId, @RequestBody CreateTeamRequest request) {
        Team team = organizationService.createTeam(organizationId, request.name());
        return ResponseEntity.ok(team);
    }

    public record CreateOrganizationRequest(String name, String billingPlan) {}

    public record CreateTeamRequest(String name) {}
}
