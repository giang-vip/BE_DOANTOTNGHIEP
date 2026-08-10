package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponse {
    private Long id;
    private Long assignmentId;
    private Long enrollmentId;
    private String studentCode;
    private String studentName;
    private String content;
    private String fileUrl;
    private LocalDateTime submittedAt;
    private BigDecimal score;
    private String feedback;
    private String status;
}
