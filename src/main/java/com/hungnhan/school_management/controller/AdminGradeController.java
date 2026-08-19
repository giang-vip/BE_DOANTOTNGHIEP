package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.response.FinalGradeResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.TeacherGradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/classes/{classSectionId}/grades")
@RequiredArgsConstructor
@Tag(name = "Admin - Final Grade", description = "API quản lý điểm dành cho Admin (Bỏ qua Role Giảng Viên)")
public class AdminGradeController {

    private final TeacherGradeService teacherGradeService;

    @GetMapping
    @Operation(summary = "Xem điểm tổng kết của lớp (Admin)")
    public ApiResponse<PageResponse<FinalGradeResponse>> getFinalGrades(
            @PathVariable Long classSectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "500") int size
    ) {
        return ApiResponse.<PageResponse<FinalGradeResponse>>builder()
                .result(teacherGradeService.getFinalGradesForAdmin(classSectionId, page, size))
                .build();
    }

    @PutMapping
    @Operation(summary = "Nhập/Cập nhật điểm cho sinh viên (Admin)")
    public ApiResponse<String> updateStudentGrades(
            @PathVariable Long classSectionId,
            @RequestBody @Valid java.util.List<com.hungnhan.school_management.dto.request.TeacherGradeUpdateRequest> requests
    ) {
        teacherGradeService.updateStudentGradesForAdmin(classSectionId, requests);
        return ApiResponse.<String>builder()
                .result("Cập nhật điểm thành công")
                .build();
    }
}
