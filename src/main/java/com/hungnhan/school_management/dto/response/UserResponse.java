package com.hungnhan.school_management.dto.response;

import com.hungnhan.school_management.entity.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String fullName;
    private String avatarUrl;
    private String gender;
    private String status;
    private Set<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional Profile details
    private String studentCode;
    private String majorName;
    private String schoolClassName;
    private java.math.BigDecimal gpa;
    private Integer totalCredits;
    private String dateOfBirth;
    private Integer majorTotalCredits;
    private Integer entryStartYear;

    private String teacherCode;
    private String departmentName;
    private String title;
}
