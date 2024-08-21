package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.model.Admin;
import com.JavaFest2024.xyz.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminProfileController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/features/MyProfile")
    public String showMyProfile(Model model, HttpSession session) {
        String adminEmail = (String) session.getAttribute("adminEmail");
        Admin admin = adminService.getAdminByEmail(adminEmail);
        model.addAttribute("admin", admin);
        return "admin/features/MyProfile";
    }

    @PostMapping("/admin/updateProfile")
    public String updateProfile(@ModelAttribute Admin admin, @RequestParam(required = false) String newPassword, HttpSession session) {
        String adminEmail = (String) session.getAttribute("adminEmail");
        if (adminEmail == null || !adminEmail.equals(admin.getEmail())) {
            return "redirect:/admin/AdminLogin";
        }
        Admin existingAdmin = adminService.getAdminByEmail(adminEmail);
        existingAdmin.setFullName(admin.getFullName());

        if (newPassword != null && !newPassword.isEmpty()) {
            existingAdmin.setPassword(newPassword);
        }

        adminService.updateAdmin(existingAdmin);
        return "redirect:/admin/features/MyProfile";
    }
}