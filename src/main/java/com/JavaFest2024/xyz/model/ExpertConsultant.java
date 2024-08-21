package com.JavaFest2024.xyz.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "expert_consultants")
public class ExpertConsultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalTime availabilityStart;

    @Column(nullable = false)
    private LocalTime availabilityEnd;

    @Column(nullable = false, length = 1000)
    private String bio;

    @Lob
    @Column(name = "profile_image", columnDefinition="LONGBLOB")
    private byte[] profileImage;

    @Transient
    private String profileImageBase64;

    // Existing getters and setters...

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageBase64() {
        return profileImageBase64;
    }

    public void setProfileImageBase64(String profileImageBase64) {
        this.profileImageBase64 = profileImageBase64;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalTime getAvailabilityStart() {
        return availabilityStart;
    }

    public void setAvailabilityStart(LocalTime availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    public LocalTime getAvailabilityEnd() {
        return availabilityEnd;
    }

    public void setAvailabilityEnd(LocalTime availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    // toString method (if needed)
    @Override
    public String toString() {
        return "ExpertConsultant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", availabilityStart=" + availabilityStart +
                ", availabilityEnd=" + availabilityEnd +
                ", bio='" + bio + '\'' +
                ", profileImage='" + (profileImage != null ? "Image data available" : "No image") + '\'' +
                '}';
    }
}