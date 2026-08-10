package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.SchoolClassRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.SchoolClassResponse;

public interface SchoolClassService {
    SchoolClassResponse createClass(SchoolClassRequest request);

    SchoolClassResponse updateClass(Long id, SchoolClassRequest request);

    PageResponse<SchoolClassResponse> getClasses(String search, Long majorId, int page, int size);

    SchoolClassResponse getClassById(Long id);

    void deleteClass(Long id);
}
