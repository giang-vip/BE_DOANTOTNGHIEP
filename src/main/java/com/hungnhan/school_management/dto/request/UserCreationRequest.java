package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {

    @Size(min = 3, message = "USERNAME_INVALID")
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @Email(message = "Email không đúng định dạng")
    @NotBlank(message = "Email không được để trống")
    private String email;

    private String phone;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    private String avatarUrl;

    private String gender;

    private Set<String> roles; // Danh sách tên vai trò (ví dụ: "TEACHER", "STUDENT")
}
