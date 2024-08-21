package com.JavaFest2024.xyz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private Map<String, OtpData> otpStore = new HashMap<>();

    // Existing methods
    public boolean sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendOTP(String to, String otp) {
        String subject = "Password Reset OTP";
        String content = "Your OTP for password reset is: " + otp + ". This OTP will expire in 1 minute.";
        boolean sent = sendEmail(to, subject, content);
        if (sent) {
            storeOTP(to, otp);
        }
        return sent;
    }

    // New OTP-related methods
    public String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void storeOTP(String email, String otp) {
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
    // Add this new method
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupExpiredOTPs() {
        long currentTime = System.currentTimeMillis();
        otpStore.entrySet().removeIf(entry -> currentTime - entry.getValue().timestamp > 60000); // 1 minutes
    }

    // For admin otp

}