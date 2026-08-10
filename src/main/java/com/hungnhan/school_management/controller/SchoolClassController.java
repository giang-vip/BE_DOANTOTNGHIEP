package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.SchoolClassRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.SchoolClassResponse;
import com.hungnhan.school_management.service.SchoolClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/classes")
@RequiredArgsConstructor
@Tag(name = "School Class Management", description = "API quản lý Lớp hành chính (Dành cho ADMIN)")
public class SchoolClassController {

    private final SchoolClassService schoolClassService;

    @PostMapping
    @Operation(summary = "Tạo mới lớp hành chính (API_AD_20)")
    public ApiResponse<SchoolClassResponse> createClass(@RequestBody @Valid SchoolClassRequest request) {
        return ApiResponse.<SchoolClassResponse>builder()
                .result(schoolClassService.createClass(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Danh sách lớp hành chính (API_AD_19)")
    public ApiResponse<PageResponse<SchoolClassResponse>> getClasses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long majorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<SchoolClassResponse>>builder()
                .result(schoolClassService.getClasses(search, majorId, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết lớp hành chính")
    public ApiResponse<SchoolClassResponse> getClassById(@PathVariable Long id) {
        return ApiResponse.<SchoolClassResponse>builder()
                .result(schoolClassService.getClassById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật lớp hành chính (API_AD_20)")
    public ApiResponse<SchoolClassResponse> updateClass(@PathVariable Long id, @RequestBody @Valid SchoolClassRequest request) {
        return ApiResponse.<SchoolClassResponse>builder()
                .result(schoolClassService.updateClass(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa lớp hành chính")
    public ApiResponse<String> deleteClass(@PathVariable Long id) {
        schoolClassService.deleteClass(id);
        return ApiResponse.<String>builder()
                .result("Lớp hành chính đã được xóa thành công")
                .build();
    }
}
