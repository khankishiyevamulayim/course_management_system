package org.example.coursemanagementsystem.controller;


import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.request.*;
import org.example.coursemanagementsystem.dto.response.AuthResponse;
import org.example.coursemanagementsystem.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token.replace("Bearer ", ""));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/student")
    public ResponseEntity<Void> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        authService.registerStudent(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<Void> registerTeacher(@Valid @RequestBody TeacherRegistrationRequest request) {
        authService.registerTeacher(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Void> verifyOtpAndSetPassword(@Valid @RequestBody OtpVerificationRequest request) {
        authService.verifyOtpAndSetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordUpdateRequest request,
                                               @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        Long userId = authService.getUserIdFromToken(jwt); // bu metodu AuthService-ə əlavə edə bilərsiniz
        authService.changePassword(userId, request);
        return ResponseEntity.ok().build();
    }
}
