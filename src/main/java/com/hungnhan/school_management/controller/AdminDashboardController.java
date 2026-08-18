package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.response.AdminDashboardResponse;
import com.hungnhan.school_management.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/stats")
    public ApiResponse<AdminDashboardResponse> getStats() {
        return ApiResponse.<AdminDashboardResponse>builder()
                .result(adminDashboardService.getDashboardStats())
                .build();
    }

    @GetMapping("/grade-distribution")
    public ApiResponse<java.util.Map<String, Integer>> getGradeDistribution(
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long yearId,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long semesterId,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long classSectionId,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long departmentId,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long majorId) {
        
        return ApiResponse.<java.util.Map<String, Integer>>builder()
                .result(adminDashboardService.getGradeDistribution(yearId, semesterId, classSectionId, departmentId, majorId))
                .build();
    }
}
