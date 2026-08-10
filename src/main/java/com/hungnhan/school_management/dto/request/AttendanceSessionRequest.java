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
public class AttendanceSessionRequest {

    @NotNull(message = "Ngày điểm danh không được để trống")
    private LocalDate sessionDate;

    @NotBlank(message = "Tiêu đề phiên điểm danh không được để trống")
    private String title;
    
    private String status;
}
