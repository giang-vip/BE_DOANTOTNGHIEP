package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.UserCreationRequest;
import com.hungnhan.school_management.dto.request.UserUpdateRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.UserResponse;
import com.hungnhan.school_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * UserController - Quản lý tài khoản người dùng trong hệ thống.
 * 
 * @author Nguyễn Trường Giang
 * @version 1.0.0
 * @since 2026-08-08
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API quản lý thông tin người dùng trong hệ thống (Tác giả: Nguyễn Hùng Nhân)")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Tạo mới người dùng (POST)", description = "Đăng ký hoặc tạo tài khoản người dùng mới với các thông tin cơ bản và vai trò đi kèm.")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả người dùng (GET)", description = "Trả về danh sách tài khoản người dùng trong hệ thống có phân trang và lọc.")
    public ApiResponse<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .result(userService.getUsers(search, roleName, status, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết người dùng theo ID (GET)", description = "Trả về thông tin chi tiết của một người dùng cụ thể dựa trên ID.")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin người dùng (PUT)", description = "Cập nhật các thông tin như Họ tên, Email, Số điện thoại, Avatar, Trạng thái hoạt động hoặc danh sách Vai trò dựa trên ID.")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa người dùng (DELETE)", description = "Xóa vĩnh viễn tài khoản người dùng khỏi hệ thống theo ID.")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.<String>builder()
                .result("Người dùng đã được xóa thành công")
                .build();
    }
}
