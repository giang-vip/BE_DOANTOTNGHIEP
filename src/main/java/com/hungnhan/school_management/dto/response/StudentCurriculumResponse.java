package com.hungnhan.school_management.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCurriculumResponse {
    private Long subjectId;
    private String subjectCode;
    private String subjectName;
    private Integer credits;
    private Integer semesterIndex;
    private String subjectType;
}
