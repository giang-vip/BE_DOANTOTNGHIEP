package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClassRequest {

    @NotBlank(message = "Mã lớp không được để trống")
    private String code;

    @NotBlank(message = "Tên lớp không được để trống")
    private String name;

    @NotNull(message = "ID ngành học không được để trống")
    private Long majorId;

    private Long entryAcademicYearId;

    private Long homeroomTeacherId;

    private String status;
}
