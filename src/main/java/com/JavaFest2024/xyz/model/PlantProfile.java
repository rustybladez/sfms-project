package com.JavaFest2024.xyz.model;

import jakarta.persistence.*;

@Entity
@Table(name = "plant_profiles")
public class PlantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(nullable = false)
    private String userId;

    @Column
    private String customNotes;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomNotes() {
        return customNotes;
    }

    public void setCustomNotes(String customNotes) {
        this.customNotes = customNotes;
    }

    @Override
    public String toString() {
        return "PlantProfile{" +
                "id=" + id +
                ", plant=" + plant +
                ", userId='" + userId + '\'' +
                ", customNotes='" + customNotes + '\'' +
                '}';
    }
}