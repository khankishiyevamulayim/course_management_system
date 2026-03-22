package org.example.coursemanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.TeacherProfileResponse;
import org.example.coursemanagementsystem.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/profile/{teacherId}")
    @PreAuthorize("(hasRole('TEACHER') and #teacherId == principal) or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherProfileResponse>> getTeacherProfile(
            @PathVariable Long teacherId) {
        return ResponseEntity.ok(ApiResponse.success(
                teacherService.getTeacherProfile(teacherId)));
    }

    @GetMapping("/specialization/{specialization}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<TeacherProfileResponse>>> getTeachersBySpecialization(
            @PathVariable String specialization) {
        return ResponseEntity.ok(ApiResponse.success(
                teacherService.getTeachersBySpecialization(specialization)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TeacherProfileResponse>>> getAllTeachers() {
        return ResponseEntity.ok(ApiResponse.success(teacherService.getAllTeachers()));
    }

    @DeleteMapping("/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable Long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}