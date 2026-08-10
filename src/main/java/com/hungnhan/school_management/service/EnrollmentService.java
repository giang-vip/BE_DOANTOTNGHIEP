package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.EnrollmentRequest;
import com.hungnhan.school_management.dto.response.EnrollmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

public interface EnrollmentService {
    EnrollmentResponse createEnrollment(EnrollmentRequest request);

    EnrollmentResponse updateEnrollment(Long id, EnrollmentRequest request);

    PageResponse<EnrollmentResponse> getEnrollments(Long studentId, Long classSectionId, int page, int size);

    EnrollmentResponse getEnrollmentById(Long id);

    void deleteEnrollment(Long id);
}
