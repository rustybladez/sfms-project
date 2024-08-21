package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.model.TreeProfile;
import com.JavaFest2024.xyz.service.TreeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tree-profiles")
public class TreeProfileController {

    @Autowired
    private TreeProfileService treeProfileService;

    @PostMapping
    public ResponseEntity<TreeProfile> createTreeProfile(
            @RequestPart("treeProfile") String treeProfileJson,
            @RequestPart("treeImage") MultipartFile treeImage) {
        try {
            TreeProfile savedProfile = treeProfileService.saveTreeProfile(treeProfileJson, treeImage);
            return new ResponseEntity<>(savedProfile, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<TreeProfile>> getAllTreeProfiles() {
        List<TreeProfile> profiles = treeProfileService.getAllTreeProfiles();
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreeProfile> getTreeProfileById(@PathVariable Long id) {
        TreeProfile profile = treeProfileService.getTreeProfileById(id);
        if (profile != null) {
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreeProfile> updateTreeProfile(
            @PathVariable Long id,
            @RequestPart("treeProfile") String treeProfileJson,
            @RequestPart(value = "treeImage", required = false) MultipartFile treeImage) {
        try {
            TreeProfile updatedProfile = treeProfileService.updateTreeProfile(id, treeProfileJson, treeImage);
            if (updatedProfile != null) {
                return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTreeProfile(@PathVariable Long id) {
        try {
            treeProfileService.deleteTreeProfile(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //tree suggestions
    @GetMapping("/{filterType}/{filterValue}")
    public ResponseEntity<List<TreeProfile>> getTreeProfilesByFilter(
            @PathVariable String filterType,
            @PathVariable String filterValue) {
        System.out.println("Received request for filter type: " + filterType + ", value: " + filterValue);
        List<TreeProfile> profiles = treeProfileService.getTreeProfilesByFilter(filterType, filterValue);
        System.out.println("Returning " + profiles.size() + " profiles");
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    //This Method is for AdminDashBoard Summery
    @GetMapping("/seasonal/count")
    public ResponseEntity<Long> getSeasonalPlantCount() {
        Long count = treeProfileService.getSeasonalPlantCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}