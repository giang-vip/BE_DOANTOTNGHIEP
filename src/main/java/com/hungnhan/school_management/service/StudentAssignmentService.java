package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.SubmissionRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.StudentAssignmentResponse;
import com.hungnhan.school_management.dto.response.SubmissionResponse;

public interface StudentAssignmentService {

    PageResponse<StudentAssignmentResponse> getAssignments(String username, Long classSectionId, int page, int size);

    SubmissionResponse submitAssignment(String username, Long assignmentId, SubmissionRequest request);

    SubmissionResponse getMySubmission(String username, Long assignmentId);
}
