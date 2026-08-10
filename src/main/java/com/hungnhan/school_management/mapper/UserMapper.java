package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.UserCreationRequest;
import com.hungnhan.school_management.dto.request.UserUpdateRequest;
import com.hungnhan.school_management.dto.response.UserResponse;
import com.hungnhan.school_management.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Chuyển DTO Request -> Entity khi tạo mới (chưa mã hóa password và set roles)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    // Chuyển Entity -> DTO Response để trả về Client
    UserResponse toUserResponse(User user);

    // Cập nhật thông tin từ DTO Update vào Entity sẵn có
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
