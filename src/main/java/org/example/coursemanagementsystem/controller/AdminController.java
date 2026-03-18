package org.example.coursemanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.DashboardStats;
import org.example.coursemanagementsystem.dto.response.UserBaseResponse;
import org.example.coursemanagementsystem.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getDashboardStats()));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserBaseResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getAllUsers()));
    }

    @GetMapping("/users/active")
    public ResponseEntity<ApiResponse<List<UserBaseResponse>>> getActiveUsers() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getActiveUsers()));
    }

    @GetMapping("/users/inactive")
    public ResponseEntity<ApiResponse<List<UserBaseResponse>>> getInactiveUsers() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getInactiveUsers()));
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<ApiResponse<List<UserBaseResponse>>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(ApiResponse.success(adminService.getUsersByRole(role)));
    }

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Long id) {
        adminService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Long id) {
        adminService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<Void>> changeUserRole(@PathVariable Long id, @RequestParam String role) {
        adminService.changeUserRole(id, role);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}