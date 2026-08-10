package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.DepartmentRequest;
import com.hungnhan.school_management.dto.response.DepartmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.Department;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.DepartmentMapper;
import com.hungnhan.school_management.repository.DepartmentRepository;
import com.hungnhan.school_management.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.DEPARTMENT_EXISTED);
        }

        Department department = departmentMapper.toDepartment(request);
        return departmentMapper.toDepartmentResponse(departmentRepository.save(department));
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        if (!department.getCode().equals(request.getCode()) && departmentRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.DEPARTMENT_EXISTED);
        }

        departmentMapper.updateDepartment(department, request);
        return departmentMapper.toDepartmentResponse(departmentRepository.save(department));
    }

    @Override
    public PageResponse<DepartmentResponse> getDepartments(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departmentPage = departmentRepository.searchDepartments(search, pageable);

        List<DepartmentResponse> content = departmentPage.getContent().stream()
                .map(departmentMapper::toDepartmentResponse)
                .collect(Collectors.toList());

        return PageResponse.<DepartmentResponse>builder()
                .content(content)
                .pageNumber(departmentPage.getNumber())
                .pageSize(departmentPage.getSize())
                .totalElements(departmentPage.getTotalElements())
                .totalPages(departmentPage.getTotalPages())
                .last(departmentPage.isLast())
                .build();
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        return departmentMapper.toDepartmentResponse(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        try {
            departmentRepository.delete(department);
            departmentRepository.flush(); // Flush để bắt ngoại lệ constraint ngay lập tức
        } catch (DataIntegrityViolationException ex) {
            log.error("Lỗi xóa khoa, đang có dữ liệu liên kết: ", ex);
            throw new AppException(ErrorCode.DEPARTMENT_HAS_REFERENCES);
        }
    }
}
