package org.example.coursemanagementsystem.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.*;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.AuthResponse;
import org.example.coursemanagementsystem.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                authService.refreshToken(request.getToken())));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Authorization header düzgün deyil"));
        }
        authService.logout(authHeader.substring(7));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/register/student")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> registerStudent(
            @Valid @RequestBody StudentRegistrationRequest request) {
        authService.registerStudent(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/register/teacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> registerTeacher(
            @Valid @RequestBody TeacherRegistrationRequest request) {
        authService.registerTeacher(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtpAndSetPassword(
            @Valid @RequestBody OtpVerificationRequest request) {
        authService.verifyOtpAndSetPassword(
                request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody PasswordUpdateRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        authService.changePassword(userId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @RequestParam @NotBlank @Email String email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(
                request.getToken(),
                request.getNewPassword(),
                request.getConfirmPassword());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}