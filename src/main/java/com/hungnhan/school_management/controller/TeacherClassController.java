package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.EnrollmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.TeacherClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher/classes")
@RequiredArgsConstructor
@Tag(name = "Teacher - Class Management", description = "API quản lý lớp giảng dạy dành cho Giảng viên (Phase 3)")
public class TeacherClassController {

    private final TeacherClassService teacherClassService;

    @GetMapping
    @Operation(summary = "Danh sách lớp học phần giảng dạy (API_TC_01)")
    public ApiResponse<PageResponse<ClassSectionResponse>> getTeacherClassSections(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<ClassSectionResponse>>builder()
                .result(teacherClassService.getTeacherClassSections(username, search, semesterId, page, size))
                .build();
    }

    @GetMapping("/{classSectionId}/students")
    @Operation(summary = "Danh sách sinh viên trong lớp học phần (API_TC_02)")
    public ApiResponse<PageResponse<EnrollmentResponse>> getStudentsInClassSection(
            @PathVariable Long classSectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<EnrollmentResponse>>builder()
                .result(teacherClassService.getStudentsInClassSection(username, classSectionId, page, size))
                .build();
    }
}
