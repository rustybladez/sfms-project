package com.JavaFest2024.xyz.repository;

import com.JavaFest2024.xyz.model.ExpertConsultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertConsultantRepository extends JpaRepository<ExpertConsultant, Long> {
    // You can add custom query methods here if needed
}