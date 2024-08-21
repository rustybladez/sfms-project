package com.JavaFest2024.xyz.service;

import com.JavaFest2024.xyz.model.Admin;
import com.JavaFest2024.xyz.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public String getAdminFullName(String email) {
        Admin admin = adminRepository.findByEmail(email);
        return admin != null ? admin.getFullName() : null;
    }

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public boolean authenticateAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null) {
            // In a real application, you should use a password encoder
            return admin.getPassword().equals(password);
        }
        return false;
    }

    // New methods for admin profile functionality

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    //ResetPassword
    public Admin findAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public boolean resetAdminPassword(String email, String newPassword) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null) {
            admin.setPassword(newPassword);
            adminRepository.save(admin);
            return true;
        }
        return false;
    }
    // admin list part code
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public boolean deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }
    // Add this new method is for Admin DashBoard Summery
    public long getTotalAdminCount() {
        return adminRepository.count();
    }
}