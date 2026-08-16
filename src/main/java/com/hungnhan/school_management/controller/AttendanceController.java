package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.AttendanceRecordRequest;
import com.hungnhan.school_management.dto.request.AttendanceSessionRequest;
import com.hungnhan.school_management.dto.response.AttendanceRecordResponse;
import com.hungnhan.school_management.dto.response.AttendanceSessionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@Tag(name = "Teacher - Attendance Management", description = "API quản lý điểm danh dành cho Giảng viên (Phase 3)")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/classes/{classSectionId}/attendance-sessions")
    @Operation(summary = "Xem các phiên điểm danh của lớp học phần (API_TC_03)")
    public ApiResponse<PageResponse<AttendanceSessionResponse>> getAttendanceSessions(
            @PathVariable Long classSectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<AttendanceSessionResponse>>builder()
                .result(attendanceService.getAttendanceSessions(username, classSectionId, page, size))
                .build();
    }

    @PostMapping("/classes/{classSectionId}/attendance-sessions")
    @Operation(summary = "Tạo phiên điểm danh mới (API_TC_04)")
    public ApiResponse<AttendanceSessionResponse> createAttendanceSession(
            @PathVariable Long classSectionId,
            @RequestBody @Valid AttendanceSessionRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<AttendanceSessionResponse>builder()
                .result(attendanceService.createAttendanceSession(username, classSectionId, request))
                .build();
    }

    @GetMapping("/attendance-sessions/{sessionId}/records")
    @Operation(summary = "Lấy bảng điểm danh chi tiết (API_TC_05)")
    public ApiResponse<List<AttendanceRecordResponse>> getAttendanceRecords(
            @PathVariable Long sessionId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<List<AttendanceRecordResponse>>builder()
                .result(attendanceService.getAttendanceRecords(username, sessionId))
                .build();
    }

    @PatchMapping("/attendance-records/{recordId}")
    @Operation(summary = "Chấm điểm danh thủ công (API_TC_06)")
    public ApiResponse<AttendanceRecordResponse> updateAttendanceRecord(
            @PathVariable Long recordId,
            @RequestBody @Valid AttendanceRecordRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<AttendanceRecordResponse>builder()
                .result(attendanceService.updateAttendanceRecord(username, recordId, request))
                .build();
    }

    @PatchMapping("/attendance-sessions/{sessionId}/status")
    @Operation(summary = "Cập nhật trạng thái phiên điểm danh (API_TC_14)")
    public ApiResponse<AttendanceSessionResponse> updateSessionStatus(
            @PathVariable Long sessionId,
            @RequestParam String status
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<AttendanceSessionResponse>builder()
                .result(attendanceService.updateSessionStatus(username, sessionId, status))
                .build();
    }
}
