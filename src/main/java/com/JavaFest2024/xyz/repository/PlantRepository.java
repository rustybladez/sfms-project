package com.JavaFest2024.xyz.repository;

import com.JavaFest2024.xyz.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

    // Find all plants by crop type
    List<Plant> findByCropType(String cropType);

    // Get all distinct crop types
    @Query("SELECT DISTINCT p.cropType FROM Plant p")
    List<String> findDistinctCropTypes();

    // Find plants by crop name (useful for searching)
    List<Plant> findByCropNameContainingIgnoreCase(String cropName);

    // Find plants by crop type and name (useful for filtering)
    List<Plant> findByCropTypeAndCropNameContainingIgnoreCase(String cropType, String cropName);
}