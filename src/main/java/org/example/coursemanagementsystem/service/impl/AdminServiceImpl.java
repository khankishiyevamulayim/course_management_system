package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.DashboardStats;
import org.example.coursemanagementsystem.dto.response.UserBaseResponse;
import org.example.coursemanagementsystem.entity.User;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserMapper userMapper;

    @Override
    public DashboardStats getDashboardStats() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByIsActiveTrue();
        long inactiveUsers = totalUsers - activeUsers;
        long totalStudents = studentRepository.count();
        long totalTeachers = teacherRepository.count();
        long totalAdmins = userRepository.countByRole(Role.ADMIN);

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
        return userRepository.findAll()
                .stream()
                .map(userMapper::toBaseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserBaseResponse> getActiveUsers() {
        return userRepository.findByIsActiveTrue()
                .stream()
                .map(userMapper::toBaseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserBaseResponse> getInactiveUsers() {
        return userRepository.findByIsActiveFalse()
                .stream()
                .map(userMapper::toBaseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserBaseResponse> getUsersByRole(String role) {
        Role roleEnum = Role.valueOf(role.toUpperCase());
        return userRepository.findByRole(roleEnum)
                .stream()
                .map(userMapper::toBaseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + userId));

        // Əgər student və ya teacher əlaqəli obyektləri varsa, onları da silmək olar
        // Burada sadəcə user-i silirik. Əlaqəli entity-lər cascade ilə silinə bilər.
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + userId));
        user.setActive(true);
        // save çağırmağa ehtiyac yoxdur, @Transactional avtomatik flush edir
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + userId));
        user.setActive(false);
    }

    @Override
    @Transactional
    public void changeUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + userId));

        Role roleEnum = Role.valueOf(newRole.toUpperCase());
        user.setRole(roleEnum);
    }
}