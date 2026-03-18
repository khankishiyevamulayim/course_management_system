package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.request.LoginRequest;
import org.example.coursemanagementsystem.dto.request.PasswordUpdateRequest;
import org.example.coursemanagementsystem.dto.request.StudentRegistrationRequest;
import org.example.coursemanagementsystem.dto.request.TeacherRegistrationRequest;
import org.example.coursemanagementsystem.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
    void registerStudent(StudentRegistrationRequest request);
    void registerTeacher(TeacherRegistrationRequest request);
    void verifyOtpAndSetPassword(String email, String otp, String newPassword);
    void changePassword(Long userId, PasswordUpdateRequest request);
    Long getUserIdFromToken(String token);
}
