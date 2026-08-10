package com.hungnhan.school_management.dto.response;

import com.hungnhan.school_management.constant.Gender;
import com.hungnhan.school_management.constant.TeacherStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponse {
    private Long id;
    private Long userId;
    private String username;
    private String teacherCode;
    private String fullName;
    private Gender gender;
    private Long departmentId;
    private String departmentName;
    private String title;
    private TeacherStatus status;
}
