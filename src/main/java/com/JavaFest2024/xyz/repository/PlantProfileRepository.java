package com.JavaFest2024.xyz.repository;

import com.JavaFest2024.xyz.model.PlantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantProfileRepository extends JpaRepository<PlantProfile, Long> {
    List<PlantProfile> findByUserId(String userId);
}