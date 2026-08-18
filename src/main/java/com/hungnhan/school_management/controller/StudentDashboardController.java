package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.response.AnnouncementResponse;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.RegistrationPeriodResponse;
import com.hungnhan.school_management.dto.response.EnrollmentResponse;
import com.hungnhan.school_management.service.StudentDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.hungnhan.school_management.repository.ClassSectionRepository;
import com.hungnhan.school_management.entity.ClassSection;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Tag(name = "Student - Dashboard", description = "API Dashboard dành cho Sinh viên (Phase 4)")
public class StudentDashboardController {

    private final StudentDashboardService studentDashboardService;
    private final ClassSectionRepository classSectionRepository;

    @GetMapping("/debug-classes")
    public java.util.Map<String, Object> debugClasses() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        List<ClassSection> classes = classSectionRepository.findAll();
        for (ClassSection c : classes) {
            if (c.getId() == 2014 || c.getId() == 2015) {
                Long dId = c.getDepartment() != null ? c.getDepartment().getId() : null;
                Long mId = c.getMajor() != null ? c.getMajor().getId() : null;
                result.put("class_" + c.getId(), "Subject: " + c.getSubject().getId() + " Dept: " + dId + " Major: " + mId);
            }
        }
        return result;
    }

    @GetMapping("/classes")
    @Operation(summary = "Lịch học (Danh sách lớp học phần sinh viên tham gia) (API_ST_01)")
    public ApiResponse<PageResponse<ClassSectionResponse>> getStudentClasses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<ClassSectionResponse>>builder()
                .result(studentDashboardService.getStudentClasses(username, page, size))
                .build();
    }

    @GetMapping("/announcements")
    @Operation(summary = "Danh sách thông báo lớp học (API_ST_02)")
    public ApiResponse<PageResponse<AnnouncementResponse>> getStudentAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<AnnouncementResponse>>builder()
                .result(studentDashboardService.getStudentAnnouncements(username, page, size))
                .build();
    }

    @GetMapping("/registration/period")
    @Operation(summary = "Lấy kỳ đăng ký học phần hiện tại")
    public ApiResponse<RegistrationPeriodResponse> getCurrentPeriod() {
        return ApiResponse.<RegistrationPeriodResponse>builder()
                .result(studentDashboardService.getCurrentRegistrationPeriod())
                .build();
    }

    @GetMapping("/registration/curriculum")
    @Operation(summary = "Lấy danh sách khung chương trình (danh sách môn học) của sinh viên")
    public ApiResponse<List<com.hungnhan.school_management.dto.response.StudentCurriculumResponse>> getStudentCurriculum() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<List<com.hungnhan.school_management.dto.response.StudentCurriculumResponse>>builder()
                .result(studentDashboardService.getStudentCurriculum(username))
                .build();
    }

    @GetMapping("/registration/classes")
    @Operation(summary = "Xem danh sách học phần mở đăng ký phù hợp với ngành khoa")
    public ApiResponse<PageResponse<ClassSectionResponse>> getAvailableClasses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<ClassSectionResponse>>builder()
                .result(studentDashboardService.getAvailableClasses(username, search, semesterId, page, size))
                .build();
    }

    @PostMapping("/registration/enroll")
    @Operation(summary = "Sinh viên đăng ký lớp học phần")
    public ApiResponse<EnrollmentResponse> enrollClass(@RequestParam Long classSectionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<EnrollmentResponse>builder()
                .result(studentDashboardService.enrollClass(username, classSectionId))
                .build();
    }

    @DeleteMapping("/registration/drop/{classSectionId}")
    @Operation(summary = "Sinh viên hủy đăng ký lớp học phần")
    public ApiResponse<String> dropClass(@PathVariable Long classSectionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        studentDashboardService.dropClass(username, classSectionId);
        return ApiResponse.<String>builder()
                .result("Hủy đăng ký học phần thành công")
                .build();
    }
}
