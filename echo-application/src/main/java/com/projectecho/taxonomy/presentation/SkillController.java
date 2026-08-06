package com.projectecho.taxonomy.presentation;

import com.projectecho.shared.domain.SkillId;
import com.projectecho.taxonomy.application.TaxonomyApplicationService;
import com.projectecho.taxonomy.domain.SkillCategory;
import com.projectecho.taxonomy.domain.SkillName;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;
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
@RequestMapping("/api/v1/skills")
public class SkillController {

    private final TaxonomyApplicationService service;

    public SkillController(final TaxonomyApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SkillResponse> create(
            @Valid @RequestBody final CreateSkillRequest request) {
        final Optional<SkillId> parentId =
                request.parentSkillId() != null
                        ? Optional.of(new SkillId(request.parentSkillId()))
                        : Optional.empty();
        final SkillId id =
                service.register(
                        new SkillName(request.name()),
                        new SkillCategory(request.category()),
                        parentId);
        final SkillResponse response = SkillResponse.from(service.findById(id.value()));
        return ResponseEntity.created(URI.create("/api/v1/skills/" + id.value())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillResponse> findById(@PathVariable final UUID id) {
        return ResponseEntity.ok(SkillResponse.from(service.findById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<SkillResponse>> findAll(
            @PageableDefault(size = 20) final Pageable pageable,
            @RequestParam(required = false) final String name,
            @RequestParam(required = false) final String category) {
        final Page<SkillResponse> page;
        if (name != null && !name.isBlank()) {
            page = service.searchByName(name, pageable).map(SkillResponse::from);
        } else if (category != null && !category.isBlank()) {
            page =
                    service.findByCategory(new SkillCategory(category), pageable)
                            .map(SkillResponse::from);
        } else {
            page = service.findAll(pageable).map(SkillResponse::from);
        }
        return ResponseEntity.ok(page);
    }
}
