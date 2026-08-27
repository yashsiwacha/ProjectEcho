package com.projectecho.identity.presentation;

import com.projectecho.identity.application.IdentityApplicationService;
import com.projectecho.identity.domain.CareerPassport;
import com.projectecho.identity.domain.EmailAddress;
import com.projectecho.identity.domain.JobTitle;
import com.projectecho.identity.domain.Name;
import com.projectecho.shared.domain.PassportId;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/passports")
public class PassportController {

    private final IdentityApplicationService service;

    public PassportController(final IdentityApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PassportResponse> create(
            @Valid @RequestBody final CreatePassportRequest request,
            final java.security.Principal principal) {
        if (principal != null && !request.email().equals(principal.getName())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Access Denied: Cannot create passport for a different email");
        }
        final PassportId id =
                service.initialize(
                        new Name(request.name()),
                        new EmailAddress(request.email()),
                        new JobTitle(request.jobTitle()));
        final PassportResponse response = PassportResponse.from(service.findById(id.value()));
        return ResponseEntity.created(URI.create("/api/v1/passports/" + id.value())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassportResponse> findById(
            @PathVariable final UUID id, final java.security.Principal principal) {
        final CareerPassport passport = service.findById(id);
        if (principal != null && !passport.getEmail().value().equals(principal.getName())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Access Denied: Passport does not belong to the authenticated user");
        }
        return ResponseEntity.ok(PassportResponse.from(passport));
    }

    @GetMapping
    public ResponseEntity<Page<PassportResponse>> findAll(
            @PageableDefault(size = 20) final Pageable pageable,
            @RequestParam(required = false) final String name,
            final java.security.Principal principal) {
        Page<PassportResponse> page;
        if (principal != null) {
            // For security, only return the passport belonging to the authenticated user
            try {
                final CareerPassport passport =
                        service.findByEmail(new EmailAddress(principal.getName()));
                final java.util.List<PassportResponse> list =
                        java.util.List.of(PassportResponse.from(passport));
                page = new org.springframework.data.domain.PageImpl<>(list, pageable, 1);
            } catch (com.projectecho.shared.exception.ResourceNotFoundException ex) {
                page = Page.empty(pageable);
            }
        } else {
            page = Page.empty(pageable);
        }
        return ResponseEntity.ok(page);
    }
}
