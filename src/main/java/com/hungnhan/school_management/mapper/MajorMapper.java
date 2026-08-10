package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.MajorRequest;
import com.hungnhan.school_management.dto.response.MajorResponse;
import com.hungnhan.school_management.entity.Major;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MajorMapper {

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    MajorResponse toMajorResponse(Major major);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    Major toMajor(MajorRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateMajorFromRequest(MajorRequest request, @MappingTarget Major major);
}
