package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.response.RoleResponse;
import com.hungnhan.school_management.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
}
