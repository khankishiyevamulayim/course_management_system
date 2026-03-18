package org.example.coursemanagementsystem.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DashboardStats {
    private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
    private long totalStudents;
    private long totalTeachers;
    private long totalAdmins;
}