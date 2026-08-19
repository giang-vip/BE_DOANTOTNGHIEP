package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.EnrollmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

public interface TeacherClassService {
    PageResponse<ClassSectionResponse> getTeacherClassSections(String username, String search, Long semesterId, int page, int size);
    
    PageResponse<ClassSectionResponse> getTeacherClassSectionsByTeacherId(Long teacherId, String search, Long semesterId, int page, int size);

    PageResponse<EnrollmentResponse> getStudentsInClassSection(String username, Long classSectionId, int page, int size);
}
