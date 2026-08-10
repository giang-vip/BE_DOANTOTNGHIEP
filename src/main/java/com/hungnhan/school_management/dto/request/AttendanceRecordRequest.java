package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordRequest {

    @NotBlank(message = "Trạng thái điểm danh không được để trống")
    private String status; // PRESENT, ABSENT, LATE, EXCUSED

    private String note;
}
