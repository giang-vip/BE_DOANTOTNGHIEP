package com.hungnhan.school_management.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String refreshToken;
    private String role;
    private UserInfo userInfo;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String email;
        private String fullName;
        private String avatarUrl;
        
        // Additional Profile details
        private String studentCode;
        private String majorName;
        private String schoolClassName;
        private java.math.BigDecimal gpa;
        private Integer totalCredits;
        private String dateOfBirth;

        private String teacherCode;
        private String departmentName;
        private String title;
    }
}
