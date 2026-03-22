package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.DashboardStats;
import org.example.coursemanagementsystem.dto.response.UserBaseResponse;
import org.example.coursemanagementsystem.entity.User;
import org.example.coursemanagementsystem.exception.InvalidRoleException;
import org.example.coursemanagementsystem.exception.UserNotFoundException;
import org.example.coursemanagementsystem.mapper.UserMapper;
import org.example.coursemanagementsystem.repository.StudentRepository;
import org.example.coursemanagementsystem.repository.TeacherRepository;
import org.example.coursemanagementsystem.repository.UserRepository;
import org.example.coursemanagementsystem.service.AdminService;
import org.example.coursemanagementsystem.util.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserMapper userMapper;

    @Override
    public DashboardStats getDashboardStats() {
        long totalUsers    = userRepository.count();
        long activeUsers   = userRepository.countByIsActiveTrue();
        long inactiveUsers = totalUsers - activeUsers;
        long totalStudents = studentRepository.count();
        long totalTeachers = teacherRepository.count();
        long totalAdmins   = userRepository.countByRole(Role.ADMIN);

        return DashboardStats.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalAdmins(totalAdmins)
                .build();
    }

    @Override
    public List<UserBaseResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toBaseResponse)
                .toList();
    }

    @Override
    public List<UserBaseResponse> getActiveUsers() {
        return userRepository.findByIsActiveTrue().stream()
                .map(userMapper::toBaseResponse)
                .toList();
    }

    @Override
    public List<UserBaseResponse> getInactiveUsers() {
        return userRepository.findByIsActiveFalse().stream()
                .map(userMapper::toBaseResponse)
                .toList();
    }

    @Override
    public List<UserBaseResponse> getUsersByRole(String role) {
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Yanlış rol: " + role);
        }
        return userRepository.findByRole(roleEnum).stream()
                .map(userMapper::toBaseResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + userId));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + userId));
        user.setIsActive(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + userId));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + userId));

        Role roleEnum;
        try {
            roleEnum = Role.valueOf(newRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Yanlış rol: " + newRole);
        }
        user.setRole(roleEnum);
        userRepository.save(user);
    }
}

