package com.compassio.selection;

import java.util.List;

public class SelectionAuthRequest {
    private String email;
    private String password;
    private String mode;
    private List<SelectionItemDto> items;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<SelectionItemDto> getItems() {
        return items;
    }

    public void setItems(List<SelectionItemDto> items) {
        this.items = items;
    }
}
