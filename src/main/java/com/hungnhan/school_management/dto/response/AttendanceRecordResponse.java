package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordResponse {
    private Long id;
    private Long attendanceSessionId;
    private Long enrollmentId;
    private String studentCode;
    private String studentName;
    private String status;
    private String note;
    private LocalDateTime checkedAt;
    private Long checkedById;
    private String checkedByUsername;
}
