package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.model.User;
import com.JavaFest2024.xyz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/Profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        User user = userService.findUserByEmail(userEmail);
        model.addAttribute("user", user);
        model.addAttribute("editMode", false);
        return "user/Profile";
    }

    @GetMapping("/edit")
    public String editProfile(Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return "redirect:/user/login";
        }
        User user = userService.findUserByEmail(userEmail);
        if (user == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("editMode", true);
        return "user/Profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User updatedUser, Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null || !userEmail.equals(updatedUser.getEmail())) {
            return "redirect:/user/login";
        }
        User user = userService.updateUser(updatedUser);
        if (user == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("editMode", false);
        model.addAttribute("successMessage", "Profile updated successfully!");
        return "user/Profile";
    }
}