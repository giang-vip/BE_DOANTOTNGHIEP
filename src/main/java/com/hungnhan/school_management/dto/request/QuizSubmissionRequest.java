package com.hungnhan.school_management.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmissionRequest {

    @NotNull(message = "Danh sách câu trả lời không được để trống")
    @Valid
    private List<QuizAnswerRequest> answers;
}
