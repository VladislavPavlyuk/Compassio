package com.compassio.selection;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.compassio.auth.UserAccount;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "selection")
public class Selection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private SelectionStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "selection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelectionItem> items = new ArrayList<>();

    protected Selection() {
    }

    public Selection(UserAccount user, SelectionStatus status) {
        this.user = user;
        this.status = status;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public UserAccount getUser() {
        return user;
    }

    public SelectionStatus getStatus() {
        return status;
    }

    public void setStatus(SelectionStatus status) {
        this.status = status;
    }

    public List<SelectionItem> getItems() {
        return items;
    }

    public void addItem(SelectionItem item) {
        items.add(item);
        item.setSelection(this);
    }
}
