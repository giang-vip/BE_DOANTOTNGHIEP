package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.TeacherRequest;
import com.hungnhan.school_management.dto.response.TeacherResponse;
import com.hungnhan.school_management.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "department", ignore = true)
    Teacher toTeacher(TeacherRequest request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    TeacherResponse toTeacherResponse(Teacher teacher);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "department", ignore = true)
    void updateTeacher(@MappingTarget Teacher teacher, TeacherRequest request);
}
