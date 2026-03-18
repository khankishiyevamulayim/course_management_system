package org.example.coursemanagementsystem.service;

import org.example.coursemanagementsystem.dto.response.TeacherProfileResponse;

import java.util.List;

public interface TeacherService {

    TeacherProfileResponse getTeacherProfile(Long teacherId);

    List<TeacherProfileResponse> getTeachersBySpecialization(String specialization);

    List<TeacherProfileResponse> getAllTeachers();

    void deleteTeacher(Long teacherId);
}