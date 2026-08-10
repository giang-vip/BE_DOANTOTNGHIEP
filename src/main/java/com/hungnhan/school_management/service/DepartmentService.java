package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.DepartmentRequest;
import com.hungnhan.school_management.dto.response.DepartmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentRequest request);

    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);

    PageResponse<DepartmentResponse> getDepartments(String search, int page, int size);

    DepartmentResponse getDepartmentById(Long id);

    void deleteDepartment(Long id);
}
