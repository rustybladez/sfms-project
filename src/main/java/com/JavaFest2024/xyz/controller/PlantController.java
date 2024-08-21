package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.model.*;
import com.JavaFest2024.xyz.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    @Autowired
    private PlantService plantService;
    private final Path root = Paths.get("uploads");

    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(file))
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addPlant(@ModelAttribute Plant plant, @RequestParam("file") MultipartFile file) {
        try {
            Plant savedPlant = plantService.savePlant(plant, file);
            return ResponseEntity.ok(savedPlant);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error occurred while saving the plant: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<Plant>> getAllPlants() {
        return ResponseEntity.ok(plantService.getAllPlants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plant> getPlantById(@PathVariable Long id) {
        return plantService.getPlantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlant(@PathVariable Long id, @ModelAttribute Plant plantDetails, @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Plant updatedPlant = plantService.updatePlant(id, plantDetails, file);
            if (updatedPlant != null) {
                return ResponseEntity.ok(updatedPlant);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error occurred while updating the plant: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}/harvest-info")
    public ResponseEntity<Map<String, String>> getHarvestInfo(@PathVariable Long id) {
        return plantService.getHarvestInfo(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Prediction part
    @PostMapping("/predict")
    public ResponseEntity<PlantPredictionResponse> predictPlantSuitability(@RequestBody PlantPredictionRequest request) {
        PlantPredictionResponse response = plantService.predictPlantSuitability(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-crop-type/{cropType}")
    public ResponseEntity<List<Plant>> getPlantsByCropType(@PathVariable String cropType) {
        List<Plant> plants = plantService.getPlantsByCropType(cropType);
        return ResponseEntity.ok(plants);
    }
// Soil testing page.
@GetMapping("/crop-types")
public ResponseEntity<List<String>> getAllCropTypes() {
    List<String> cropTypes = plantService.getAllCropTypes();
    return ResponseEntity.ok(cropTypes);
}
    @PostMapping("/soil-test")
    public ResponseEntity<SoilTestResponse> testSoil(@RequestBody SoilTestRequest request) {
        SoilTestResponse response = plantService.testSoil(request);
        return ResponseEntity.ok(response);
    }
//AdminDashBoard Summery
@GetMapping("/count")
public ResponseEntity<Long> getTotalPlantCount() {
    long count = plantService.getTotalPlantCount();
    return ResponseEntity.ok(count);
}
}