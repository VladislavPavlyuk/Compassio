package com.compassio.profession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionRepository extends JpaRepository<Profession, Long> {
    Page<Profession> findByNameContainingIgnoreCaseOrderByCodeAsc(String name, Pageable pageable);
}
