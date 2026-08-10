package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerRequest {

    @NotNull(message = "ID câu hỏi không được để trống")
    private Long questionId;

    private String selectedChoice; // A, B, C, D hoac null (bo qua)
}
