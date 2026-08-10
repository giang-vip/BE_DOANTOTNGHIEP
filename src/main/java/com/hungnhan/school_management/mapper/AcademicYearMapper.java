package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.AcademicYearRequest;
import com.hungnhan.school_management.dto.response.AcademicYearResponse;
import com.hungnhan.school_management.entity.AcademicYear;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AcademicYearMapper {
    AcademicYear toAcademicYear(AcademicYearRequest request);

    AcademicYearResponse toAcademicYearResponse(AcademicYear academicYear);

    void updateAcademicYear(@MappingTarget AcademicYear academicYear, AcademicYearRequest request);
}
