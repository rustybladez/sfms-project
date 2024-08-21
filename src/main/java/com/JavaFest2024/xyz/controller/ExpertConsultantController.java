package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.model.ExpertConsultant;
import com.JavaFest2024.xyz.service.ExpertConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/consultants")
public class ExpertConsultantController {

    private final ExpertConsultantService consultantService;

    @Autowired
    public ExpertConsultantController(ExpertConsultantService consultantService) {
        this.consultantService = consultantService;
    }

    @GetMapping
    public List<ExpertConsultant> getAllConsultants() {
        List<ExpertConsultant> consultants = consultantService.getAllConsultants();
        for (ExpertConsultant consultant : consultants) {
            if (consultant.getProfileImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(consultant.getProfileImage());
                consultant.setProfileImageBase64("data:image/jpeg;base64," + base64Image);
            }
        }
        return consultants;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpertConsultant> getConsultantById(@PathVariable Long id) {
        return consultantService.getConsultantById(id)
                .map(consultant -> {
                    if (consultant.getProfileImage() != null) {
                        String base64Image = Base64.getEncoder().encodeToString(consultant.getProfileImage());
                        consultant.setProfileImageBase64("data:image/jpeg;base64," + base64Image);
                    }
                    return ResponseEntity.ok(consultant);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExpertConsultant> createConsultant(@RequestParam("name") String name,
                                                             @RequestParam("specialization") String specialization,
                                                             @RequestParam("email") String email,
                                                             @RequestParam("phone") String phone,
                                                             @RequestParam("availabilityStart") String availabilityStart,
                                                             @RequestParam("availabilityEnd") String availabilityEnd,
                                                             @RequestParam("bio") String bio,
                                                             @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
        ExpertConsultant consultant = new ExpertConsultant();
        consultant.setName(name);
        consultant.setSpecialization(specialization);
        consultant.setEmail(email);
        consultant.setPhone(phone);
        consultant.setAvailabilityStart(LocalTime.parse(availabilityStart));
        consultant.setAvailabilityEnd(LocalTime.parse(availabilityEnd));
        consultant.setBio(bio);
        consultant.setProfileImage(profileImage.getBytes());

        ExpertConsultant savedConsultant = consultantService.saveConsultant(consultant);
        return ResponseEntity.ok(savedConsultant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpertConsultant> updateConsultant(@PathVariable Long id,
                                                             @RequestParam("name") String name,
                                                             @RequestParam("specialization") String specialization,
                                                             @RequestParam("email") String email,
                                                             @RequestParam("phone") String phone,
                                                             @RequestParam("availabilityStart") String availabilityStart,
                                                             @RequestParam("availabilityEnd") String availabilityEnd,
                                                             @RequestParam("bio") String bio,
                                                             @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {
        ExpertConsultant consultantDetails = new ExpertConsultant();
        consultantDetails.setName(name);
        consultantDetails.setSpecialization(specialization);
        consultantDetails.setEmail(email);
        consultantDetails.setPhone(phone);
        consultantDetails.setAvailabilityStart(LocalTime.parse(availabilityStart));
        consultantDetails.setAvailabilityEnd(LocalTime.parse(availabilityEnd));
        consultantDetails.setBio(bio);
        if (profileImage != null && !profileImage.isEmpty()) {
            consultantDetails.setProfileImage(profileImage.getBytes());
        }

        try {
            ExpertConsultant updatedConsultant = consultantService.updateConsultant(id, consultantDetails);
            return ResponseEntity.ok(updatedConsultant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultant(@PathVariable Long id) {
        try {
            consultantService.deleteConsultant(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/update-image")
    public ResponseEntity<ExpertConsultant> updateProfileImage(@PathVariable Long id, @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
        ExpertConsultant updatedConsultant = consultantService.updateProfileImage(id, profileImage.getBytes());
        if (updatedConsultant.getProfileImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(updatedConsultant.getProfileImage());
            updatedConsultant.setProfileImageBase64("data:image/jpeg;base64," + base64Image);
        }
        return ResponseEntity.ok(updatedConsultant);
    }

    //This method is for AdminDashBoard Summery

    @GetMapping("/count")
    public ResponseEntity<Long> getConsultantCount() {
        long count = consultantService.getConsultantCount();
        return ResponseEntity.ok(count);
    }
}