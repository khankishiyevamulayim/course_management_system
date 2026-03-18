package org.example.coursemanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.StudentProfileResponse;
import org.example.coursemanagementsystem.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/profile/{studentId}")
    @PreAuthorize("hasRole('STUDENT') and #studentId == principal.id or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> getStudentProfile(@PathVariable Long studentId) {
        StudentProfileResponse response = studentService.getStudentProfile(studentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/number/{studentNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> getStudentByNumber(@PathVariable String studentNumber) {
        StudentProfileResponse response = studentService.getStudentByNumber(studentNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StudentProfileResponse>>> getAllStudents() {
        List<StudentProfileResponse> response = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}