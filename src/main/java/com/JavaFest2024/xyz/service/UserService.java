package com.JavaFest2024.xyz.service;

import com.JavaFest2024.xyz.model.User;
import com.JavaFest2024.xyz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // New field for OTP storage
    private Map<String, OtpData> otpStore = new HashMap<>();

    // Existing methods

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }

    public User updateUser(User updatedUser) {
        User existingUser = userRepository.findByEmail(updatedUser.getEmail());
        if (existingUser != null) {
            existingUser.setFullName(updatedUser.getFullName());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            // Don't update email as it's used as an identifier
            // Only update password if a new one is provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            return userRepository.save(existingUser);
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setFullName(updatedUser.getFullName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            return userRepository.save(existingUser);
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long getTotalUserCount() {
        return userRepository.count();
    }

    // New methods for OTP functionality

    public String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void storeOTP(String email, String otp) {
        otpStore.put(email, new OtpData(otp, System.currentTimeMillis()));
    }

    public boolean verifyOTP(String email, String otp) {
        OtpData storedOtpData = otpStore.get(email);
        if (storedOtpData != null) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - storedOtpData.timestamp <= 60000) { // 60 seconds validity
                boolean isValid = storedOtpData.otp.equals(otp);
                if (isValid) {
                    otpStore.remove(email); // Remove OTP after successful verification
                }
                return isValid;
            } else {
                otpStore.remove(email); // Remove expired OTP
            }
        }
        return false;
    }

    // Inner class to store OTP data
    private static class OtpData {
        String otp;
        long timestamp;

        OtpData(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }
    }

    //Login with google
    public User processOAuthPostLogin(String email, String name) {
        User existUser = userRepository.findByEmail(email);
        if (existUser == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(name);
            newUser.setPassword("12345"); // Default password
            newUser.setPhoneNumber("Add Phone"); // You might want to set this later

            User savedUser = userRepository.save(newUser);

            // Send email with temporary password
            String subject = "Welcome to SFMS - Your Temporary Password";
            String body = "Dear " + name + ",\n\n"
                    + "Welcome to SFMS! Your account has been created with Google Sign-In.\n"
                    + "Your temporary password is: 12345\n"
                    + "Please log in and change your password as soon as possible.\n\n"
                    + "Best regards,\nSFMS Team";
            emailService.sendSimpleMessage(email, subject, body);

            return savedUser;
        }
        return existUser;
    }
}