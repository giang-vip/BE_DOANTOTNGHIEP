package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.response.AnnouncementResponse;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.StudentDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Tag(name = "Student - Dashboard", description = "API Dashboard dành cho Sinh viên (Phase 4)")
public class StudentDashboardController {

    private final StudentDashboardService studentDashboardService;

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
}
