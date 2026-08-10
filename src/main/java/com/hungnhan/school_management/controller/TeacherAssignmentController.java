package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.AssignmentRequest;
import com.hungnhan.school_management.dto.request.QuizQuestionRequest;
import com.hungnhan.school_management.dto.request.SubmissionGradeRequest;
import com.hungnhan.school_management.dto.response.AssignmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.QuizQuestionResponse;
import com.hungnhan.school_management.dto.response.SubmissionResponse;
import com.hungnhan.school_management.service.TeacherAssignmentService;
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
@Tag(name = "Teacher - Assignment & Grading", description = "API quản lý bài tập và chấm điểm dành cho Giảng viên (Phase 3)")
public class TeacherAssignmentController {

    private final TeacherAssignmentService teacherAssignmentService;

    @GetMapping("/classes/{classSectionId}/assignments")
    @Operation(summary = "Danh sách bài tập lớp HP (API_TC_12)")
    public ApiResponse<PageResponse<AssignmentResponse>> getAssignments(
            @PathVariable Long classSectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<AssignmentResponse>>builder()
                .result(teacherAssignmentService.getAssignments(username, classSectionId, page, size))
                .build();
    }

    @PostMapping("/classes/{classSectionId}/assignments")
    @Operation(summary = "Tạo bài tập mới (Luận/Quiz) (API_TC_13)")
    public ApiResponse<AssignmentResponse> createAssignment(
            @PathVariable Long classSectionId,
            @RequestBody @Valid AssignmentRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<AssignmentResponse>builder()
                .result(teacherAssignmentService.createAssignment(username, classSectionId, request))
                .build();
    }

    @PutMapping("/assignments/{id}")
    @Operation(summary = "Cập nhật bài tập (API_TC_14)")
    public ApiResponse<AssignmentResponse> updateAssignment(
            @PathVariable Long id,
            @RequestBody @Valid AssignmentRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<AssignmentResponse>builder()
                .result(teacherAssignmentService.updateAssignment(username, id, request))
                .build();
    }

    @DeleteMapping("/assignments/{id}")
    @Operation(summary = "Xóa bài tập (API_TC_15)")
    public ApiResponse<String> deleteAssignment(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        teacherAssignmentService.deleteAssignment(username, id);
        return ApiResponse.<String>builder()
                .result("Bài tập đã được xóa thành công")
                .build();
    }

    @PostMapping("/assignments/{id}/configure-quiz")
    @Operation(summary = "Đính kèm đề trắc nghiệm & đáp án (API_TC_16)")
    public ApiResponse<List<QuizQuestionResponse>> configureQuiz(
            @PathVariable Long id,
            @RequestBody @Valid List<QuizQuestionRequest> requests
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<List<QuizQuestionResponse>>builder()
                .result(teacherAssignmentService.configureQuiz(username, id, requests))
                .build();
    }

    @GetMapping("/assignments/{id}/submissions")
    @Operation(summary = "Xem danh sách bài nộp của lớp (API_TC_17)")
    public ApiResponse<PageResponse<SubmissionResponse>> getSubmissions(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<SubmissionResponse>>builder()
                .result(teacherAssignmentService.getSubmissions(username, id, page, size))
                .build();
    }

    @PatchMapping("/submissions/{id}/grade")
    @Operation(summary = "Chấm điểm & nhận xét bài tự luận (API_TC_18)")
    public ApiResponse<SubmissionResponse> gradeSubmission(
            @PathVariable Long id,
            @RequestBody @Valid SubmissionGradeRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<SubmissionResponse>builder()
                .result(teacherAssignmentService.gradeSubmission(username, id, request))
                .build();
    }
}
