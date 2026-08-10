package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.response.AdminDashboardResponse;
import com.hungnhan.school_management.repository.ClassSectionRepository;
import com.hungnhan.school_management.repository.StudentRepository;
import com.hungnhan.school_management.repository.TeacherRepository;
import com.hungnhan.school_management.repository.DepartmentRepository;
import com.hungnhan.school_management.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ClassSectionRepository classSectionRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public AdminDashboardResponse getDashboardStats() {
        long totalStudents = studentRepository.count();
        long totalTeachers = teacherRepository.count();
        long totalClasses = classSectionRepository.count();
        
        // Count students with GPA < 1.0 (assuming GPA is stored in Student entity)
        // Since we just added it, we can filter using stream for now if there is no custom query
        long lowGpaStudentsCount = studentRepository.findAll().stream()
                .filter(s -> s.getGpa() != null && s.getGpa().compareTo(new BigDecimal("1.0")) < 0)
                .count();

        // Calculate a dummy attendance rate for now, or fetch actual from DB
        double attendanceRate = 85.5; 

        // Generate dummy charts for now (or query from DB)
        List<Map<String, Object>> attendanceChartData = new ArrayList<Map<String, Object>>() {
            {
                add(Map.of("name", "LHP001", "rate", 92));
                add(Map.of("name", "LHP002", "rate", 78));
                add(Map.of("name", "LHP003", "rate", 85));
            }
        };

        // Group teachers by department
        List<Map<String, Object>> teacherChartData = new ArrayList<>();
        departmentRepository.findAll().forEach(dept -> {
            long count = teacherRepository.findAll().stream()
                    .filter(t -> t.getDepartment().getId().equals(dept.getId()))
                    .count();
            if (count > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", dept.getName());
                map.put("value", count);
                teacherChartData.add(map);
            }
        });

        return AdminDashboardResponse.builder()
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalClasses(totalClasses)
                .attendanceRate(attendanceRate)
                .lowGpaStudentsCount(lowGpaStudentsCount)
                .attendanceChartData(attendanceChartData)
                .teacherChartData(teacherChartData)
                .build();
    }
}
