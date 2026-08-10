package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponse {
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
    private Long createdById;
    private String createdByUsername;
    private LocalDateTime createdAt;
}
