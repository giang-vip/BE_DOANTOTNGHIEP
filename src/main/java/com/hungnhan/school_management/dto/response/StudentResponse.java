package com.hungnhan.school_management.dto.response;

import com.hungnhan.school_management.constant.Gender;
import com.hungnhan.school_management.constant.StudentStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private Long userId;
    private String username;
    private String studentCode;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String address;
    private Long majorId;
    private String majorName;
    private Long classId;
    private String classCode;
    private StudentStatus status;
}
