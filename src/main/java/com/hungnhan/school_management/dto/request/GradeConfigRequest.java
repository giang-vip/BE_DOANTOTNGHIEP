package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeConfigRequest {

    @NotNull(message = "Trọng số điểm chuyên cần không được để trống")
    @Min(value = 0, message = "Trọng số phải lớn hơn hoặc bằng 0")
    @Max(value = 100, message = "Trọng số không vượt quá 100")
    private Integer attendanceWeight;

    @NotNull(message = "Trọng số điểm giữa kỳ không được để trống")
    @Min(value = 0, message = "Trọng số phải lớn hơn hoặc bằng 0")
    @Max(value = 100, message = "Trọng số không vượt quá 100")
    private Integer midtermWeight;

    @NotNull(message = "Trọng số điểm cuối kỳ không được để trống")
    @Min(value = 0, message = "Trọng số phải lớn hơn hoặc bằng 0")
    @Max(value = 100, message = "Trọng số không vượt quá 100")
    private Integer finalWeight;
}
