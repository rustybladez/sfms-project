package com.JavaFest2024.xyz.service;

import com.JavaFest2024.xyz.model.User;
import com.JavaFest2024.xyz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

@Service
public class UserServiceImpl extends UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // In a real application, you should use a password encoder
            return user.getPassword().equals(password);
        }
        return false;
    }
}