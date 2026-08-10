package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSessionResponse {
    private Long id;
    private Long classSectionId;
    private String sectionCode;
    private LocalDate sessionDate;
    private String title;
    private String status; // Changed to String or mapped from Enum
    private Long createdById;
    private String createdByUsername;
    private LocalDateTime createdAt;
}
