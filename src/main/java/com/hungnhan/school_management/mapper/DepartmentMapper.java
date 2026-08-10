package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.DepartmentRequest;
import com.hungnhan.school_management.dto.response.DepartmentResponse;
import com.hungnhan.school_management.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDepartment(DepartmentRequest request);

    DepartmentResponse toDepartmentResponse(Department department);

    void updateDepartment(@MappingTarget Department department, DepartmentRequest request);
}
