package com.hungnhan.school_management.dto.request;

import com.hungnhan.school_management.constant.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {

    @NotNull(message = "ID người dùng không được để trống")
    private Long userId;

    @NotBlank(message = "Mã sinh viên không được để trống")
    private String studentCode;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String address;

    @NotNull(message = "ID ngành học không được để trống")
    private Long majorId;

    private Long classId;

    private String status;
}
