package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestionRequest {

    @NotNull(message = "Thứ tự câu hỏi không được để trống")
    private Integer orderIndex;

    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    private String questionText;

    private String choiceAText;
    private String choiceBText;
    private String choiceCText;
    private String choiceDText;

    @NotBlank(message = "Đáp án đúng không được để trống")
    private String correctChoice; // A, B, C, D

    @NotNull(message = "Điểm câu hỏi không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Điểm phải lớn hơn 0")
    private BigDecimal points;

    private String explanationText;
}
