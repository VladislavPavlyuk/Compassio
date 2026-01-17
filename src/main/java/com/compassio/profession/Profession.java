package com.compassio.profession;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "profession",
        indexes = {
                @Index(name = "idx_profession_code", columnList = "code"),
                @Index(name = "idx_profession_level", columnList = "level"),
                @Index(name = "idx_profession_name", columnList = "name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_profession_code", columnNames = "code")
        }
)
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String code;

    @Column(nullable = false, length = 512)
    private String name;

    @Column(nullable = false)
    private int level;

    protected Profession() {
    }

    public Profession(String code, String name, int level) {
        this.code = code;
        this.name = name;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
