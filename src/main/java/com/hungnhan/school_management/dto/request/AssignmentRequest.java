package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentRequest {

    @NotBlank(message = "Tiêu đề bài tập không được để trống")
    private String title;

    private String description;

    @NotNull(message = "Hạn nộp không được để trống")
    private LocalDateTime dueAt;

    @NotNull(message = "Điểm tối đa không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Điểm tối đa phải lớn hơn 0")
    private BigDecimal maxPoints;

    @NotBlank(message = "Loại bài tập không được để trống (essay/quiz)")
    private String type; // essay, quiz

    private String examFileUrl;
    
    private String examFileName;
    
    private String examFileType;
    
    private Integer questionCount;
}
