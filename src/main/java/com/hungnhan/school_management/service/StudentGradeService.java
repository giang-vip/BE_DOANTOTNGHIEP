package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.StudentGradeResponse;

public interface StudentGradeService {

    PageResponse<StudentGradeResponse> getMyGrades(String username, Long semesterId, int page, int size);
    
    PageResponse<StudentGradeResponse> getStudentGradesByStudentId(Long studentId, Long semesterId, int page, int size);
}
