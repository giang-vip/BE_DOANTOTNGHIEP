package com.hungnhan.school_management.dto.request;

import com.hungnhan.school_management.constant.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRequest {
    @NotNull(message = "ID người dùng không được để trống")
    private Long userId;

    @NotBlank(message = "Mã giảng viên không được để trống")
    private String teacherCode;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private Gender gender;

    private Long departmentId;

    private String title;

    private String status;
}
