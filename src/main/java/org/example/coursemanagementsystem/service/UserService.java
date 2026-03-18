package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.request.PasswordUpdateRequest;
import org.example.coursemanagementsystem.dto.response.StudentProfileResponse;
import org.example.coursemanagementsystem.dto.response.TeacherProfileResponse;
import org.example.coursemanagementsystem.dto.response.UserBaseResponse;

import java.util.List;

public interface UserService {

    UserBaseResponse getUserById(Long id);
    UserBaseResponse getUserByEmail(String email);
    List<UserBaseResponse> getAllUsers();
    void updatePassword(Long userId, PasswordUpdateRequest request);
    void deactivateUser(Long userId);
    void activateUser(Long userId);
    void deleteUser(Long userId);
    StudentProfileResponse getStudentProfile(Long studentId);
    TeacherProfileResponse getTeacherProfile(Long teacherId);

}
