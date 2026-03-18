package org.example.coursemanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Şifrə yeniləmə OTP kodu");
        message.setText("OTP kodunuz: " + otp + "\nBu kod 5 dəqiqə etibarlıdır.");
        mailSender.send(message);
    }

    public void sendWelcomeEmail(String to, String name) {
        // Hoş geldiniz emaili
    }
}
