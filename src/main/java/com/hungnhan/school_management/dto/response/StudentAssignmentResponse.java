package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAssignmentResponse {
    private Long id;
    private Long classSectionId;
    private String title;
    private String description;
    private LocalDateTime dueAt;
    private BigDecimal maxPoints;
    private String type;
    private String examFileUrl;
    private String examFileName;
    private String examFileType;
    private Integer questionCount;
    
    // Thong tin nop bai cua sinh vien (neu co)
    private Long submissionId;
    private String submissionStatus;
    private BigDecimal submissionScore;
    private LocalDateTime submittedAt;
}
