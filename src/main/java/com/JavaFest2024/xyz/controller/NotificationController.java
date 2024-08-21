package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.model.Notification;
import com.JavaFest2024.xyz.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Show notifications on Admin side
    @GetMapping("/admin/features/notifications")
    public String showNotifications(Model model) {
        model.addAttribute("notifications", notificationService.findAllNotifications());
        model.addAttribute("notification", new Notification());
        return "admin/features/notifications";
    }

    // Show notifications on User side
    @GetMapping("/user/features/Notification/all")
    public String getAllNotifications(Model model) {
        List<Notification> notifications = notificationService.findAllNotifications();
        model.addAttribute("notifications", notifications);
        model.addAttribute("type", "all");
        return "user/features/Notification";
    }

    // Filter notifications by type
    @GetMapping("/user/features/Notification/{type}")
    public String getNotificationsByType(@PathVariable String type, Model model) {
        List<Notification> notifications = notificationService.findNotificationsByType(type);
        System.out.println("Type: " + type + ", Notifications found: " + notifications.size());
        model.addAttribute("notifications", notifications);
        model.addAttribute("type", type);
        return "user/features/Notification";
    }

    // Post a new notification from Admin side
    @PostMapping("/admin/features/notifications/create")
    public String createNotification(@ModelAttribute Notification notification) {
        notificationService.saveNotification(notification);
        return "redirect:/admin/features/notifications";
    }

    // Edit notifications from Admin side
    @GetMapping("/admin/features/notifications/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification != null) {
            model.addAttribute("notification", notification);
            return "admin/features/editNotification";
        } else {
            return "redirect:/admin/features/notifications";
        }
    }

    // Updating a notification from Admin side
    @PostMapping("/admin/features/notifications/update/{id}")
    public String updateNotification(@PathVariable Long id, @ModelAttribute Notification notification) {
        notification.setId(id);
        notificationService.saveNotification(notification);
        return "redirect:/admin/features/notifications";
    }

    // Mark a notification as read on User side
    @PostMapping("/user/features/Notification/markAsRead/{id}")
    public String markAsRead(@PathVariable Long id, @RequestParam String type, RedirectAttributes redirectAttributes) {
        try {
            notificationService.markAsRead(id);
            redirectAttributes.addFlashAttribute("message", "Notification marked as read successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error marking notification as read: " + e.getMessage());
        }
        return "redirect:/user/features/Notification/" + type;
    }

    // Delete a notification from Admin side
    @PostMapping("/admin/features/notifications/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "redirect:/admin/features/notifications";
    }

    // New method to get notification details for the pop-up
    @GetMapping("/user/features/Notification/view/{id}")
    @ResponseBody
    public ResponseEntity<Notification> viewNotification(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}