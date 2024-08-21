package com.JavaFest2024.xyz.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type;
    private String content;
    private LocalDate postedDate;
    private LocalDate expiryDate;
    private boolean readOrNot;

// Constructors, getters, and setters

    public Notification() {}

    public Notification(String title, String type, String content, LocalDate postedDate, LocalDate expiryDate, boolean readOrNot) {
        this.title = title;
        this.type = type;
        this.content = content;
        this.postedDate = postedDate;
        this.expiryDate = expiryDate;
        this.readOrNot = readOrNot;
    }

    // Getters and setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postDate) {
        this.postedDate = postDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isReadOrNot() {
        return readOrNot;
    }

    public void setReadOrNot(boolean readOrNot) {
        this.readOrNot = readOrNot;
    }
}