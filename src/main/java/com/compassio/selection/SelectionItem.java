package com.compassio.selection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "selection_item")
public class SelectionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "selection_id", nullable = false)
    private Selection selection;

    @Column(nullable = false, length = 32)
    private String code;

    @Column(nullable = false, length = 512)
    private String name;

    @Column(nullable = false)
    private int level;

    protected SelectionItem() {
    }

    public SelectionItem(String code, String name, int level) {
        this.code = code;
        this.name = name;
        this.level = level;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }
}
