package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.AssignmentRequest;
import com.hungnhan.school_management.dto.request.QuizQuestionRequest;
import com.hungnhan.school_management.dto.request.SubmissionGradeRequest;
import com.hungnhan.school_management.dto.response.AssignmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.QuizQuestionResponse;
import com.hungnhan.school_management.dto.response.SubmissionResponse;

import java.util.List;

public interface TeacherAssignmentService {

    PageResponse<AssignmentResponse> getAssignments(String username, Long classSectionId, int page, int size);

    AssignmentResponse createAssignment(String username, Long classSectionId, AssignmentRequest request);

    AssignmentResponse updateAssignment(String username, Long id, AssignmentRequest request);

    void deleteAssignment(String username, Long id);

    List<QuizQuestionResponse> configureQuiz(String username, Long assignmentId, List<QuizQuestionRequest> requests);

    List<QuizQuestionResponse> getQuizQuestions(String username, Long assignmentId);

    PageResponse<SubmissionResponse> getSubmissions(String username, Long assignmentId, int page, int size);

    SubmissionResponse gradeSubmission(String username, Long submissionId, SubmissionGradeRequest request);
}
