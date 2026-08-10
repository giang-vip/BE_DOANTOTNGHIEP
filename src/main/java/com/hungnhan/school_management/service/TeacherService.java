package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.TeacherRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.TeacherResponse;

public interface TeacherService {
    TeacherResponse createTeacher(TeacherRequest request);

    TeacherResponse updateTeacher(Long id, TeacherRequest request);

    PageResponse<TeacherResponse> getTeachers(String search, Long departmentId, int page, int size);

    TeacherResponse getTeacherById(Long id);

    void deleteTeacher(Long id);
}
