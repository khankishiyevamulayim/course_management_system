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
    @PreAuthorize("hasRole('TEACHER') and #teacherId == principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherProfileResponse>> getTeacherProfile(@PathVariable Long teacherId) {
        TeacherProfileResponse response = teacherService.getTeacherProfile(teacherId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/specialization/{specialization}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TeacherProfileResponse>>> getTeachersBySpecialization(@PathVariable String specialization) {
        List<TeacherProfileResponse> response = teacherService.getTeachersBySpecialization(specialization);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TeacherProfileResponse>>> getAllTeachers() {
        List<TeacherProfileResponse> response = teacherService.getAllTeachers();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable Long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}