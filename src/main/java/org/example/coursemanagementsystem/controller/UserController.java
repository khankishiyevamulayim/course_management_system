package org.example.coursemanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.UserBaseResponse;
import org.example.coursemanagementsystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #id == principal.id) or (hasRole('TEACHER') and #id == principal.id)")
    public ResponseEntity<ApiResponse<UserBaseResponse>> getUserById(@PathVariable Long id) {
        UserBaseResponse response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserBaseResponse>> getUserByEmail(@PathVariable String email) {
        UserBaseResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserBaseResponse>>> getAllUsers() {
        List<UserBaseResponse> response = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}