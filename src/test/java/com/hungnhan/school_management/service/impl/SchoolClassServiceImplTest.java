package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.SchoolClassRequest;
import com.hungnhan.school_management.dto.response.SchoolClassResponse;
import com.hungnhan.school_management.entity.Department;
import com.hungnhan.school_management.entity.Major;
import com.hungnhan.school_management.entity.SchoolClass;
import com.hungnhan.school_management.entity.Teacher;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.SchoolClassMapper;
import com.hungnhan.school_management.repository.DepartmentRepository;
import com.hungnhan.school_management.repository.MajorRepository;
import com.hungnhan.school_management.repository.SchoolClassRepository;
import com.hungnhan.school_management.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SchoolClassServiceImplTest {

    @Mock private SchoolClassRepository schoolClassRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private MajorRepository majorRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private SchoolClassMapper schoolClassMapper;

    @InjectMocks
    private SchoolClassServiceImpl schoolClassService;

    private SchoolClass mockEntity;
    private SchoolClassRequest request;

    @BeforeEach
    void setUp() {
        mockEntity = new SchoolClass();
        mockEntity.setId(1L);
        mockEntity.setCode("D20CQCN01-N");

        request = new SchoolClassRequest();
        request.setCode("D20CQCN01-N");
        request.setMajorId(1L);
        request.setHomeroomTeacherId(1L);
    }

    @Test
    void create_Success() {
        Mockito.when(schoolClassRepository.existsByCode(request.getCode())).thenReturn(false);
        Mockito.when(majorRepository.findById(1L)).thenReturn(Optional.of(new Major()));
        Mockito.when(teacherRepository.findById(1L)).thenReturn(Optional.of(new Teacher()));
        
        Mockito.when(schoolClassMapper.toSchoolClass(request)).thenReturn(mockEntity);
        Mockito.when(schoolClassRepository.save(any())).thenReturn(mockEntity);
        
        SchoolClassResponse mockResponse = new SchoolClassResponse();
        mockResponse.setCode("D20CQCN01-N");
        Mockito.when(schoolClassMapper.toSchoolClassResponse(mockEntity)).thenReturn(mockResponse);

        SchoolClassResponse response = schoolClassService.createClass(request);
        assertEquals("D20CQCN01-N", response.getCode());
    }

    @Test
    void create_CodeExisted_ThrowsException() {
        Mockito.when(schoolClassRepository.existsByCode(request.getCode())).thenReturn(true);
        AppException exception = assertThrows(AppException.class, () -> schoolClassService.createClass(request));
        assertEquals(ErrorCode.CLASS_EXISTED, exception.getErrorCode());
    }
}
