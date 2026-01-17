package com.compassio.selection;

import java.time.Instant;
import java.util.List;

public class SelectionViewResponse {
    private final Long id;
    private final Instant createdAt;
    private final List<SelectionViewItem> items;

    public SelectionViewResponse(Long id, Instant createdAt, List<SelectionViewItem> items) {
        this.id = id;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<SelectionViewItem> getItems() {
        return items;
    }
}
