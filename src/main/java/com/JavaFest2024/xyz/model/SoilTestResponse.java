package com.JavaFest2024.xyz.model;

import java.util.List;

public class SoilTestResponse {
    private String recommendation;
    private List<Plant> suitablePlants;

    // Constructors

    public SoilTestResponse() {}

    public SoilTestResponse(String recommendation, List<Plant> suitablePlants) {
        this.recommendation = recommendation;
        this.suitablePlants = suitablePlants;
    }

    // Getters and setters

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public List<Plant> getSuitablePlants() {
        return suitablePlants;
    }

    public void setSuitablePlants(List<Plant> suitablePlants) {
        this.suitablePlants = suitablePlants;
    }

    @Override
    public String toString() {
        return "SoilTestResponse{" +
                "recommendation='" + recommendation + '\'' +
                ", suitablePlants=" + suitablePlants +
                '}';
    }
}