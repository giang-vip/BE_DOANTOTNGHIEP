package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.StudentRequest;
import com.hungnhan.school_management.dto.response.StudentResponse;
import com.hungnhan.school_management.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "major", ignore = true)
    @Mapping(target = "schoolClass", ignore = true)
    Student toStudent(StudentRequest request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "major.id", target = "majorId")
    @Mapping(source = "major.name", target = "majorName")
    @Mapping(source = "major.totalCredits", target = "majorTotalCredits")
    @Mapping(source = "schoolClass.id", target = "classId")
    @Mapping(source = "schoolClass.code", target = "classCode")
    @Mapping(target = "entryStartYear", expression = "java(student.getSchoolClass() != null && student.getSchoolClass().getEntryAcademicYear() != null && student.getSchoolClass().getEntryAcademicYear().getStartDate() != null ? student.getSchoolClass().getEntryAcademicYear().getStartDate().getYear() : null)")
    StudentResponse toStudentResponse(Student student);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "major", ignore = true)
    @Mapping(target = "schoolClass", ignore = true)
    void updateStudent(@MappingTarget Student student, StudentRequest request);
}
