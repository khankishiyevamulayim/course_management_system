package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.StudentProfileResponse;
import org.example.coursemanagementsystem.entity.Student;
import org.example.coursemanagementsystem.exception.StudentNotFoundException;
import org.example.coursemanagementsystem.mapper.StudentMapper;
import org.example.coursemanagementsystem.repository.StudentRepository;
import org.example.coursemanagementsystem.repository.UserRepository;
import org.example.coursemanagementsystem.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserRepository userRepository;

    @Override
    public StudentProfileResponse getStudentProfile(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Tələbə tapılmadı: " + studentId));
        return studentMapper.toProfileResponse(student);
    }

    @Override
    public StudentProfileResponse getStudentByNumber(String studentNumber) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Tələbə nömrəsi tapılmadı: " + studentNumber));
        return studentMapper.toProfileResponse(student);
    }

    @Override
    public List<StudentProfileResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toProfileResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Tələbə tapılmadı: " + studentId));

        studentRepository.delete(student);
        userRepository.delete(student.getUser());
    }
}
