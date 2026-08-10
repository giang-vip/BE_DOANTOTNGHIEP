package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.AcademicYearRequest;
import com.hungnhan.school_management.dto.response.AcademicYearResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

public interface AcademicYearService {
    AcademicYearResponse createAcademicYear(AcademicYearRequest request);

    AcademicYearResponse updateAcademicYear(Long id, AcademicYearRequest request);

    PageResponse<AcademicYearResponse> getAcademicYears(String search, int page, int size);

    AcademicYearResponse getAcademicYearById(Long id);

    void deleteAcademicYear(Long id);
}
