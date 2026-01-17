package com.compassio.selection;

import java.util.List;
import java.util.Optional;

import com.compassio.auth.UserAccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectionRepository extends JpaRepository<Selection, Long> {
    List<Selection> findByUserAndStatus(UserAccount user, SelectionStatus status);
    Optional<Selection> findTopByUserOrderByCreatedAtDesc(UserAccount user);
}
