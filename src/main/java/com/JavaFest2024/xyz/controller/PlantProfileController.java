package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.model.Plant;
import com.JavaFest2024.xyz.model.PlantProfile;
import com.JavaFest2024.xyz.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plant-profiles")
public class PlantProfileController {

    @Autowired
    private PlantService plantService;

    @GetMapping("/crop-types")
    public ResponseEntity<List<String>> getAllCropTypes() {
        return ResponseEntity.ok(plantService.getAllCropTypes());
    }

    @GetMapping("/plants/{cropType}")
    public ResponseEntity<List<Plant>> getPlantsByCropType(@PathVariable String cropType) {
        return ResponseEntity.ok(plantService.getPlantsByCropType(cropType));
    }

    @PostMapping
    public ResponseEntity<PlantProfile> addPlantProfile(@RequestBody PlantProfile plantProfile) {
        return ResponseEntity.ok(plantService.savePlantProfile(plantProfile));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlantProfile>> getPlantProfilesByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(plantService.getPlantProfilesByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantProfile> getPlantProfileById(@PathVariable Long id) {
        return plantService.getPlantProfileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantProfile> updatePlantProfile(@PathVariable Long id, @RequestBody PlantProfile plantProfileDetails) {
        PlantProfile updatedProfile = plantService.updatePlantProfile(id, plantProfileDetails);
        return updatedProfile != null ? ResponseEntity.ok(updatedProfile) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlantProfile(@PathVariable Long id) {
        plantService.deletePlantProfile(id);
        return ResponseEntity.ok().build();
    }

    // this new endpoint to get the total count of plant profiles
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalPlantProfileCount() {
        return ResponseEntity.ok(plantService.getTotalPlantProfileCount());
    }
}