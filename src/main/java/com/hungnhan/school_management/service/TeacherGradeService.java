package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.GradeConfigRequest;
import com.hungnhan.school_management.dto.response.FinalGradeResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

public interface TeacherGradeService {

    void configureGradeWeights(String username, Long classSectionId, GradeConfigRequest request);

    PageResponse<FinalGradeResponse> getFinalGrades(String username, Long classSectionId, int page, int size);

    void updateStudentGrades(String username, Long classSectionId, java.util.List<com.hungnhan.school_management.dto.request.TeacherGradeUpdateRequest> requests);

    PageResponse<FinalGradeResponse> getFinalGradesForAdmin(Long classSectionId, int page, int size);

    void updateStudentGradesForAdmin(Long classSectionId, java.util.List<com.hungnhan.school_management.dto.request.TeacherGradeUpdateRequest> requests);
}
