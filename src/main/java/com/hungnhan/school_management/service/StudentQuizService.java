package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.QuizSubmissionRequest;
import com.hungnhan.school_management.dto.response.QuizResultResponse;
import com.hungnhan.school_management.dto.response.SubmissionResponse;

public interface StudentQuizService {

    SubmissionResponse startQuiz(String username, Long assignmentId);

    QuizResultResponse submitQuiz(String username, Long assignmentId, QuizSubmissionRequest request);

    QuizResultResponse getQuizResult(String username, Long assignmentId);

    java.util.List<com.hungnhan.school_management.dto.response.QuizQuestionResponse> getQuizQuestionsForStudent(String username, Long assignmentId);
}
