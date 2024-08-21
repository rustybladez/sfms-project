package com.JavaFest2024.xyz.model;

public class PlantPredictionRequest {
    private Long plantId;
    private Double ph;
    private Double n;
    private Double p;
    private Double k;
    private String soilType;

    // Getters and setters
    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public Double getPh() {
        return ph;
    }

    public void setPh(Double ph) {
        this.ph = ph;
    }

    public Double getN() {
        return n;
    }

    public void setN(Double n) {
        this.n = n;
    }

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Double getK() {
        return k;
    }

    public void setK(Double k) {
        this.k = k;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    @Override
    public String toString() {
        return "PlantPredictionRequest{" +
                "plantId=" + plantId +
                ", ph=" + ph +
                ", n=" + n +
                ", p=" + p +
                ", k=" + k +
                ", soilType='" + soilType + '\'' +
                '}';
    }
}