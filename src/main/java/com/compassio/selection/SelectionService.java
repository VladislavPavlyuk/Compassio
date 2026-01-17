package com.compassio.selection;

import java.util.List;
import java.util.Optional;

import com.compassio.auth.UserAccount;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class SelectionService {
    private final SelectionRepository selectionRepository;

    public SelectionService(SelectionRepository selectionRepository) {
        this.selectionRepository = selectionRepository;
    }

    public Selection saveSelection(UserAccount user, SelectionStatus status, List<SelectionItem> items) {
        Selection selection = new Selection(user, status);
        for (SelectionItem item : items) {
            selection.addItem(item);
        }
        return selectionRepository.save(selection);
    }

    public List<Selection> findPending(UserAccount user) {
        return selectionRepository.findByUserAndStatus(user, SelectionStatus.PENDING);
    }

    public List<Selection> saveAll(@NonNull List<Selection> selections) {
        return selectionRepository.saveAll(selections);
    }

    public Optional<Selection> findLatest(UserAccount user) {
        return selectionRepository.findTopByUserOrderByCreatedAtDesc(user);
    }
}
