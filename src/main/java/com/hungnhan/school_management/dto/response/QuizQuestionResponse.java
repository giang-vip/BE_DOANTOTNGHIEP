package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestionResponse {
    private Long id;
    private Long assignmentId;
    private Integer orderIndex;
    private String questionText;
    private String choiceAText;
    private String choiceBText;
    private String choiceCText;
    private String choiceDText;
    private String correctChoice;
    private BigDecimal points;
    private String explanationText;
    private String ocrStatus;
    private String ocrExtractedText;
}
