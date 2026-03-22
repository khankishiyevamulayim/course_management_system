package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.request.*;
import org.example.coursemanagementsystem.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(String token);
    void registerStudent(StudentRegistrationRequest request);
    void registerTeacher(TeacherRegistrationRequest request);
    void verifyOtpAndSetPassword(String email, String otp, String newPassword);
    void changePassword(Long userId, PasswordUpdateRequest request);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword, String confirmPassword);
}