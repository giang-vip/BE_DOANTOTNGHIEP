package com.hungnhan.school_management.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {
    private Long id;
    private String code;
    private String name;
    private Integer credits;
    private Long departmentId;
    private String departmentName;
    private Long majorId;
    private String majorName;
    private Integer semesterIndex;
    private String type;
    private String description;
}
