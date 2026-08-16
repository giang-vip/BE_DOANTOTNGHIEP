package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.RegistrationPeriodRequest;
import com.hungnhan.school_management.dto.response.RegistrationPeriodResponse;
import com.hungnhan.school_management.service.RegistrationPeriodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/admin/config/registration-period")
@RequiredArgsConstructor
@Tag(name = "Registration Period Config", description = "API Cấu hình thời gian đăng ký học phần (Dành cho ADMIN)")
public class RegistrationPeriodController {

    private final RegistrationPeriodService registrationPeriodService;

    @PostMapping
    @Operation(summary = "Tạo mới hoặc cập nhật thời gian mở/đóng cổng đăng ký")
    public ApiResponse<RegistrationPeriodResponse> createOrUpdate(@RequestBody @Valid RegistrationPeriodRequest request) {
        return ApiResponse.<RegistrationPeriodResponse>builder()
                .result(registrationPeriodService.createOrUpdateRegistrationPeriod(request))
                .build();
    }

    @GetMapping("/current")
    @Operation(summary = "Lấy cấu hình thời gian đăng ký hiện tại (Active)")
    public ApiResponse<RegistrationPeriodResponse> getCurrent() {
        return ApiResponse.<RegistrationPeriodResponse>builder()
                .result(registrationPeriodService.getCurrentRegistrationPeriod())
                .build();
    }

    @GetMapping
    @Operation(summary = "Lấy lịch sử cấu hình đăng ký học phần")
    public ApiResponse<List<RegistrationPeriodResponse>> getAll() {
        return ApiResponse.<List<RegistrationPeriodResponse>>builder()
                .result(registrationPeriodService.getAllRegistrationPeriods())
                .build();
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Bật/tắt trạng thái mở cổng đăng ký thủ công")
    public ApiResponse<Void> toggle(@PathVariable Long id, @RequestParam boolean isOpen) {
        registrationPeriodService.toggleRegistrationPeriod(id, isOpen);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa cấu hình thời gian đăng ký")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        registrationPeriodService.deleteRegistrationPeriod(id);
        return ApiResponse.<Void>builder().build();
    }
}
