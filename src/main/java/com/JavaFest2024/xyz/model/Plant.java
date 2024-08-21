package com.JavaFest2024.xyz.model;

import jakarta.persistence.*;

@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cropType;

    @Column(nullable = false)
    private String cropName;

    @Column(columnDefinition = "TEXT")
    private String cropDetails;

    @Column(columnDefinition = "TEXT")
    private String irrigationTips;

    @Column(columnDefinition = "TEXT")
    private String fertilizerApplication;

    @Column(columnDefinition = "TEXT")
    private String pestManagement;

    @Column(columnDefinition = "TEXT")
    private String harvesting;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    // New fields
    @Column(name = "need_duration")
    private String needDuration;

    @Column(name = "required_ph")
    private Double requiredPH;

    @Column(name = "required_n")
    private Double requiredN;

    @Column(name = "required_p")
    private Double requiredP;

    @Column(name = "required_k")
    private Double requiredK;

    @Column(name = "l_comments", columnDefinition = "TEXT")
    private String lComments;

    @Column(name = "u_comments", columnDefinition = "TEXT")
    private String uComments;

    @Column(name = "soil_type")
    private String soilType;

    // Existing getters and setters...


    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public String getuComments() {
        return uComments;
    }

    public void setuComments(String uComments) {
        this.uComments = uComments;
    }

    public String getlComments() {
        return lComments;
    }

    public void setlComments(String lComments) {
        this.lComments = lComments;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHarvesting() {
        return harvesting;
    }

    public void setHarvesting(String harvesting) {
        this.harvesting = harvesting;
    }

    public String getPestManagement() {
        return pestManagement;
    }

    public void setPestManagement(String pestManagement) {
        this.pestManagement = pestManagement;
    }

    public String getFertilizerApplication() {
        return fertilizerApplication;
    }

    public void setFertilizerApplication(String fertilizerApplication) {
        this.fertilizerApplication = fertilizerApplication;
    }

    public String getIrrigationTips() {
        return irrigationTips;
    }

    public void setIrrigationTips(String irrigationTips) {
        this.irrigationTips = irrigationTips;
    }

    public String getCropDetails() {
        return cropDetails;
    }

    public void setCropDetails(String cropDetails) {
        this.cropDetails = cropDetails;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // New getters and setters
    public String getNeedDuration() {
        return needDuration;
    }

    public void setNeedDuration(String needDuration) {
        this.needDuration = needDuration;
    }

    public Double getRequiredPH() {
        return requiredPH;
    }

    public void setRequiredPH(Double requiredPH) {
        this.requiredPH = requiredPH;
    }

    public Double getRequiredN() {
        return requiredN;
    }

    public void setRequiredN(Double requiredN) {
        this.requiredN = requiredN;
    }

    public Double getRequiredP() {
        return requiredP;
    }

    public void setRequiredP(Double requiredP) {
        this.requiredP = requiredP;
    }

    public Double getRequiredK() {
        return requiredK;
    }

    public void setRequiredK(Double requiredK) {
        this.requiredK = requiredK;
    }

    public String getLComments() {
        return lComments;
    }

    public void setLComments(String lComments) {
        this.lComments = lComments;
    }

    public String getUComments() {
        return uComments;
    }

    public void setUComments(String uComments) {
        this.uComments = uComments;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "id=" + id +
                ", cropType='" + cropType + '\'' +
                ", cropName='" + cropName + '\'' +
                ", cropDetails='" + cropDetails + '\'' +
                ", irrigationTips='" + irrigationTips + '\'' +
                ", fertilizerApplication='" + fertilizerApplication + '\'' +
                ", pestManagement='" + pestManagement + '\'' +
                ", harvesting='" + harvesting + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", needDuration='" + needDuration + '\'' +
                ", requiredPH=" + requiredPH +
                ", requiredN=" + requiredN +
                ", requiredP=" + requiredP +
                ", requiredK=" + requiredK +
                ", lComments='" + lComments + '\'' +
                ", uComments='" + uComments + '\'' +
                ", soilType='" + soilType + '\'' +
                '}';
    }
}