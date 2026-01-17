package com.compassio.selection;

import java.util.List;

public class SelectionItemsRequest {
    private List<SelectionItemDto> items;

    public List<SelectionItemDto> getItems() {
        return items;
    }

    public void setItems(List<SelectionItemDto> items) {
        this.items = items;
    }
}
