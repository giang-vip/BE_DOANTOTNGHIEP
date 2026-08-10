package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.response.AttendanceRecordResponse;
import com.hungnhan.school_management.dto.response.LearningMaterialResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.StudentClassResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/classes/{classSectionId}")
@RequiredArgsConstructor
@Tag(name = "Student - Class Resources", description = "API Điểm danh & Học liệu dành cho Sinh viên (Phase 4)")
public class StudentClassResourceController {

    private final StudentClassResourceService studentClassResourceService;

    @GetMapping("/attendance")
    @Operation(summary = "Xem lịch sử điểm danh của sinh viên trong lớp (API_ST_03)")
    public ApiResponse<List<AttendanceRecordResponse>> getMyAttendance(@PathVariable Long classSectionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<List<AttendanceRecordResponse>>builder()
                .result(studentClassResourceService.getMyAttendance(username, classSectionId))
                .build();
    }

    @GetMapping("/materials")
    @Operation(summary = "Danh sách học liệu của lớp (API_ST_04 & API_ST_05)")
    public ApiResponse<PageResponse<LearningMaterialResponse>> getMyMaterials(
            @PathVariable Long classSectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<LearningMaterialResponse>>builder()
                .result(studentClassResourceService.getMyMaterials(username, classSectionId, page, size))
                .build();
    }
}
