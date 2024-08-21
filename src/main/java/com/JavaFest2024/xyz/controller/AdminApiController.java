package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.model.Admin;
import com.JavaFest2024.xyz.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminApiController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admins")
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @PostMapping("/admins")
    public Admin addAdmin(@RequestBody Admin admin) {
        return adminService.saveAdmin(admin);
    }

    @DeleteMapping("/admins/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        boolean deleted = adminService.deleteAdmin(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // this new endpoint is for AdminDashBoard Summery
    @GetMapping("/admins/count")
    public ResponseEntity<Long> getAdminCount() {
        long count = adminService.getTotalAdminCount();
        return ResponseEntity.ok(count);
    }
}