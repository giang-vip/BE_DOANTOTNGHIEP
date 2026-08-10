package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.SubjectRequest;
import com.hungnhan.school_management.dto.response.SubjectResponse;
import com.hungnhan.school_management.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    @Mapping(target = "department", ignore = true)
    Subject toSubject(SubjectRequest request);

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    SubjectResponse toSubjectResponse(Subject subject);

    @Mapping(target = "department", ignore = true)
    void updateSubject(@MappingTarget Subject subject, SubjectRequest request);
}
