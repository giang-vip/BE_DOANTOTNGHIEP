package com.hungnhan.school_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private long totalStudents;
    private long totalTeachers;
    private long totalClasses;
    private double attendanceRate;
    private long lowGpaStudentsCount;
    private List<Map<String, Object>> teacherChartData;
    private List<Map<String, Object>> studentChartData;
    private List<Map<String, Object>> gradeDistributionData;
    private double averageCreditCompletionRate;
}
