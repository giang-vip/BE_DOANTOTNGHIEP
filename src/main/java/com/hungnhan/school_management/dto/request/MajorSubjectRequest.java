package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MajorSubjectRequest {

    @NotNull(message = "Mã môn học không được để trống")
    private Long subjectId;

    private Integer semesterIndex;

    private String type; // COMPULSORY, ELECTIVE, EQUIVALENT
}
