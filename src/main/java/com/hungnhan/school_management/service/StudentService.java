package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.StudentRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.StudentResponse;

public interface StudentService {
    StudentResponse createStudent(StudentRequest request);

    StudentResponse updateStudent(Long id, StudentRequest request);

    PageResponse<StudentResponse> getStudents(String search, Long departmentId, Long majorId, Long classId, int page, int size);

    StudentResponse getStudentById(Long id);

    void deleteStudent(Long id);
}
