package com.JavaFest2024.xyz.service;

import com.JavaFest2024.xyz.model.TreeProfile;
import com.JavaFest2024.xyz.repository.TreeProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreeProfileService {

    @Autowired
    private TreeProfileRepository treeProfileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public TreeProfile saveTreeProfile(String treeProfileJson, MultipartFile imageFile) throws IOException {
        TreeProfile treeProfile = objectMapper.readValue(treeProfileJson, TreeProfile.class);
        if (imageFile != null && !imageFile.isEmpty()) {
            String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
            treeProfile.setTreeImageBase64(base64Image);
        }
        return treeProfileRepository.save(treeProfile);
    }

    public List<TreeProfile> getAllTreeProfiles() {
        return treeProfileRepository.findAll();
    }

    public TreeProfile getTreeProfileById(Long id) {
        return treeProfileRepository.findById(id).orElse(null);
    }

    public TreeProfile updateTreeProfile(Long id, String treeProfileJson, MultipartFile imageFile) throws IOException {
        TreeProfile existingProfile = treeProfileRepository.findById(id).orElse(null);
        if (existingProfile == null) {
            return null;
        }

        TreeProfile updatedProfile = objectMapper.readValue(treeProfileJson, TreeProfile.class);
        existingProfile.setTreeType(updatedProfile.getTreeType());
        existingProfile.setTreeName(updatedProfile.getTreeName());
        existingProfile.setSeasons(updatedProfile.getSeasons());
        existingProfile.setHarvestMonths(updatedProfile.getHarvestMonths());

        if (imageFile != null && !imageFile.isEmpty()) {
            String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
            existingProfile.setTreeImageBase64(base64Image);
        }

        return treeProfileRepository.save(existingProfile);
    }

    public void deleteTreeProfile(Long id) {
        treeProfileRepository.deleteById(id);
    }

    public List<TreeProfile> getTreeProfilesByFilter(String filterType, String filterValue) {
        List<TreeProfile> allProfiles = getAllTreeProfiles();
        return allProfiles.stream()
                .filter(profile -> {
                    if ("season".equalsIgnoreCase(filterType)) {
                        return profile.getSeasons().stream()
                                .anyMatch(season -> season.trim().equalsIgnoreCase(filterValue.trim()));
                    } else if ("month".equalsIgnoreCase(filterType)) {
                        return profile.getHarvestMonths().stream()
                                .anyMatch(month -> month.trim().equalsIgnoreCase(filterValue.trim()));
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    //This method is for AdminDashBoard

    public Long getSeasonalPlantCount() {
        return treeProfileRepository.countSeasonalPlants();
    }
}