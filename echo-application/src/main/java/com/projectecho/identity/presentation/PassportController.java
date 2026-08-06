package com.projectecho.identity.presentation;

import com.projectecho.identity.application.IdentityApplicationService;
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
            @Valid @RequestBody final CreatePassportRequest request) {
        final PassportId id =
                service.initialize(
                        new Name(request.name()),
                        new EmailAddress(request.email()),
                        new JobTitle(request.jobTitle()));
        final PassportResponse response = PassportResponse.from(service.findById(id.value()));
        return ResponseEntity.created(URI.create("/api/v1/passports/" + id.value())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassportResponse> findById(@PathVariable final UUID id) {
        return ResponseEntity.ok(PassportResponse.from(service.findById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<PassportResponse>> findAll(
            @PageableDefault(size = 20) final Pageable pageable,
            @RequestParam(required = false) final String name) {
        final Page<PassportResponse> page;
        if (name != null && !name.isBlank()) {
            page = service.searchByName(name, pageable).map(PassportResponse::from);
        } else {
            page = service.findAll(pageable).map(PassportResponse::from);
        }
        return ResponseEntity.ok(page);
    }
}
