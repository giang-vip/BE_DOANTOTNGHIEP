package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.response.RoleResponse;
import com.hungnhan.school_management.mapper.RoleMapper;
import com.hungnhan.school_management.repository.RoleRepository;
import com.hungnhan.school_management.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleResponse)
                .collect(Collectors.toList());
    }
}
