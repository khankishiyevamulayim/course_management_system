package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.response.UserBaseResponse;
import java.util.List;

public interface UserService {
    UserBaseResponse getUserById(Long id);
    UserBaseResponse getUserByEmail(String email);
    List<UserBaseResponse> getAllUsers();
    void deactivateUser(Long userId);
    void activateUser(Long userId);
    void deleteUser(Long userId);
}