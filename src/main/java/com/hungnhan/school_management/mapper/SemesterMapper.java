package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.SemesterRequest;
import com.hungnhan.school_management.dto.response.SemesterResponse;
import com.hungnhan.school_management.entity.Semester;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SemesterMapper {
    @Mapping(target = "academicYear", ignore = true)
    Semester toSemester(SemesterRequest request);

    @Mapping(source = "academicYear.id", target = "academicYearId")
    @Mapping(source = "academicYear.code", target = "academicYearCode")
    SemesterResponse toSemesterResponse(Semester semester);

    @Mapping(target = "academicYear", ignore = true)
    void updateSemester(@MappingTarget Semester semester, SemesterRequest request);
}
