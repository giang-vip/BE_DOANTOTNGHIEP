package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.SubjectRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.SubjectResponse;
import com.hungnhan.school_management.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/subjects")
@RequiredArgsConstructor
@Tag(name = "Subject Management", description = "API quản lý Môn học (Dành cho ADMIN)")
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    @Operation(summary = "Tạo mới môn học (API_AD_22)")
    public ApiResponse<SubjectResponse> createSubject(@RequestBody @Valid SubjectRequest request) {
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.createSubject(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Danh sách môn học (API_AD_21)")
    public ApiResponse<PageResponse<SubjectResponse>> getSubjects(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<SubjectResponse>>builder()
                .result(subjectService.getSubjects(search, departmentId, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết môn học")
    public ApiResponse<SubjectResponse> getSubjectById(@PathVariable Long id) {
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.getSubjectById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật môn học (API_AD_22)")
    public ApiResponse<SubjectResponse> updateSubject(@PathVariable Long id, @RequestBody @Valid SubjectRequest request) {
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.updateSubject(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa môn học")
    public ApiResponse<String> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ApiResponse.<String>builder()
                .result("Môn học đã được xóa thành công")
                .build();
    }
}
