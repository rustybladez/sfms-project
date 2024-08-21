package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.model.User;
import com.JavaFest2024.xyz.service.EmailService;
import com.JavaFest2024.xyz.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/features/CheckTotalUser")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        logger.info("Retrieved {} users", users.size());
        model.addAttribute("users", users);
        return "admin/features/CheckTotalUser";
    }

    @GetMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/api/users/send-message")
    @ResponseBody
    public ResponseEntity<Void> sendMessage(@RequestParam String email, @RequestParam String content) {
        boolean sent = emailService.sendEmail(email, "Message from Admin", content);
        return sent ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }

// DashBoard Card summery
@GetMapping("/features/AdminDashboard")
public String getDashboard(Model model) {
    long totalUserCount = userService.getTotalUserCount();
    model.addAttribute("totalUserCount", totalUserCount);
    model.addAttribute("welcomeMessage", "Welcome to the Admin Dashboard");
    logger.info("Total user count: {}", totalUserCount); // Add this log
    return "admin/features/AdminDashboard"; // Update this to match your actual template name
}
}