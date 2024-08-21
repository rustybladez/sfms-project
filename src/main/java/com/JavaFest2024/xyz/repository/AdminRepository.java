package com.JavaFest2024.xyz.repository;

import com.JavaFest2024.xyz.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email);
}