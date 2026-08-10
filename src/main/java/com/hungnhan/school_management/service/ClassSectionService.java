package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.ClassSectionRequest;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

public interface ClassSectionService {
    ClassSectionResponse createClassSection(ClassSectionRequest request);

    ClassSectionResponse updateClassSection(Long id, ClassSectionRequest request);

    PageResponse<ClassSectionResponse> getClassSections(String search, Long semesterId, Long subjectId, Long departmentId, Long majorId, int page, int size);

    ClassSectionResponse getClassSectionById(Long id);

    void deleteClassSection(Long id);
}
