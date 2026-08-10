package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.UserCreationRequest;
import com.hungnhan.school_management.dto.request.UserUpdateRequest;
import com.hungnhan.school_management.dto.response.UserResponse;

import com.hungnhan.school_management.dto.response.PageResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);

    UserResponse getUserById(Long id);

    PageResponse<UserResponse> getUsers(String search, String roleName, String status, int page, int size);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
}
