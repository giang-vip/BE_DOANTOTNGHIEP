package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.ClassSectionRequest;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.entity.ClassSection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClassSectionMapper {
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "major", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "semester", ignore = true)
    ClassSection toClassSection(ClassSectionRequest request);

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "major.id", target = "majorId")
    @Mapping(source = "major.name", target = "majorName")
    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "subject.name", target = "subjectName")
    @Mapping(source = "subject.credits", target = "credits")
    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(source = "teacher.fullName", target = "teacherName")
    @Mapping(source = "semester.id", target = "semesterId")
    @Mapping(source = "semester.code", target = "semesterCode")
    @Mapping(target = "subjectSemesterIndex", ignore = true)
    @Mapping(target = "subjectType", ignore = true)
    ClassSectionResponse toClassSectionResponse(ClassSection classSection);

    @Mapping(target = "department", ignore = true)
    @Mapping(target = "major", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "semester", ignore = true)
    void updateClassSection(@MappingTarget ClassSection classSection, ClassSectionRequest request);
}
