package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.DepartmentRequest;
import com.hungnhan.school_management.dto.response.DepartmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/departments")
@RequiredArgsConstructor
@Tag(name = "Department Management", description = "API quản lý Khoa hành chính (Dành cho ADMIN)")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @Operation(summary = "Tạo mới khoa hành chính (API_AD_08)")
    public ApiResponse<DepartmentResponse> createDepartment(@RequestBody @Valid DepartmentRequest request) {
        return ApiResponse.<DepartmentResponse>builder()
                .result(departmentService.createDepartment(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Danh sách khoa hành chính (API_AD_07)")
    public ApiResponse<PageResponse<DepartmentResponse>> getDepartments(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<DepartmentResponse>>builder()
                .result(departmentService.getDepartments(search, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết khoa hành chính")
    public ApiResponse<DepartmentResponse> getDepartmentById(@PathVariable Long id) {
        return ApiResponse.<DepartmentResponse>builder()
                .result(departmentService.getDepartmentById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Sửa khoa hành chính (API_AD_09)")
    public ApiResponse<DepartmentResponse> updateDepartment(@PathVariable Long id, @RequestBody @Valid DepartmentRequest request) {
        return ApiResponse.<DepartmentResponse>builder()
                .result(departmentService.updateDepartment(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa khoa hành chính (API_AD_10)")
    public ApiResponse<String> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ApiResponse.<String>builder()
                .result("Khoa hành chính đã được xóa thành công")
                .build();
    }
}
