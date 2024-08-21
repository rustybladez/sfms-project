package com.JavaFest2024.xyz.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tree_profiles")
public class TreeProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String treeType;

    @Column(nullable = false)
    private String treeName;

    @ElementCollection
    @CollectionTable(name = "tree_seasons", joinColumns = @JoinColumn(name = "tree_profile_id"))
    @Column(name = "season")
    private List<String> seasons;

    @ElementCollection
    @CollectionTable(name = "tree_harvest_months", joinColumns = @JoinColumn(name = "tree_profile_id"))
    @Column(name = "month")
    private List<String> harvestMonths;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String treeImageBase64;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTreeType() {
        return treeType;
    }

    public void setTreeType(String treeType) {
        this.treeType = treeType;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public List<String> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<String> seasons) {
        this.seasons = seasons;
    }

    public List<String> getHarvestMonths() {
        return harvestMonths;
    }

    public void setHarvestMonths(List<String> harvestMonths) {
        this.harvestMonths = harvestMonths;
    }

    public String getTreeImageBase64() {
        return treeImageBase64;
    }

    public void setTreeImageBase64(String treeImageBase64) {
        this.treeImageBase64 = treeImageBase64;
    }

    @Override
    public String toString() {
        return "TreeProfile{" +
                "id=" + id +
                ", treeType='" + treeType + '\'' +
                ", treeName='" + treeName + '\'' +
                ", seasons=" + seasons +
                ", harvestMonths=" + harvestMonths +
                ", treeImageBase64='" + (treeImageBase64 != null ? "present" : "null") + '\'' +
                '}';
    }
}