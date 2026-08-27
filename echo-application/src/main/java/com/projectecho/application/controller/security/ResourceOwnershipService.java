package com.projectecho.application.controller.security;

import com.projectecho.identity.application.IdentityApplicationService;
import com.projectecho.identity.domain.CareerPassport;
import java.security.Principal;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class ResourceOwnershipService {

    private final IdentityApplicationService identityService;

    public ResourceOwnershipService(final IdentityApplicationService identityService) {
        this.identityService = identityService;
    }

    public void verifyPassportOwnership(final UUID passportId, final Principal principal) {
        if (principal == null) {
            return;
        }
        final CareerPassport passport = identityService.findById(passportId);
        if (!passport.getEmail().value().equals(principal.getName())) {
            throw new AccessDeniedException("Access Denied: Resource belongs to another user");
        }
    }

    public UUID getPassportIdByPrincipal(final Principal principal) {
        UUID id = null;
        if (principal != null) {
            final CareerPassport passport =
                    identityService.findByEmail(
                            new com.projectecho.identity.domain.EmailAddress(principal.getName()));
            id = passport.getId();
        }
        return id;
    }
}
