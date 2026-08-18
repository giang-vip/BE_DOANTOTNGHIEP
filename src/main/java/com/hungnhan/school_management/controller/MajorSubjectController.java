package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.MajorSubjectRequest;
import com.hungnhan.school_management.dto.response.SubjectResponse;
import com.hungnhan.school_management.service.MajorSubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/majors/{majorId}/subjects")
@RequiredArgsConstructor
@Tag(name = "Major Subject Management", description = "API quản lý Khung chương trình của Ngành (Dành cho ADMIN)")
public class MajorSubjectController {

    private final MajorSubjectService majorSubjectService;

    @PostMapping
    @Operation(summary = "Thêm môn học vào khung chương trình của ngành")
    public ApiResponse<SubjectResponse> addSubjectToMajor(
            @PathVariable Long majorId, 
            @RequestBody @Valid MajorSubjectRequest request) {
        return ApiResponse.<SubjectResponse>builder()
                .result(majorSubjectService.addSubjectToMajor(majorId, request))
                .build();
    }

    @PutMapping("/{subjectId}")
    @Operation(summary = "Cập nhật kỳ học hoặc loại môn học trong khung chương trình")
    public ApiResponse<SubjectResponse> updateSubjectInMajor(
            @PathVariable Long majorId,
            @PathVariable Long subjectId,
            @RequestBody @Valid MajorSubjectRequest request) {
        return ApiResponse.<SubjectResponse>builder()
                .result(majorSubjectService.updateSubjectInMajor(majorId, subjectId, request))
                .build();
    }

    @DeleteMapping("/{subjectId}")
    @Operation(summary = "Xóa môn học khỏi khung chương trình của ngành")
    public ApiResponse<Void> removeSubjectFromMajor(
            @PathVariable Long majorId, 
            @PathVariable Long subjectId) {
        majorSubjectService.removeSubjectFromMajor(majorId, subjectId);
        return ApiResponse.<Void>builder().build();
    }
}
