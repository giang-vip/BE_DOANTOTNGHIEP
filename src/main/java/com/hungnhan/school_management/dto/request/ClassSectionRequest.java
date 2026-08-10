package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassSectionRequest {

    private Long departmentId;

    private Long majorId;

    @NotNull(message = "ID môn học không được để trống")
    private Long subjectId;

    @NotNull(message = "ID giảng viên không được để trống")
    private Long teacherId;

    @NotBlank(message = "Mã lớp học phần không được để trống")
    private String sectionCode;

    private String room;

    @NotNull(message = "Thứ trong tuần không được để trống")
    @Min(value = 1, message = "Thứ không hợp lệ")
    private Integer weekday;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private LocalTime startTime;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    private LocalTime endTime;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;

    @Min(value = 1, message = "Sĩ số phải lớn hơn 0")
    private Integer capacity;

    private String status;

    private Long semesterId;
}
