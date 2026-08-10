package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.MajorRequest;
import com.hungnhan.school_management.dto.response.MajorResponse;
import com.hungnhan.school_management.service.MajorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/admin/majors")
@RequiredArgsConstructor
@Tag(name = "Major Management", description = "API quản lý Ngành học (Dành cho ADMIN)")
public class MajorController {

    private final MajorService majorService;

    @PostMapping
    @Operation(summary = "Thêm mới ngành học")
    public ApiResponse<MajorResponse> createMajor(@RequestBody @Valid MajorRequest request) {
        return ApiResponse.<MajorResponse>builder()
                .result(majorService.createMajor(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả ngành học")
    public ApiResponse<com.hungnhan.school_management.dto.response.PageResponse<MajorResponse>> getAllMajors(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<com.hungnhan.school_management.dto.response.PageResponse<MajorResponse>>builder()
                .result(majorService.getAllMajors(search, departmentId, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết ngành học theo ID")
    public ApiResponse<MajorResponse> getMajorById(@PathVariable Long id) {
        return ApiResponse.<MajorResponse>builder()
                .result(majorService.getMajorById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật ngành học")
    public ApiResponse<MajorResponse> updateMajor(@PathVariable Long id, @RequestBody @Valid MajorRequest request) {
        return ApiResponse.<MajorResponse>builder()
                .result(majorService.updateMajor(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa ngành học")
    public ApiResponse<Void> deleteMajor(@PathVariable Long id) {
        majorService.deleteMajor(id);
        return ApiResponse.<Void>builder().build();
    }
}
