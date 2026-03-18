package org.example.coursemanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.*;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.AuthResponse;
import org.example.coursemanagementsystem.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.refreshToken(request.getToken());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token.replace("Bearer ", ""));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/register/student")
    public ResponseEntity<ApiResponse<Void>> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        authService.registerStudent(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<ApiResponse<Void>> registerTeacher(@Valid @RequestBody TeacherRegistrationRequest request) {
        authService.registerTeacher(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtpAndSetPassword(@Valid @RequestBody OtpVerificationRequest request) {
        authService.verifyOtpAndSetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody PasswordUpdateRequest request,
                                                            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        Long userId = authService.getUserIdFromToken(jwt);
        authService.changePassword(userId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}