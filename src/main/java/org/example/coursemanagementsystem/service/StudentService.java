package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.response.StudentProfileResponse;

import java.util.List;

public interface StudentService {

    StudentProfileResponse getStudentProfile(Long studentId);

    StudentProfileResponse getStudentByNumber(String studentNumber);

    List<StudentProfileResponse> getAllStudents();

    void deleteStudent(Long studentId);
}
