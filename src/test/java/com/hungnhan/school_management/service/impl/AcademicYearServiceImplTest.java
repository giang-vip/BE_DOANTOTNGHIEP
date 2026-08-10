package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.AcademicYearRequest;
import com.hungnhan.school_management.dto.response.AcademicYearResponse;
import com.hungnhan.school_management.entity.AcademicYear;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.AcademicYearMapper;
import com.hungnhan.school_management.repository.AcademicYearRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class AcademicYearServiceImplTest {

    @Mock
    private AcademicYearRepository academicYearRepository;

    @Mock
    private AcademicYearMapper academicYearMapper;

    @InjectMocks
    private AcademicYearServiceImpl academicYearService;

    private AcademicYear mockEntity;
    private AcademicYearRequest request;

    @BeforeEach
    void setUp() {
        mockEntity = new AcademicYear();
        mockEntity.setId(1L);
        mockEntity.setCode("K2023");
        
        request = new AcademicYearRequest();
        request.setCode("K2023");
        request.setStartDate(LocalDate.of(2023, 9, 1));
        request.setEndDate(LocalDate.of(2024, 6, 30));
    }

    @Test
    void createAcademicYear_Success() {
        Mockito.when(academicYearRepository.existsByCode(request.getCode())).thenReturn(false);
        Mockito.when(academicYearMapper.toAcademicYear(request)).thenReturn(mockEntity);
        Mockito.when(academicYearRepository.save(any())).thenReturn(mockEntity);
        
        AcademicYearResponse mockResponse = new AcademicYearResponse();
        mockResponse.setCode("K2023");
        Mockito.when(academicYearMapper.toAcademicYearResponse(mockEntity)).thenReturn(mockResponse);

        AcademicYearResponse response = academicYearService.createAcademicYear(request);
        assertEquals("K2023", response.getCode());
    }

    @Test
    void createAcademicYear_CodeExisted_ThrowsException() {
        Mockito.when(academicYearRepository.existsByCode(request.getCode())).thenReturn(true);
        AppException exception = assertThrows(AppException.class, () -> academicYearService.createAcademicYear(request));
        assertEquals(ErrorCode.ACADEMIC_YEAR_EXISTED, exception.getErrorCode());
    }
}
