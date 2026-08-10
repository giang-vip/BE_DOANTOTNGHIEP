package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.EnrollmentRequest;
import com.hungnhan.school_management.dto.response.EnrollmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollment Management", description = "API quản lý Đăng ký học phần (Dành cho ADMIN)")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "Đăng ký học phần cho sinh viên (API_AD_25)")
    public ApiResponse<EnrollmentResponse> createEnrollment(@RequestBody @Valid EnrollmentRequest request) {
        return ApiResponse.<EnrollmentResponse>builder()
                .result(enrollmentService.createEnrollment(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Danh sách đăng ký học phần")
    public ApiResponse<PageResponse<EnrollmentResponse>> getEnrollments(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long classSectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<EnrollmentResponse>>builder()
                .result(enrollmentService.getEnrollments(studentId, classSectionId, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết đăng ký học phần")
    public ApiResponse<EnrollmentResponse> getEnrollmentById(@PathVariable Long id) {
        return ApiResponse.<EnrollmentResponse>builder()
                .result(enrollmentService.getEnrollmentById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật đăng ký học phần")
    public ApiResponse<EnrollmentResponse> updateEnrollment(@PathVariable Long id, @RequestBody @Valid EnrollmentRequest request) {
        return ApiResponse.<EnrollmentResponse>builder()
                .result(enrollmentService.updateEnrollment(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa đăng ký học phần / Hủy đăng ký")
    public ApiResponse<String> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ApiResponse.<String>builder()
                .result("Hủy đăng ký học phần thành công")
                .build();
    }
}
