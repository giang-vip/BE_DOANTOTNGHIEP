package com.hungnhan.school_management.dto.response;

import com.hungnhan.school_management.constant.EnrollmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {
    private Long id;
    private Long studentId;
    private String studentCode;
    private String studentName;
    private Long classSectionId;
    private String sectionCode;
    private LocalDateTime enrolledAt;
    private EnrollmentStatus status;
    private String note;
}
