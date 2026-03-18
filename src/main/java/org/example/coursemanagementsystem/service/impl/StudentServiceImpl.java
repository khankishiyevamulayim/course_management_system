package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.StudentProfileResponse;
import org.example.coursemanagementsystem.entity.Student;
import org.example.coursemanagementsystem.exception.UserNotFoundException;
import org.example.coursemanagementsystem.mapper.StudentMapper;
import org.example.coursemanagementsystem.repository.StudentRepository;
import org.example.coursemanagementsystem.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentProfileResponse getStudentProfile(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("Tələbə tapılmadı: " + studentId));
        return studentMapper.toProfileResponse(student);
    }

    @Override
    public StudentProfileResponse getStudentByNumber(String studentNumber) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new UserNotFoundException("Tələbə nömrəsi tapılmadı: " + studentNumber));
        return studentMapper.toProfileResponse(student);
    }

    @Override
    public List<StudentProfileResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toProfileResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("Tələbə tapılmadı: " + studentId));
        studentRepository.delete(student);
    }
}
