package com.hungnhan.school_management.dto.response;

import com.hungnhan.school_management.constant.ClassStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClassResponse {
    private Long id;
    private String code;
    private String name;
    private Long majorId;
    private String majorName;
    private Long entryAcademicYearId;
    private String entryAcademicYearCode;
    private Long homeroomTeacherId;
    private String homeroomTeacherName;
    private ClassStatus status;
}
