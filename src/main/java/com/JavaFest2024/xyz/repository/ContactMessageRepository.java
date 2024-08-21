package com.JavaFest2024.xyz.repository;

import com.JavaFest2024.xyz.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    ContactMessage findByEmail(String email);
}
