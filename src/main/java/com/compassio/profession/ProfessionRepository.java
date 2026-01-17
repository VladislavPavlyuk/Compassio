package com.compassio.profession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfessionRepository extends JpaRepository<Profession, Long> {
    @Query("""
            select p from Profession p
            where lower(p.name) like lower(concat('%', :q, '%'))
               or lower(p.code) like lower(concat('%', :q, '%'))
               or (:digits <> '' and regexp_replace(p.name, '\\D', '', 'g') like concat('%', :digits, '%'))
            order by p.code asc
            """)
    Page<Profession> search(@Param("q") String q, @Param("digits") String digits, Pageable pageable);
}
