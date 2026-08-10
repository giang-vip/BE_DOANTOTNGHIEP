package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.EnrollmentRequest;
import com.hungnhan.school_management.dto.response.EnrollmentResponse;
import com.hungnhan.school_management.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "classSection", ignore = true)
    Enrollment toEnrollment(EnrollmentRequest request);

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.studentCode", target = "studentCode")
    @Mapping(source = "student.fullName", target = "studentName")
    @Mapping(source = "classSection.id", target = "classSectionId")
    @Mapping(source = "classSection.sectionCode", target = "sectionCode")
    EnrollmentResponse toEnrollmentResponse(Enrollment enrollment);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "classSection", ignore = true)
    void updateEnrollment(@MappingTarget Enrollment enrollment, EnrollmentRequest request);
}
