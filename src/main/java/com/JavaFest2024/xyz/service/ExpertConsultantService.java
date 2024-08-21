package com.JavaFest2024.xyz.service;

import com.JavaFest2024.xyz.model.ExpertConsultant;
import com.JavaFest2024.xyz.repository.ExpertConsultantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpertConsultantService {

    private static final Logger logger = LoggerFactory.getLogger(ExpertConsultantService.class);

    private final ExpertConsultantRepository consultantRepository;

    @Autowired
    public ExpertConsultantService(ExpertConsultantRepository consultantRepository) {
        this.consultantRepository = consultantRepository;
    }

    public List<ExpertConsultant> getAllConsultants() {
        logger.info("Fetching all consultants");
        return consultantRepository.findAll();
    }

    public Optional<ExpertConsultant> getConsultantById(Long id) {
        logger.info("Fetching consultant with id: {}", id);
        return consultantRepository.findById(id);
    }

    public ExpertConsultant saveConsultant(ExpertConsultant consultant) {
        logger.info("Saving new consultant: {}", consultant.getName());
        return consultantRepository.save(consultant);
    }

    public ExpertConsultant updateConsultant(Long id, ExpertConsultant consultantDetails) {
        logger.info("Updating consultant with id: {}", id);
        return consultantRepository.findById(id)
                .map(existingConsultant -> {
                    existingConsultant.setName(consultantDetails.getName());
                    existingConsultant.setSpecialization(consultantDetails.getSpecialization());
                    existingConsultant.setEmail(consultantDetails.getEmail());
                    existingConsultant.setPhone(consultantDetails.getPhone());
                    existingConsultant.setAvailabilityStart(consultantDetails.getAvailabilityStart());
                    existingConsultant.setAvailabilityEnd(consultantDetails.getAvailabilityEnd());
                    existingConsultant.setBio(consultantDetails.getBio());
                    if (consultantDetails.getProfileImage() != null) {
                        existingConsultant.setProfileImage(consultantDetails.getProfileImage());
                    }
                    return consultantRepository.save(existingConsultant);
                })
                .orElseThrow(() -> {
                    logger.error("Consultant not found with id: {}", id);
                    return new RuntimeException("Consultant not found with id: " + id);
                });
    }

    public void deleteConsultant(Long id) {
        logger.info("Deleting consultant with id: {}", id);
        consultantRepository.findById(id)
                .ifPresentOrElse(
                        consultant -> consultantRepository.delete(consultant),
                        () -> {
                            logger.error("Consultant not found with id: {}", id);
                            throw new RuntimeException("Consultant not found with id: " + id);
                        }
                );
    }
    public ExpertConsultant updateProfileImage(Long id, byte[] profileImage) {
        logger.info("Updating profile image for consultant with id: {}", id);
        return consultantRepository.findById(id)
                .map(existingConsultant -> {
                    existingConsultant.setProfileImage(profileImage);
                    return consultantRepository.save(existingConsultant);
                })
                .orElseThrow(() -> {
                    logger.error("Consultant not found with id: {}", id);
                    return new RuntimeException("Consultant not found with id: " + id);
                });
    }

    //This method is for AdminDashBoard Summery
    public long getConsultantCount() {
        logger.info("Fetching total number of consultants");
        return consultantRepository.count();
    }

}