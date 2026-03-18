package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.response.DashboardStats;
import org.example.coursemanagementsystem.dto.response.UserBaseResponse;

import java.util.List;

public interface AdminService {

    // Dashboard statistikası
    DashboardStats getDashboardStats();

    // Bütün istifadəçiləri siyahıla
    List<UserBaseResponse> getAllUsers();

    // Aktiv istifadəçilər
    List<UserBaseResponse> getActiveUsers();

    // Aktiv olmayan istifadəçilər
    List<UserBaseResponse> getInactiveUsers();

    // Rol'a görə istifadəçilər (STUDENT, TEACHER, ADMIN)
    List<UserBaseResponse> getUsersByRole(String role);

    // İstifadəçini sil (fiziki silmə)
    void deleteUser(Long userId);

    // İstifadəçini aktiv et
    void activateUser(Long userId);

    // İstifadəçini deaktiv et
    void deactivateUser(Long userId);

    // İstifadəçi rolunu dəyiş (ehtiyatlı ol!)
    void changeUserRole(Long userId, String newRole);
}