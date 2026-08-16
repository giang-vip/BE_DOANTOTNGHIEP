package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.LoginRequest;
import com.hungnhan.school_management.dto.request.ChangePasswordRequest;
import com.hungnhan.school_management.dto.response.AuthResponse;
import com.hungnhan.school_management.dto.response.UserResponse;

import com.hungnhan.school_management.dto.request.UpdateProfileRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    void logout(String bearerToken);
    UserResponse getMe();
    void changePassword(ChangePasswordRequest request);
    UserResponse updateProfile(UpdateProfileRequest request);
}
