package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.GradeConfigRequest;
import com.hungnhan.school_management.dto.response.FinalGradeResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.TeacherGradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@Tag(name = "Teacher - Final Grade", description = "API thiết lập và xem điểm tổng kết dành cho Giảng viên (Phase 3)")
public class TeacherGradeController {

    private final TeacherGradeService teacherGradeService;

    @PostMapping("/classes/{classSectionId}/grade-config")
    @Operation(summary = "Thiết lập công thức điểm (API_TC_19)")
    public ApiResponse<String> configureGradeWeights(
            @PathVariable Long classSectionId,
            @RequestBody @Valid GradeConfigRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        teacherGradeService.configureGradeWeights(username, classSectionId, request);
        return ApiResponse.<String>builder()
                .result("Cấu hình điểm thành công")
                .build();
    }

    @GetMapping("/classes/{classSectionId}/final-grades")
    @Operation(summary = "Xem điểm tổng kết của lớp (API_TC_20)")
    public ApiResponse<PageResponse<FinalGradeResponse>> getFinalGrades(
            @PathVariable Long classSectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<FinalGradeResponse>>builder()
                .result(teacherGradeService.getFinalGrades(username, classSectionId, page, size))
                .build();
    }
}
