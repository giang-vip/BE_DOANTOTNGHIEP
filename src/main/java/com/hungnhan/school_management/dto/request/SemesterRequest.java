package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterRequest {
    @NotNull(message = "ID năm học không được để trống")
    private Long academicYearId;

    @NotBlank(message = "Mã học kỳ không được để trống")
    private String code;

    @NotBlank(message = "Tên học kỳ không được để trống")
    private String name;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;

    private Boolean isCurrent;
}
