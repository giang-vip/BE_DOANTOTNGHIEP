package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.DepartmentRequest;
import com.hungnhan.school_management.dto.response.DepartmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.Department;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.DepartmentMapper;
import com.hungnhan.school_management.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department mockDepartment;
    private DepartmentRequest request;

    @BeforeEach
    void setUp() {
        mockDepartment = new Department();
        mockDepartment.setId(1L);
        mockDepartment.setCode("CNTT");
        mockDepartment.setName("Khoa Công nghệ thông tin");

        request = new DepartmentRequest();
        request.setCode("CNTT");
        request.setName("Khoa Công nghệ thông tin");
    }

    @Test
    void createDepartment_Success() {
        Mockito.when(departmentRepository.existsByCode(request.getCode())).thenReturn(false);
        Mockito.when(departmentMapper.toDepartment(request)).thenReturn(mockDepartment);
        Mockito.when(departmentRepository.save(any(Department.class))).thenReturn(mockDepartment);
        
        DepartmentResponse mockResponse = new DepartmentResponse();
        mockResponse.setCode("CNTT");
        Mockito.when(departmentMapper.toDepartmentResponse(mockDepartment)).thenReturn(mockResponse);

        DepartmentResponse response = departmentService.createDepartment(request);

        assertNotNull(response);
        assertEquals("CNTT", response.getCode());
    }

    @Test
    void createDepartment_CodeExisted_ThrowsException() {
        Mockito.when(departmentRepository.existsByCode(request.getCode())).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> departmentService.createDepartment(request));
        assertEquals(ErrorCode.DEPARTMENT_EXISTED, exception.getErrorCode());
    }

    @Test
    void updateDepartment_Success() {
        Mockito.when(departmentRepository.findById(1L)).thenReturn(Optional.of(mockDepartment));
        Mockito.when(departmentRepository.save(mockDepartment)).thenReturn(mockDepartment);
        
        DepartmentResponse mockResponse = new DepartmentResponse();
        mockResponse.setCode("CNTT");
        Mockito.when(departmentMapper.toDepartmentResponse(mockDepartment)).thenReturn(mockResponse);

        DepartmentResponse response = departmentService.updateDepartment(1L, request);

        assertNotNull(response);
        assertEquals("CNTT", response.getCode());
    }

    @Test
    void deleteDepartment_Success() {
        Mockito.when(departmentRepository.findById(1L)).thenReturn(Optional.of(mockDepartment));
        Mockito.doNothing().when(departmentRepository).delete(mockDepartment);

        assertDoesNotThrow(() -> departmentService.deleteDepartment(1L));
    }

    @Test
    void deleteDepartment_NotFound_ThrowsException() {
        Mockito.when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> departmentService.deleteDepartment(99L));
        assertEquals(ErrorCode.DEPARTMENT_NOT_FOUND, exception.getErrorCode());
    }
}
