package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.response.DashboardStats;
import org.example.coursemanagementsystem.dto.response.UserBaseResponse;

import java.util.List;

public interface AdminService {
    DashboardStats getDashboardStats();
    List<UserBaseResponse> getAllUsers();
    List<UserBaseResponse> getActiveUsers();
    List<UserBaseResponse> getInactiveUsers();
    List<UserBaseResponse> getUsersByRole(String role);
    void deleteUser(Long userId);
    void activateUser(Long userId);
    void deactivateUser(Long userId);
    void changeUserRole(Long userId, String newRole);
}
