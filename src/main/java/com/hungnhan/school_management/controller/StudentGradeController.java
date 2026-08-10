package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.StudentGradeResponse;
import com.hungnhan.school_management.service.StudentGradeService;
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
@Tag(name = "Student - Grades", description = "API Xem bảng điểm tổng hợp dành cho Sinh viên (Phase 4)")
public class StudentGradeController {

    private final StudentGradeService studentGradeService;

    @GetMapping("/grades")
    @Operation(summary = "Xem bảng điểm tổng hợp (API_ST_12 -> 14)")
    public ApiResponse<PageResponse<StudentGradeResponse>> getMyGrades(
            @RequestParam(required = false) Long semesterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<StudentGradeResponse>>builder()
                .result(studentGradeService.getMyGrades(username, semesterId, page, size))
                .build();
    }
}
