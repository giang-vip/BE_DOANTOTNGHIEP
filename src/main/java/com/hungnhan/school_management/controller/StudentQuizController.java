package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.QuizSubmissionRequest;
import com.hungnhan.school_management.dto.response.QuizResultResponse;
import com.hungnhan.school_management.dto.response.SubmissionResponse;
import com.hungnhan.school_management.service.StudentQuizService;
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
@Tag(name = "Student - Quiz", description = "API làm bài thi trắc nghiệm trực tuyến dành cho Sinh viên (Phase 4)")
public class StudentQuizController {

    private final StudentQuizService studentQuizService;

    @PostMapping("/assignments/{assignmentId}/start-quiz")
    @Operation(summary = "Bắt đầu làm bài Quiz (tạo session quiz) (API_ST_09)")
    public ApiResponse<SubmissionResponse> startQuiz(@PathVariable Long assignmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<SubmissionResponse>builder()
                .result(studentQuizService.startQuiz(username, assignmentId))
                .build();
    }

    @PostMapping("/assignments/{assignmentId}/submit-quiz")
    @Operation(summary = "Nộp bài Quiz & tự động chấm điểm (API_ST_10)")
    public ApiResponse<QuizResultResponse> submitQuiz(
            @PathVariable Long assignmentId,
            @RequestBody @Valid QuizSubmissionRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<QuizResultResponse>builder()
                .result(studentQuizService.submitQuiz(username, assignmentId, request))
                .build();
    }

    @GetMapping("/assignments/{assignmentId}/quiz-result")
    @Operation(summary = "Xem lại bài Quiz đã nộp (Xem chi tiết câu đúng sai) (API_ST_11)")
    public ApiResponse<QuizResultResponse> getQuizResult(@PathVariable Long assignmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<QuizResultResponse>builder()
                .result(studentQuizService.getQuizResult(username, assignmentId))
                .build();
    }
}
