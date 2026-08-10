package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.AttendanceRecordRequest;
import com.hungnhan.school_management.dto.request.AttendanceSessionRequest;
import com.hungnhan.school_management.dto.response.AttendanceRecordResponse;
import com.hungnhan.school_management.dto.response.AttendanceSessionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

import java.util.List;

public interface AttendanceService {
    
    PageResponse<AttendanceSessionResponse> getAttendanceSessions(String username, Long classSectionId, int page, int size);
    
    AttendanceSessionResponse createAttendanceSession(String username, Long classSectionId, AttendanceSessionRequest request);
    
    List<AttendanceRecordResponse> getAttendanceRecords(String username, Long sessionId);
    
    AttendanceRecordResponse updateAttendanceRecord(String username, Long recordId, AttendanceRecordRequest request);
}
