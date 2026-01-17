package com.compassio.profession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
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
            @PageableDefault(size = 50, sort = "code") @NonNull Pageable pageable,
            Model model
    ) {
        String query = q == null ? "" : q.trim();
        Page<Profession> page = query.isBlank()
                ? repository.findAll(pageable)
                : repository.search(query, digitsOnly(query), pageable);

        model.addAttribute("page", page);
        model.addAttribute("q", query);
        model.addAttribute("total", page.getTotalElements());
        return "index";
    }

    private String digitsOnly(String value) {
        return value.replaceAll("\\D", "");
    }
}
