package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.TeacherProfileResponse;
import org.example.coursemanagementsystem.entity.Teacher;
import org.example.coursemanagementsystem.exception.TeacherNotFoundException;
import org.example.coursemanagementsystem.mapper.TeacherMapper;
import org.example.coursemanagementsystem.repository.TeacherRepository;
import org.example.coursemanagementsystem.repository.UserRepository;
import org.example.coursemanagementsystem.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final UserRepository userRepository;

    @Override
    public TeacherProfileResponse getTeacherProfile(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException("Müəllim tapılmadı: " + teacherId));
        return teacherMapper.toProfileResponse(teacher);
    }

    @Override
    public List<TeacherProfileResponse> getTeachersBySpecialization(String specialization) {

        return teacherRepository.findAllBySpecializationIgnoreCase(specialization).stream()
                .map(teacherMapper::toProfileResponse)
                .toList();
    }

    @Override
    public List<TeacherProfileResponse> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toProfileResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteTeacher(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException("Müəllim tapılmadı: " + teacherId));

        teacherRepository.delete(teacher);
        userRepository.delete(teacher.getUser());
    }
}
