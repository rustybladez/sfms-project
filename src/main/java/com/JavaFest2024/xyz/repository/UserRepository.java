package com.JavaFest2024.xyz.repository;

import com.JavaFest2024.xyz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}