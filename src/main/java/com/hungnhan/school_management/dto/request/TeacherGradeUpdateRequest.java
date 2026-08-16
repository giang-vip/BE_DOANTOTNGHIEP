package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherGradeUpdateRequest {

    @NotNull(message = "ID đăng ký không được để trống")
    private Long enrollmentId;

    @DecimalMin(value = "0.0", message = "Điểm phải lớn hơn hoặc bằng 0")
    @DecimalMax(value = "10.0", message = "Điểm không được vượt quá 10")
    private BigDecimal attendanceScore;

    @DecimalMin(value = "0.0", message = "Điểm phải lớn hơn hoặc bằng 0")
    @DecimalMax(value = "10.0", message = "Điểm không được vượt quá 10")
    private BigDecimal midtermScore;

    @DecimalMin(value = "0.0", message = "Điểm phải lớn hơn hoặc bằng 0")
    @DecimalMax(value = "10.0", message = "Điểm không được vượt quá 10")
    private BigDecimal finalExamScore;
}
