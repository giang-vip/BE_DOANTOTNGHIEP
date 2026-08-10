package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerResponse {
    private Long questionId;
    private Integer orderIndex;
    private String questionText;
    private String selectedChoice;
    private String correctChoice;
    private Boolean isCorrect;
    private BigDecimal pointsAwarded;
    private String explanationText;
}
