package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.AnnouncementRequest;
import com.hungnhan.school_management.dto.response.AnnouncementResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.TeacherAnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher/classes/{classSectionId}/announcements")
@RequiredArgsConstructor
@Tag(name = "Teacher - Announcement", description = "API quản lý thông báo lớp học (Phase 3)")
public class TeacherAnnouncementController {

    private final TeacherAnnouncementService teacherAnnouncementService;

    @GetMapping
    @Operation(summary = "Danh sách thông báo lớp học phần (API_TC_10)")
    public ApiResponse<PageResponse<AnnouncementResponse>> getAnnouncements(
            @PathVariable Long classSectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<AnnouncementResponse>>builder()
                .result(teacherAnnouncementService.getAnnouncements(username, classSectionId, page, size))
                .build();
    }

    @PostMapping
    @Operation(summary = "Đăng thông báo mới cho lớp (API_TC_11)")
    public ApiResponse<AnnouncementResponse> createAnnouncement(
            @PathVariable Long classSectionId,
            @RequestBody @Valid AnnouncementRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<AnnouncementResponse>builder()
                .result(teacherAnnouncementService.createAnnouncement(username, classSectionId, request))
                .build();
    }

    @PutMapping("/{announcementId}")
    @Operation(summary = "Cập nhật thông báo lớp học (API_TC_12)")
    public ApiResponse<AnnouncementResponse> updateAnnouncement(
            @PathVariable Long classSectionId,
            @PathVariable Long announcementId,
            @RequestBody @Valid AnnouncementRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<AnnouncementResponse>builder()
                .result(teacherAnnouncementService.updateAnnouncement(username, classSectionId, announcementId, request))
                .build();
    }

    @DeleteMapping("/{announcementId}")
    @Operation(summary = "Xóa thông báo lớp học (API_TC_13)")
    public ApiResponse<String> deleteAnnouncement(
            @PathVariable Long classSectionId,
            @PathVariable Long announcementId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        teacherAnnouncementService.deleteAnnouncement(username, classSectionId, announcementId);
        return ApiResponse.<String>builder()
                .result("Đã xóa thông báo thành công")
                .build();
    }
}
