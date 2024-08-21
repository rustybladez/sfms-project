package com.JavaFest2024.xyz.repository;

import com.JavaFest2024.xyz.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByType(String type);
    List<Notification> findByExpiryDateAfter(LocalDate date);
}