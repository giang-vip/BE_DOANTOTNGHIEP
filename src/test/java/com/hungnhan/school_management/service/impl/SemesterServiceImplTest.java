package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.SemesterRequest;
import com.hungnhan.school_management.dto.response.SemesterResponse;
import com.hungnhan.school_management.entity.AcademicYear;
import com.hungnhan.school_management.entity.Semester;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.SemesterMapper;
import com.hungnhan.school_management.repository.AcademicYearRepository;
import com.hungnhan.school_management.repository.SemesterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SemesterServiceImplTest {

    @Mock
    private SemesterRepository semesterRepository;
    @Mock
    private AcademicYearRepository academicYearRepository;
    @Mock
    private SemesterMapper semesterMapper;

    @InjectMocks
    private SemesterServiceImpl semesterService;

    private Semester mockEntity;
    private SemesterRequest request;
    private AcademicYear mockYear;

    @BeforeEach
    void setUp() {
        mockYear = new AcademicYear();
        mockYear.setId(1L);

        mockEntity = new Semester();
        mockEntity.setId(1L);
        mockEntity.setCode("HK1");
        mockEntity.setAcademicYear(mockYear);
        
        request = new SemesterRequest();
        request.setCode("HK1");
        request.setAcademicYearId(1L);
        request.setStartDate(LocalDate.of(2023, 9, 1));
        request.setEndDate(LocalDate.of(2024, 1, 30));
    }

    @Test
    void create_Success() {
        Mockito.when(academicYearRepository.findById(1L)).thenReturn(Optional.of(mockYear));
        Mockito.when(semesterRepository.existsByAcademicYearIdAndCode(1L, "HK1")).thenReturn(false);
        Mockito.when(semesterMapper.toSemester(request)).thenReturn(mockEntity);
        Mockito.when(semesterRepository.save(any())).thenReturn(mockEntity);
        
        SemesterResponse mockResponse = new SemesterResponse();
        mockResponse.setCode("HK1");
        Mockito.when(semesterMapper.toSemesterResponse(mockEntity)).thenReturn(mockResponse);

        SemesterResponse response = semesterService.createSemester(request);
        assertEquals("HK1", response.getCode());
    }

    @Test
    void create_CodeExisted_ThrowsException() {
        Mockito.when(academicYearRepository.findById(1L)).thenReturn(Optional.of(mockYear));
        Mockito.when(semesterRepository.existsByAcademicYearIdAndCode(1L, "HK1")).thenReturn(true);
        
        AppException exception = assertThrows(AppException.class, () -> semesterService.createSemester(request));
        assertEquals(ErrorCode.SEMESTER_EXISTED, exception.getErrorCode());
    }
}
