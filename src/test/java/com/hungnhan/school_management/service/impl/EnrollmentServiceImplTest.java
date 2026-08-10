package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.constant.EnrollmentStatus;
import com.hungnhan.school_management.dto.request.EnrollmentRequest;
import com.hungnhan.school_management.dto.response.EnrollmentResponse;
import com.hungnhan.school_management.entity.ClassSection;
import com.hungnhan.school_management.entity.Enrollment;
import com.hungnhan.school_management.entity.Student;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.EnrollmentMapper;
import com.hungnhan.school_management.repository.ClassSectionRepository;
import com.hungnhan.school_management.repository.EnrollmentRepository;
import com.hungnhan.school_management.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ClassSectionRepository classSectionRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private Student mockStudent;
    private ClassSection mockClassSection;

    @BeforeEach
    void setUp() {
        mockStudent = new Student();
        mockStudent.setId(1L);

        mockClassSection = new ClassSection();
        mockClassSection.setId(10L);
        mockClassSection.setCapacity(50);
        mockClassSection.setStartDate(LocalDate.of(2026, 8, 1));
        mockClassSection.setEndDate(LocalDate.of(2026, 12, 1));
        mockClassSection.setWeekday(2);
        mockClassSection.setStartTime(LocalTime.of(8, 0));
        mockClassSection.setEndTime(LocalTime.of(10, 0));
    }

    @Test
    void createEnrollment_Success() {
        EnrollmentRequest request = EnrollmentRequest.builder()
                .studentId(1L)
                .classSectionId(10L)
                .note("Note")
                .build();
        
        when(studentRepository.findById(1L)).thenReturn(Optional.of(mockStudent));
        when(classSectionRepository.findById(10L)).thenReturn(Optional.of(mockClassSection));
        when(enrollmentRepository.existsByStudentIdAndClassSectionId(1L, 10L)).thenReturn(false);
        when(enrollmentRepository.countActiveEnrollmentsByClassSectionId(10L)).thenReturn(40L);

        Enrollment savedEnrollment = new Enrollment();
        savedEnrollment.setId(100L);
        
        when(enrollmentMapper.toEnrollment(request)).thenReturn(new Enrollment());
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(savedEnrollment);
        when(enrollmentMapper.toEnrollmentResponse(savedEnrollment)).thenReturn(new EnrollmentResponse());

        EnrollmentResponse response = enrollmentService.createEnrollment(request);

        assertNotNull(response);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void createEnrollment_AlreadyEnrolled_ThrowsException() {
        EnrollmentRequest request = EnrollmentRequest.builder()
                .studentId(1L)
                .classSectionId(10L)
                .note("Note")
                .build();
        
        when(enrollmentRepository.existsByStudentIdAndClassSectionId(1L, 10L)).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> enrollmentService.createEnrollment(request));
        assertEquals(ErrorCode.ENROLLMENT_EXISTED, exception.getErrorCode());
        
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void createEnrollment_ClassFull_ThrowsException() {
        EnrollmentRequest request = EnrollmentRequest.builder()
                .studentId(1L)
                .classSectionId(10L)
                .note("Note")
                .build();
        
        when(studentRepository.findById(1L)).thenReturn(Optional.of(mockStudent));
        when(classSectionRepository.findById(10L)).thenReturn(Optional.of(mockClassSection));
        when(enrollmentRepository.existsByStudentIdAndClassSectionId(1L, 10L)).thenReturn(false);
        when(enrollmentRepository.countActiveEnrollmentsByClassSectionId(10L)).thenReturn(50L);

        AppException exception = assertThrows(AppException.class, () -> enrollmentService.createEnrollment(request));
        assertEquals(ErrorCode.CLASS_SECTION_FULL, exception.getErrorCode());
        
        verify(enrollmentRepository, never()).save(any());
    }
}
