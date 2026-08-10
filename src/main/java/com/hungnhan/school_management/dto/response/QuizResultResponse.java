package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultResponse {
    private Long submissionId;
    private Long assignmentId;
    private String status;
    private BigDecimal totalScore;
    private BigDecimal maxPoints;
    private LocalDateTime submittedAt;
    
    private List<QuizAnswerResponse> answers;
}
