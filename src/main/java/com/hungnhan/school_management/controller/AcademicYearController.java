package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.AcademicYearRequest;
import com.hungnhan.school_management.dto.response.AcademicYearResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.AcademicYearService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/academic-years")
@RequiredArgsConstructor
@Tag(name = "Academic Year Management", description = "API quản lý Năm học (Dành cho ADMIN)")
public class AcademicYearController {

    private final AcademicYearService academicYearService;

    @PostMapping
    @Operation(summary = "Tạo mới năm học (API_AD_12)")
    public ApiResponse<AcademicYearResponse> createAcademicYear(@RequestBody @Valid AcademicYearRequest request) {
        return ApiResponse.<AcademicYearResponse>builder()
                .result(academicYearService.createAcademicYear(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Danh sách năm học (API_AD_11)")
    public ApiResponse<PageResponse<AcademicYearResponse>> getAcademicYears(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<AcademicYearResponse>>builder()
                .result(academicYearService.getAcademicYears(search, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết năm học")
    public ApiResponse<AcademicYearResponse> getAcademicYearById(@PathVariable Long id) {
        return ApiResponse.<AcademicYearResponse>builder()
                .result(academicYearService.getAcademicYearById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật năm học (API_AD_12)")
    public ApiResponse<AcademicYearResponse> updateAcademicYear(@PathVariable Long id, @RequestBody @Valid AcademicYearRequest request) {
        return ApiResponse.<AcademicYearResponse>builder()
                .result(academicYearService.updateAcademicYear(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa năm học")
    public ApiResponse<String> deleteAcademicYear(@PathVariable Long id) {
        academicYearService.deleteAcademicYear(id);
        return ApiResponse.<String>builder()
                .result("Năm học đã được xóa thành công")
                .build();
    }
}
