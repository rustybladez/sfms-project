package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.service.AdminService;
import com.JavaFest2024.xyz.service.EmailService;
import com.JavaFest2024.xyz.model.User;
import com.JavaFest2024.xyz.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin(origins = "http://localhost:1010", allowCredentials = "true")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmailService emailService;

    private Map<String, Integer> weatherToggleCounts = new HashMap<>();

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/user/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/user/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        if (userService.authenticateUser(email, password)) {
            session.setAttribute("userEmail", email);
            return "redirect:/user/features/UserDashBoard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "user/login";
        }
    }

    @GetMapping("/user/features/UserDashBoard")
    public String UserDashBoard() {
        return "user/features/UserDashBoard";
    }

    @GetMapping("/login/oauth2/code/google")
    public String handleGoogleLogin(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            return "redirect:/user/features/UserDashBoard";
        } else {
            return "redirect:/user/login";
        }
    }

    @GetMapping("/user/SignUp")
    public String register() {
        return "user/SignUp";
    }

    @PostMapping("/user/SignUp")
    public String signUp(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:login";
    }

    @GetMapping("/user/ForgetPassword")
    public String forgetPassword() {
        return "user/ForgetPassword";
    }

    @PostMapping("/user/verifyEmailAndSendOTP")
    @ResponseBody
    public Map<String, Object> verifyEmailAndSendOTP(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.findUserByEmail(email);
        if (user != null) {
            String otp = emailService.generateOTP();
            boolean emailSent = emailService.sendOTP(email, otp);
            if (emailSent) {
                response.put("success", true);
                response.put("message", "OTP sent successfully");
            } else {
                response.put("success", false);
                response.put("message", "Failed to send OTP");
            }
        } else {
            response.put("success", false);
            response.put("message", "Email not found");
        }
        return response;
    }

    @PostMapping("/user/ForgetPassword")
    @ResponseBody
    public Map<String, Object> processForgotPassword(@RequestParam String email, @RequestParam String otp) {
        Map<String, Object> response = new HashMap<>();
        if (emailService.verifyOTP(email, otp)) {
            response.put("success", true);
            response.put("message", "OTP verified successfully");
        } else {
            response.put("success", false);
            response.put("message", "Invalid OTP");
        }
        return response;
    }

    @GetMapping("/user/ResetPassword")
    public String resetPassword(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "user/ResetPassword";
    }

    @PostMapping("/user/ResetPassword")
    public String processResetPassword(@RequestParam String email,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmPassword,
                                       Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("email", email);
            return "user/ResetPassword";
        }

        if (userService.resetPassword(email, newPassword)) {
            model.addAttribute("success", "Password reset successful");
            return "redirect:/user/login";
        } else {
            model.addAttribute("error", "Password reset failed");
            model.addAttribute("email", email);
            return "user/ResetPassword";
        }
    }

    @GetMapping("/admin/AdminLogin")
    public String adminLogin() {
        return "admin/AdminLogin";
    }

    @PostMapping("/admin/features/AdminDashboard")
    public String adminLogin(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        if (adminService.authenticateAdmin(email, password)) {
            String fullName = adminService.getAdminFullName(email);
            model.addAttribute("welcomeMessage", "Welcome " + fullName);
            session.setAttribute("adminEmail", email);  // Set admin email in session
            return "redirect:AdminDashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "admin/AdminLogin";
        }
    }

    @GetMapping("/admin/AdminForgetPassword")
    public String AdminForgetPassword() {
        return "admin/AdminForgetPassword";
    }
    @PostMapping("/admin/AdminForgetPassword")
    public String processAdminForgotPassword(@RequestParam String email, Model model) {
        if (adminService.findAdminByEmail(email) != null) {
            return "redirect:/admin/AdminResetPassword?email=" + email;
        } else {
            model.addAttribute("error", "Email not found");
            return "admin/AdminForgetPassword";
        }
    }
    @PostMapping("/admin/verifyEmailAndSendOTP")
    @ResponseBody
    public Map<String, Object> adminVerifyEmailAndSendOTP(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        if (adminService.findAdminByEmail(email) != null) {
            String otp = emailService.generateOTP();
            boolean emailSent = emailService.sendOTP(email, otp);
            if (emailSent) {
                response.put("success", true);
                response.put("message", "OTP sent successfully");
            } else {
                response.put("success", false);
                response.put("message", "Failed to send OTP");
            }
        } else {
            response.put("success", false);
            response.put("message", "Email not found");
        }
        return response;
    }

    @PostMapping("/admin/verifyOTPAndResetPassword")
    @ResponseBody
    public Map<String, Object> adminVerifyOTPAndResetPassword(@RequestParam String email, @RequestParam String otp) {
        Map<String, Object> response = new HashMap<>();
        if (emailService.verifyOTP(email, otp)) {
            response.put("success", true);
            response.put("message", "OTP verified successfully");
        } else {
            response.put("success", false);
            response.put("message", "Invalid OTP");
        }
        return response;
    }

    @GetMapping("/admin/AdminResetPassword")
    public String AdminResetPassword(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "admin/AdminResetPassword";
    }

    @PostMapping("/admin/AdminResetPassword")
    public String processAdminResetPassword(@RequestParam String email, @RequestParam String newPassword,
                                            @RequestParam String retypePassword, Model model,
                                            RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(retypePassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("email", email);
            return "admin/AdminResetPassword";
        }

        try {
            if (adminService.resetAdminPassword(email, newPassword)) {
                redirectAttributes.addFlashAttribute("successMessage", "New Password Set Successfully");
                return "redirect:/admin/AdminLogin";
            } else {
                model.addAttribute("error", "Something went wrong. Please try again.");
                model.addAttribute("email", email);
                return "admin/AdminResetPassword";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            model.addAttribute("email", email);
            return "admin/AdminResetPassword";
        }
    }

    @GetMapping("/admin/features/SeasonalPlant")
    public String SeasonalPlant() {
        return "admin/features/SeasonalPlant";
    }

    @GetMapping("/admin/features/CropSectionManagement")
    public String CropSectionManagement() {
        return "admin/features/CropSectionManagement";
    }

    @GetMapping("/admin/features/AdminListBoard")
    public String AdminListBoard() {
        return "admin/features/AdminListBoard";
    }

    @GetMapping("/admin/features/ExpertConsultation")
    public String expertConsultation() {
        return "admin/features/ExpertConsultation";
    }

    //UserFeatures
    @GetMapping("/user/features/PlantManagement")
    public String PlantManagement() {
        return "user/features/PlantManagement";
    }

    @GetMapping("/user/features/SoilHealthMonitoring")
    public String SoilHealthMonitoring() {
        return "user/features/SoilHealthMonitoring";
    }

    /*@GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }*/

    @GetMapping("/user/features/Notification")
    public String Notification() {
        return "user/features/Notification";
    }

    @GetMapping("/user/features/weather")
    public String weather() {
        return "user/features/weather";
    }
    @PostMapping("/api/update-weather-toggle")
    @ResponseBody
    public Map<String, Object> updateWeatherToggle(@RequestBody Map<String, Integer> payload, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail != null) {
            int count = payload.get("count");
            weatherToggleCounts.put(userEmail, count);
            return Map.of("success", true, "message", "Weather toggle count updated", "count", count);
        }
        return Map.of("success", false, "message", "User not authenticated");
    }

    @GetMapping("/api/weather-toggle-count")
    @ResponseBody
    public Map<String, Object> getWeatherToggleCount(HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail != null) {
            int count = weatherToggleCounts.getOrDefault(userEmail, 0);
            return Map.of("success", true, "count", count);
        }
        return Map.of("success", false, "message", "User not authenticated", "count", 0);
    }

    @GetMapping("/user/features/ExpertConsultation")
    public String userExpertConsultation() {
        return "user/features/ExpertConsultation";
    }
}