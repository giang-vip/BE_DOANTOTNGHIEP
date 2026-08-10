package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.SubmissionRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.StudentAssignmentResponse;
import com.hungnhan.school_management.dto.response.SubmissionResponse;
import com.hungnhan.school_management.service.StudentAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Tag(name = "Student - Assignment", description = "API quản lý bài tập dành cho Sinh viên (Phase 4)")
public class StudentAssignmentController {

    private final StudentAssignmentService studentAssignmentService;

    @GetMapping("/classes/{classSectionId}/assignments")
    @Operation(summary = "Xem danh sách bài tập của lớp (API_ST_06)")
    public ApiResponse<PageResponse<StudentAssignmentResponse>> getAssignments(
            @PathVariable Long classSectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<StudentAssignmentResponse>>builder()
                .result(studentAssignmentService.getAssignments(username, classSectionId, page, size))
                .build();
    }

    @PostMapping("/assignments/{assignmentId}/submit")
    @Operation(summary = "Làm & Nộp bài tập (Luận) (API_ST_07)")
    public ApiResponse<SubmissionResponse> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestBody @Valid SubmissionRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<SubmissionResponse>builder()
                .result(studentAssignmentService.submitAssignment(username, assignmentId, request))
                .build();
    }

    @GetMapping("/assignments/{assignmentId}/submission")
    @Operation(summary = "Xem chi tiết bài nộp & điểm tự luận (API_ST_08)")
    public ApiResponse<SubmissionResponse> getMySubmission(@PathVariable Long assignmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<SubmissionResponse>builder()
                .result(studentAssignmentService.getMySubmission(username, assignmentId))
                .build();
    }
}
