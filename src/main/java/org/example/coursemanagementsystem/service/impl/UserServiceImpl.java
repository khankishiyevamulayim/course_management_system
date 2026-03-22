package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.UserBaseResponse;
import org.example.coursemanagementsystem.entity.User;
import org.example.coursemanagementsystem.exception.UserNotFoundException;
import org.example.coursemanagementsystem.mapper.UserMapper;
import org.example.coursemanagementsystem.repository.UserRepository;
import org.example.coursemanagementsystem.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserBaseResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + id));
        return userMapper.toBaseResponse(user);
    }

    @Override
    public UserBaseResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + email));
        return userMapper.toBaseResponse(user);
    }

    @Override
    public List<UserBaseResponse> getAllUsers() {
        return userMapper.toBaseResponseList(userRepository.findAll());
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
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + userId));
        user.setIsActive(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("İstifadəçi tapılmadı: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
