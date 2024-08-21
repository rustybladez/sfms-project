package com.JavaFest2024.xyz.service;

import com.JavaFest2024.xyz.model.*;
import com.JavaFest2024.xyz.repository.PlantRepository;
import com.JavaFest2024.xyz.repository.PlantProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PlantService {

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private PlantProfileRepository plantProfileRepository;

    private final Path root = Paths.get("uploads");

    public PlantService() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!", e);
        }
    }

    public Plant savePlant(Plant plant, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            plant.setImageUrl("/api/plants/uploads/" + filename); // Update this line
        }
        return plantRepository.save(plant);
    }

    public Plant updatePlant(Long id, Plant plantDetails, MultipartFile file) throws IOException {
        Optional<Plant> plantOptional = plantRepository.findById(id);
        if (plantOptional.isPresent()) {
            Plant existingPlant = plantOptional.get();
            existingPlant.setCropType(plantDetails.getCropType());
            existingPlant.setCropName(plantDetails.getCropName());
            existingPlant.setCropDetails(plantDetails.getCropDetails());
            existingPlant.setIrrigationTips(plantDetails.getIrrigationTips());
            existingPlant.setFertilizerApplication(plantDetails.getFertilizerApplication());
            existingPlant.setPestManagement(plantDetails.getPestManagement());
            existingPlant.setHarvesting(plantDetails.getHarvesting());

            // Update new fields
            existingPlant.setNeedDuration(plantDetails.getNeedDuration());
            existingPlant.setRequiredPH(plantDetails.getRequiredPH());
            existingPlant.setRequiredN(plantDetails.getRequiredN());
            existingPlant.setRequiredP(plantDetails.getRequiredP());
            existingPlant.setRequiredK(plantDetails.getRequiredK());
            existingPlant.setLComments(plantDetails.getLComments());
            existingPlant.setUComments(plantDetails.getUComments());

            if (file != null && !file.isEmpty()) {
                String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                try {
                    Files.copy(file.getInputStream(), this.root.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                    existingPlant.setImageUrl("/uploads/" + filename); // Store the relative path
                } catch (IOException e) {
                    throw new IOException("Failed to store file " + filename, e);
                }
            }

            return plantRepository.save(existingPlant);
        }
        return null;
    }

    public Plant savePlant(Plant plant) {
        return plantRepository.save(plant);
    }

    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    public Optional<Plant> getPlantById(Long id) {
        return plantRepository.findById(id);
    }

    public void deletePlant(Long id) {
        Optional<Plant> plant = plantRepository.findById(id);
        if (plant.isPresent()) {
            String imageUrl = plant.get().getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    Path imagePath = Paths.get(imageUrl.substring(1)); // Remove leading '/'
                    Files.deleteIfExists(imagePath);
                } catch (IOException e) {
                    // Log the error, but continue with deletion from database
                    System.err.println("Failed to delete image file: " + e.getMessage());
                }
            }
            plantRepository.deleteById(id);
        }
    }

    public Plant updatePlant(Long id, Plant plantDetails) throws IOException {
        return updatePlant(id, plantDetails, null);
    }

    public List<Plant> getPlantsByCropType(String cropType) {
        return plantRepository.findByCropType(cropType);
    }

    public PlantProfile savePlantProfile(PlantProfile plantProfile) {
        return plantProfileRepository.save(plantProfile);
    }

    public List<PlantProfile> getPlantProfilesByUserId(String userId) {
        return plantProfileRepository.findByUserId(userId);
    }

    public Optional<PlantProfile> getPlantProfileById(Long id) {
        return plantProfileRepository.findById(id);
    }

    public void deletePlantProfile(Long id) {
        plantProfileRepository.deleteById(id);
    }

    public PlantProfile updatePlantProfile(Long id, PlantProfile plantProfileDetails) {
        Optional<PlantProfile> plantProfile = plantProfileRepository.findById(id);
        if (plantProfile.isPresent()) {
            PlantProfile existingProfile = plantProfile.get();
            existingProfile.setPlant(plantProfileDetails.getPlant());
            existingProfile.setCustomNotes(plantProfileDetails.getCustomNotes());
            return plantProfileRepository.save(existingProfile);
        }
        return null;
    }

    public Optional<Map<String, String>> getHarvestInfo(Long plantId) {
        return plantRepository.findById(plantId)
                .map(plant -> {
                    Map<String, String> harvestInfo = new HashMap<>();
                    harvestInfo.put("needDuration", plant.getNeedDuration());
                    return harvestInfo;
                });
    }

    //Plant Prediction part
    public PlantPredictionResponse predictPlantSuitability(PlantPredictionRequest request) {
        Optional<Plant> plantOptional = plantRepository.findById(request.getPlantId());

        if (plantOptional.isPresent()) {
            List<String> mismatches = getStrings(request, plantOptional);

            boolean suitable = mismatches.isEmpty();
            String message;
            if (suitable) {
                message = "This plant is suitable for your area!";
            } else {
                message = "This plant may not be suitable for your area. Mismatches in: " + String.join(", ", mismatches);
            }

            return new PlantPredictionResponse(suitable, message);
        } else {
            return new PlantPredictionResponse(false, "Plant not found");
        }
    }

    private List<String> getStrings(PlantPredictionRequest request, Optional<Plant> plantOptional) {
        Plant plant = plantOptional.get();
        List<String> mismatches = new ArrayList<>();

        if (!isWithinRange(plant.getRequiredPH(), request.getPh(), 0.5)) {
            mismatches.add("PH");
        }
        if (!isWithinRange(plant.getRequiredN(), request.getN(), 0.5)) {
            mismatches.add("Nitrogen (N)");
        }
        if (!isWithinRange(plant.getRequiredP(), request.getP(), 0.5)) {
            mismatches.add("Phosphorus (P)");
        }
        if (!isWithinRange(plant.getRequiredK(), request.getK(), 0.5)) {
            mismatches.add("Potassium (K)");
        }
        if (!plant.getSoilType().equalsIgnoreCase(request.getSoilType())) {
            mismatches.add("Soil Type");
        }
        return mismatches;
    }

    // Keep only one implementation of isWithinRange
    private boolean isWithinRange(Double required, Double actual, Double tolerance) {
        if (required == null || actual == null) {
            return false;
        }
        return Math.abs(required - actual) <= tolerance;
    }

    // Soil test part
    public SoilTestResponse testSoil(SoilTestRequest request) {
        Plant plant = plantRepository.findById(request.getPlantId())
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        StringBuilder recommendation = new StringBuilder();
        boolean isSuitable = true;

        if (!isWithinRange(plant.getRequiredPH(), request.getPh(), 0.5)) {
            isSuitable = false;
            recommendation.append("pH level is not suitable. ");
            recommendation.append(getPHRecommendation(plant.getRequiredPH(), request.getPh()));
        }

        if (!isWithinRange(plant.getRequiredN(), request.getN(), 10.0)) {
            isSuitable = false;
            recommendation.append("Nitrogen level is not suitable. ");
            recommendation.append(getNutrientRecommendation("Nitrogen", plant.getRequiredN(), request.getN()));
        }

        if (!isWithinRange(plant.getRequiredP(), request.getP(), 10.0)) {
            isSuitable = false;
            recommendation.append("Phosphorus level is not suitable. ");
            recommendation.append(getNutrientRecommendation("Phosphorus", plant.getRequiredP(), request.getP()));
        }

        if (!isWithinRange(plant.getRequiredK(), request.getK(), 10.0)) {
            isSuitable = false;
            recommendation.append("Potassium level is not suitable. ");
            recommendation.append(getNutrientRecommendation("Potassium", plant.getRequiredK(), request.getK()));
        }

        if (isSuitable) {
            recommendation.append("The soil is suitable for this plant. No amendments are necessary.");
        }

        List<Plant> suitablePlants = findSuitablePlants(request);

        return new SoilTestResponse(recommendation.toString(), suitablePlants);
    }

    private String getPHRecommendation(Double required, Double actual) {
        if (actual < required) {
            return "Consider adding lime to increase soil pH. ";
        } else {
            return "Consider adding sulfur to decrease soil pH. ";
        }
    }

    private String getNutrientRecommendation(String nutrient, Double required, Double actual) {
        if (actual < required) {
            return String.format("Consider adding %s-rich fertilizer. ", nutrient);
        } else {
            return String.format("Reduce %s application. ", nutrient);
        }
    }
    public List<String> getAllCropTypes() {
        return plantRepository.findDistinctCropTypes();
    }

    private List<Plant> findSuitablePlants(SoilTestRequest request) {
        List<Plant> allPlants = plantRepository.findAll();
        return allPlants.stream()
                .filter(p -> isWithinRange(p.getRequiredPH(), request.getPh(), 0.5) &&
                        isWithinRange(p.getRequiredN(), request.getN(), 10.0) &&
                        isWithinRange(p.getRequiredP(), request.getP(), 10.0) &&
                        isWithinRange(p.getRequiredK(), request.getK(), 10.0))
                .collect(Collectors.toList());
    }

    //For adminDashboard summery
    public long getTotalPlantCount() {
        return plantRepository.count();
    }
    //  this new method to get the total count of plant profiles
    public long getTotalPlantProfileCount() {
        return plantProfileRepository.count();
    }
}