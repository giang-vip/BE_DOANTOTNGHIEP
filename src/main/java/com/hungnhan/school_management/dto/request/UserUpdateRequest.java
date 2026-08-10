package com.hungnhan.school_management.dto.request;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String password;
    private String email;
    private String phone;
    private String fullName;
    private String avatarUrl;
    private String gender;
    private String status; // ACTIVE, INACTIVE, LOCKED
    private Set<String> roles;
}
