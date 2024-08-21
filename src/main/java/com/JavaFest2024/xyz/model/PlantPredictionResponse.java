package com.JavaFest2024.xyz.model;

public class PlantPredictionResponse {
    private boolean suitable;
    private String message;

    public PlantPredictionResponse(boolean suitable, String message) {
        this.suitable = suitable;
        this.message = message;
    }

    // Getters and setters

    public boolean isSuitable() {
        return suitable;
    }

    public void setSuitable(boolean suitable) {
        this.suitable = suitable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PlantPredictionResponse{" +
                "suitable=" + suitable +
                ", message='" + message + '\'' +
                '}';
    }
}