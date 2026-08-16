package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.AdminAnnouncementRequest;
import com.hungnhan.school_management.dto.response.AdminAnnouncementResponse;
import com.hungnhan.school_management.service.AdminAnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/admin/announcements")
@RequiredArgsConstructor
@Tag(name = "Admin Announcement Management", description = "API quản lý Thông báo hệ thống (Dành cho ADMIN)")
public class AdminAnnouncementController {

    private final AdminAnnouncementService announcementService;

    @PostMapping
    @Operation(summary = "Tạo mới thông báo hệ thống")
    public ApiResponse<AdminAnnouncementResponse> createAnnouncement(@RequestBody @Valid AdminAnnouncementRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ApiResponse.<AdminAnnouncementResponse>builder()
                .result(announcementService.createAnnouncement(request, auth.getName()))
                .build();
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả thông báo")
    public ApiResponse<List<AdminAnnouncementResponse>> getAllAnnouncements() {
        return ApiResponse.<List<AdminAnnouncementResponse>>builder()
                .result(announcementService.getAllAnnouncements())
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông báo")
    public ApiResponse<AdminAnnouncementResponse> updateAnnouncement(
            @PathVariable Long id, 
            @RequestBody @Valid AdminAnnouncementRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ApiResponse.<AdminAnnouncementResponse>builder()
                .result(announcementService.updateAnnouncement(id, request, auth.getName()))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa thông báo")
    public ApiResponse<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ApiResponse.<Void>builder().build();
    }
}
