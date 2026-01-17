package com.compassio.selection;

public class SelectionViewItem {
    private final String code;
    private final String name;
    private final int level;

    public SelectionViewItem(String code, String name, int level) {
        this.code = code;
        this.name = name;
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}
