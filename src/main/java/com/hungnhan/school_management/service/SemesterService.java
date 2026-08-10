package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.SemesterRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.SemesterResponse;

public interface SemesterService {
    SemesterResponse createSemester(SemesterRequest request);

    SemesterResponse updateSemester(Long id, SemesterRequest request);

    PageResponse<SemesterResponse> getSemesters(String search, Long academicYearId, int page, int size);

    SemesterResponse getSemesterById(Long id);

    void deleteSemester(Long id);
}
