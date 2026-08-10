package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.SchoolClassRequest;
import com.hungnhan.school_management.dto.response.SchoolClassResponse;
import com.hungnhan.school_management.entity.SchoolClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SchoolClassMapper {

    @Mapping(target = "major", ignore = true)
    @Mapping(target = "entryAcademicYear", ignore = true)
    @Mapping(target = "homeroomTeacher", ignore = true)
    SchoolClass toSchoolClass(SchoolClassRequest request);

    @Mapping(source = "major.id", target = "majorId")
    @Mapping(source = "major.name", target = "majorName")
    @Mapping(source = "entryAcademicYear.id", target = "entryAcademicYearId")
    @Mapping(source = "entryAcademicYear.code", target = "entryAcademicYearCode")
    @Mapping(source = "homeroomTeacher.id", target = "homeroomTeacherId")
    @Mapping(source = "homeroomTeacher.fullName", target = "homeroomTeacherName")
    SchoolClassResponse toSchoolClassResponse(SchoolClass schoolClass);

    @Mapping(target = "major", ignore = true)
    @Mapping(target = "entryAcademicYear", ignore = true)
    @Mapping(target = "homeroomTeacher", ignore = true)
    void updateSchoolClass(@MappingTarget SchoolClass schoolClass, SchoolClassRequest request);
}
