package com.JavaFest2024.xyz.service;

import com.JavaFest2024.xyz.model.Admin;
import com.JavaFest2024.xyz.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public boolean authenticateAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        return admin != null && admin.getPassword().equals(password);
    }

    @Override
    public String getAdminFullName(String email) {
        Admin admin = adminRepository.findByEmail(email);
        return admin != null ? admin.getFullName() : null;
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
}