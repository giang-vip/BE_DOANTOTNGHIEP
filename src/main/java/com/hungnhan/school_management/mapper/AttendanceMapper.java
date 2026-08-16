package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.AttendanceSessionRequest;
import com.hungnhan.school_management.dto.response.AttendanceRecordResponse;
import com.hungnhan.school_management.dto.response.AttendanceSessionResponse;
import com.hungnhan.school_management.entity.AttendanceRecord;
import com.hungnhan.school_management.entity.AttendanceSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(target = "classSection", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    AttendanceSession toAttendanceSession(AttendanceSessionRequest request);

    @Mapping(source = "classSection.id", target = "classSectionId")
    @Mapping(source = "classSection.sectionCode", target = "sectionCode")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.username", target = "createdByUsername")
    AttendanceSessionResponse toAttendanceSessionResponse(AttendanceSession session);

    @Mapping(target = "classSection", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateAttendanceSession(@MappingTarget AttendanceSession session, AttendanceSessionRequest request);

    @Mapping(source = "attendanceSession.id", target = "attendanceSessionId")
    @Mapping(source = "enrollment.id", target = "enrollmentId")
    @Mapping(source = "enrollment.student.studentCode", target = "studentCode")
    @Mapping(source = "enrollment.student.fullName", target = "studentName")
    @Mapping(source = "checkedBy.id", target = "checkedById")
    @Mapping(source = "checkedBy.username", target = "checkedByUsername")
    @Mapping(source = "attendanceSession.title", target = "sessionTitle")
    @Mapping(source = "attendanceSession.sessionDate", target = "sessionDate")
    @Mapping(source = "attendanceSession.status", target = "sessionStatus")
    AttendanceRecordResponse toAttendanceRecordResponse(AttendanceRecord record);
}
