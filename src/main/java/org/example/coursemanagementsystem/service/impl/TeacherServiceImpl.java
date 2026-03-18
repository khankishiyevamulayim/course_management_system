package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.TeacherProfileResponse;
import org.example.coursemanagementsystem.entity.Teacher;
import org.example.coursemanagementsystem.exception.UserNotFoundException;
import org.example.coursemanagementsystem.mapper.TeacherMapper;
import org.example.coursemanagementsystem.repository.TeacherRepository;
import org.example.coursemanagementsystem.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Override
    public TeacherProfileResponse getTeacherProfile(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new UserNotFoundException("Müəllim tapılmadı: " + teacherId));
        return teacherMapper.toProfileResponse(teacher);
    }

    @Override
    public List<TeacherProfileResponse> getTeachersBySpecialization(String specialization) {
        List<Teacher> teachers = teacherRepository.findAllBySpecializationIgnoreCase(specialization);
        return teachers.stream()
                .map(teacherMapper::toProfileResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherProfileResponse> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::toProfileResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTeacher(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new UserNotFoundException("Müəllim tapılmadı: " + teacherId));
        teacherRepository.delete(teacher);
    }
}