package com.compassio.profession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfessionApiController {
    private final ProfessionRepository repository;

    public ProfessionApiController(ProfessionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/professions")
    public Page<Profession> list(
            @RequestParam(name = "q", required = false) String q,
            @PageableDefault(size = 50, sort = "code") Pageable pageable
    ) {
        if (q == null || q.isBlank()) {
            return repository.findAll(pageable);
        }
        return repository.findByNameContainingIgnoreCaseOrderByCodeAsc(q.trim(), pageable);
    }
}
