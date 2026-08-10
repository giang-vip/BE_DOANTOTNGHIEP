package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.ClassSectionRequest;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.ClassSectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/class-sections")
@RequiredArgsConstructor
@Tag(name = "Class Section Management", description = "API quản lý Lớp học phần (Dành cho ADMIN)")
public class ClassSectionController {

    private final ClassSectionService classSectionService;

    @PostMapping
    @Operation(summary = "Tạo mới lớp học phần (API_AD_24)")
    public ApiResponse<ClassSectionResponse> createClassSection(@RequestBody @Valid ClassSectionRequest request) {
        return ApiResponse.<ClassSectionResponse>builder()
                .result(classSectionService.createClassSection(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Danh sách lớp học phần (API_AD_23)")
    public ApiResponse<PageResponse<ClassSectionResponse>> getClassSections(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long majorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<ClassSectionResponse>>builder()
                .result(classSectionService.getClassSections(search, semesterId, subjectId, departmentId, majorId, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết lớp học phần")
    public ApiResponse<ClassSectionResponse> getClassSectionById(@PathVariable Long id) {
        return ApiResponse.<ClassSectionResponse>builder()
                .result(classSectionService.getClassSectionById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật lớp học phần (API_AD_24)")
    public ApiResponse<ClassSectionResponse> updateClassSection(@PathVariable Long id, @RequestBody @Valid ClassSectionRequest request) {
        return ApiResponse.<ClassSectionResponse>builder()
                .result(classSectionService.updateClassSection(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa lớp học phần")
    public ApiResponse<String> deleteClassSection(@PathVariable Long id) {
        classSectionService.deleteClassSection(id);
        return ApiResponse.<String>builder()
                .result("Lớp học phần đã được xóa thành công")
                .build();
    }
}
