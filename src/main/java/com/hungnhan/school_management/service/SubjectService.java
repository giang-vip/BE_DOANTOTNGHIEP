package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.SubjectRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.SubjectResponse;

public interface SubjectService {
    SubjectResponse createSubject(SubjectRequest request);

    SubjectResponse updateSubject(Long id, SubjectRequest request);

    PageResponse<SubjectResponse> getSubjects(String search, Long departmentId, int page, int size);

    SubjectResponse getSubjectById(Long id);

    void deleteSubject(Long id);
}
