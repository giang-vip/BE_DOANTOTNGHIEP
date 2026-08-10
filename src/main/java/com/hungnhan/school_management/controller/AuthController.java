package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.LoginRequest;
import com.hungnhan.school_management.dto.request.ChangePasswordRequest;
import com.hungnhan.school_management.dto.response.AuthResponse;
import com.hungnhan.school_management.dto.response.UserResponse;
import com.hungnhan.school_management.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Management", description = "API xác thực và quản lý tài khoản cá nhân")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập hệ thống (API_AUTH_01)", description = "Xác thực tài khoản và trả về JWT Token cùng vai trò.")
    public ApiResponse<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.<AuthResponse>builder()
                .result(authService.login(request))
                .build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất tài khoản (API_AUTH_02)", description = "Vô hiệu hóa JWT Token hiện tại.")
    public ApiResponse<Map<String, Object>> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ApiResponse.<Map<String, Object>>builder()
                .result(Map.of(
                        "success", true,
                        "message", "Đăng xuất thành công"
                ))
                .build();
    }

    @GetMapping("/me")
    @Operation(summary = "Lấy thông tin tài khoản hiện tại (API_AUTH_03)", description = "Giải mã JWT token và trả về thông tin chi tiết người dùng đăng nhập.")
    public ApiResponse<UserResponse> getMe() {
        return ApiResponse.<UserResponse>builder()
                .result(authService.getMe())
                .build();
    }

    @PutMapping("/change-password")
    @Operation(summary = "Đổi mật khẩu tài khoản (API_AUTH_04)", description = "Đổi mật khẩu cho tài khoản đang đăng nhập.")
    public ApiResponse<Map<String, Object>> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);
        return ApiResponse.<Map<String, Object>>builder()
                .result(Map.of(
                        "success", true,
                        "message", "Đổi mật khẩu thành công"
                ))
                .build();
    }
}
