package com.compassio.profession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfessionController {
    private final ProfessionRepository repository;

    public ProfessionController(ProfessionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(name = "q", required = false) String q,
            @PageableDefault(size = 50, sort = "code") Pageable pageable,
            Model model
    ) {
        Page<Profession> page = (q == null || q.isBlank())
                ? repository.findAll(pageable)
                : repository.findByNameContainingIgnoreCaseOrderByCodeAsc(q.trim(), pageable);

        model.addAttribute("page", page);
        model.addAttribute("q", q == null ? "" : q.trim());
        model.addAttribute("total", page.getTotalElements());
        return "index";
    }
}
