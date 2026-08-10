package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.SemesterRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.SemesterResponse;
import com.hungnhan.school_management.service.SemesterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/semesters")
@RequiredArgsConstructor
@Tag(name = "Semester Management", description = "API quản lý Học kỳ (Dành cho ADMIN)")
public class SemesterController {

    private final SemesterService semesterService;

    @PostMapping
    @Operation(summary = "Tạo mới học kỳ (API_AD_14)")
    public ApiResponse<SemesterResponse> createSemester(@RequestBody @Valid SemesterRequest request) {
        return ApiResponse.<SemesterResponse>builder()
                .result(semesterService.createSemester(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Danh sách học kỳ (API_AD_13)")
    public ApiResponse<PageResponse<SemesterResponse>> getSemesters(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long academicYearId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<SemesterResponse>>builder()
                .result(semesterService.getSemesters(search, academicYearId, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết học kỳ")
    public ApiResponse<SemesterResponse> getSemesterById(@PathVariable Long id) {
        return ApiResponse.<SemesterResponse>builder()
                .result(semesterService.getSemesterById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật học kỳ (API_AD_14)")
    public ApiResponse<SemesterResponse> updateSemester(@PathVariable Long id, @RequestBody @Valid SemesterRequest request) {
        return ApiResponse.<SemesterResponse>builder()
                .result(semesterService.updateSemester(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa học kỳ")
    public ApiResponse<String> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return ApiResponse.<String>builder()
                .result("Học kỳ đã được xóa thành công")
                .build();
    }
}
