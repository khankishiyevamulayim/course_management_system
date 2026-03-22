package org.example.coursemanagementsystem.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DashboardStats {
    long totalUsers;
    long activeUsers;
    long inactiveUsers;
    long totalStudents;
    long totalTeachers;
    long totalAdmins;
}