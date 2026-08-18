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
    private final com.hungnhan.school_management.repository.EnrollmentRepository enrollmentRepository;

    @Override
    public AdminDashboardResponse getDashboardStats() {
        long totalStudents = studentRepository.count();
        long totalTeachers = teacherRepository.count();
        long totalClasses = classSectionRepository.count();
        
        // Lấy tất cả sinh viên 1 lần để tối ưu
        List<com.hungnhan.school_management.entity.Student> allStudents = studentRepository.findAll();

        long lowGpaStudentsCount = allStudents.stream()
                .filter(s -> s.getGpa() != null && s.getGpa().compareTo(new BigDecimal("1.0")) < 0)
                .count();

        double attendanceRate = 85.5; 

        // Group teachers by department
        List<Map<String, Object>> teacherChartData = new ArrayList<>();
        departmentRepository.findAll().forEach(dept -> {
            long count = teacherRepository.findAll().stream()
                    .filter(t -> t.getDepartment().getId().equals(dept.getId()))
                    .count();
            if (count > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", dept.getName());
                map.put("shortName", getShortDeptName(dept.getName()));
                map.put("value", count);
                teacherChartData.add(map);
            }
        });

        // Group students by department
        List<Map<String, Object>> studentChartData = new ArrayList<>();
        departmentRepository.findAll().forEach(dept -> {
            long count = allStudents.stream()
                    .filter(s -> s.getMajor() != null && s.getMajor().getDepartment() != null && s.getMajor().getDepartment().getId().equals(dept.getId()))
                    .count();
            if (count > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", dept.getName());
                map.put("shortName", getShortDeptName(dept.getName()));
                map.put("value", count);
                studentChartData.add(map);
            }
        });

        // Calculate Grade Distribution
        List<Map<String, Object>> gradeDistributionData = new ArrayList<>();
        long xuatSac = 0, gioi = 0, kha = 0, tb = 0, yeu = 0;
        for (com.hungnhan.school_management.entity.Student s : allStudents) {
            if (s.getGpa() == null) continue;
            double gpa = s.getGpa().doubleValue();
            if (gpa >= 3.6) xuatSac++;
            else if (gpa >= 3.2) gioi++;
            else if (gpa >= 2.5) kha++;
            else if (gpa >= 2.0) tb++;
            else yeu++;
        }
        gradeDistributionData.add(Map.of("name", "Xuất sắc (3.60 - 4.00)", "value", xuatSac, "color", "#10B981"));
        gradeDistributionData.add(Map.of("name", "Giỏi (3.20 - 3.59)", "value", gioi, "color", "#22C55E"));
        gradeDistributionData.add(Map.of("name", "Khá (2.50 - 3.19)", "value", kha, "color", "#3B82F6"));
        gradeDistributionData.add(Map.of("name", "Trung bình (2.00 - 2.49)", "value", tb, "color", "#F59E0B"));
        gradeDistributionData.add(Map.of("name", "Yếu/Cảnh báo (< 2.00)", "value", yeu, "color", "#EF4444"));

        // Calculate Average Credit Completion Rate (Assuming 130 is full program)
        double totalCreditsSum = allStudents.stream().mapToDouble(s -> s.getTotalCredits() == null ? 0 : s.getTotalCredits()).sum();
        double averageCredits = totalStudents > 0 ? totalCreditsSum / totalStudents : 0;
        double averageCreditCompletionRate = totalStudents > 0 ? Math.min(100.0, (averageCredits / 130.0) * 100.0) : 0;

        return AdminDashboardResponse.builder()
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalClasses(totalClasses)
                .attendanceRate(attendanceRate)
                .lowGpaStudentsCount(lowGpaStudentsCount)
                .teacherChartData(teacherChartData)
                .studentChartData(studentChartData)
                .gradeDistributionData(gradeDistributionData)
                .averageCreditCompletionRate(averageCreditCompletionRate)
                .build();
    }

    private String getShortDeptName(String fullName) {
        if (fullName == null) return "Unknown";
        if (fullName.contains("Khoa học máy tính và Trí tuệ nhân tạo")) return "Khoa KHMT & TTNT";
        if (fullName.contains("Mạng máy tính và Truyền thông dữ liệu")) return "Khoa MMT & TTDL";
        if (fullName.contains("Công nghệ thông tin")) return "Khoa CNTT";
        if (fullName.contains("Công nghệ phần mềm")) return "Khoa CNPM";
        if (fullName.contains("An toàn thông tin")) return "Khoa ATTT";
        if (fullName.contains("Hệ thống thông tin")) return "Khoa HTTT";
        return fullName;
    }

    @Override
    public Map<String, Integer> getGradeDistribution(Long yearId, Long semesterId, Long classSectionId, Long departmentId, Long majorId) {
        List<Object[]> results = enrollmentRepository.getGradeDistributionCounts(yearId, semesterId, classSectionId, departmentId, majorId);
        Map<String, Integer> distribution = new HashMap<>();
        
        // Khởi tạo các giá trị mặc định để Frontend luôn có data
        distribution.put("A", 0);
        distribution.put("B+", 0);
        distribution.put("B", 0);
        distribution.put("C+", 0);
        distribution.put("C", 0);
        distribution.put("D+", 0);
        distribution.put("D", 0);
        distribution.put("F", 0);
        
        for (Object[] row : results) {
            String grade = (String) row[0];
            Long count = (Long) row[1];
            if (grade != null && distribution.containsKey(grade)) {
                distribution.put(grade, count.intValue());
            } else if (grade != null) {
                // In case there are other grade formats, map them to our format if possible, or just add them
                distribution.put(grade, count.intValue());
            }
        }
        
        return distribution;
    }
}
