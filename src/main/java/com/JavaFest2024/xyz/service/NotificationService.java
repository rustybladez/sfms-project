package com.JavaFest2024.xyz.service;

import com.JavaFest2024.xyz.model.Notification;
import com.JavaFest2024.xyz.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> findAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getCurrentNotifications() {
        return notificationRepository.findByExpiryDateAfter(LocalDate.now());
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public Notification saveNotification(Notification notification) {
        if (notification.getPostedDate() == null) {
            notification.setPostedDate(LocalDate.now());
        }
        return notificationRepository.save(notification);
    }

    public List<Notification> findNotificationsByType(String type) {
        return notificationRepository.findByType(type);
    }

    public Notification markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow();
        notification.setReadOrNot(true);
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

}