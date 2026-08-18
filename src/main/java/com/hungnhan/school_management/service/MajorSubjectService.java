package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.MajorSubjectRequest;
import com.hungnhan.school_management.dto.response.SubjectResponse;

public interface MajorSubjectService {
    SubjectResponse addSubjectToMajor(Long majorId, MajorSubjectRequest request);
    SubjectResponse updateSubjectInMajor(Long majorId, Long subjectId, MajorSubjectRequest request);
    void removeSubjectFromMajor(Long majorId, Long subjectId);
}
