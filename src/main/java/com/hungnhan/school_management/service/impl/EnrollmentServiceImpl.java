package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.constant.EnrollmentStatus;
import com.hungnhan.school_management.dto.request.EnrollmentRequest;
import com.hungnhan.school_management.dto.response.EnrollmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.ClassSection;
import com.hungnhan.school_management.entity.Enrollment;
import com.hungnhan.school_management.entity.Student;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.EnrollmentMapper;
import com.hungnhan.school_management.repository.ClassSectionRepository;
import com.hungnhan.school_management.repository.EnrollmentRepository;
import com.hungnhan.school_management.repository.StudentRepository;
import com.hungnhan.school_management.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ClassSectionRepository classSectionRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public EnrollmentResponse createEnrollment(EnrollmentRequest request) {
        if (enrollmentRepository.existsByStudentIdAndClassSectionId(request.getStudentId(), request.getClassSectionId())) {
            throw new AppException(ErrorCode.ENROLLMENT_EXISTED);
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        ClassSection classSection = classSectionRepository.findById(request.getClassSectionId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        long currentCount = enrollmentRepository.countActiveEnrollmentsByClassSectionId(classSection.getId());
        if (currentCount >= classSection.getCapacity()) {
            throw new AppException(ErrorCode.CLASS_SECTION_FULL);
        }

        Enrollment enrollment = enrollmentMapper.toEnrollment(request);
        enrollment.setStudent(student);
        enrollment.setClassSection(classSection);

        if (request.getStatus() != null) {
            enrollment.setStatus(EnrollmentStatus.valueOf(request.getStatus()));
        }

        return enrollmentMapper.toEnrollmentResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponse updateEnrollment(Long id, EnrollmentRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENROLLMENT_NOT_FOUND));

        if (!enrollment.getStudent().getId().equals(request.getStudentId()) || !enrollment.getClassSection().getId().equals(request.getClassSectionId())) {
            if (enrollmentRepository.existsByStudentIdAndClassSectionId(request.getStudentId(), request.getClassSectionId())) {
                throw new AppException(ErrorCode.ENROLLMENT_EXISTED);
            }
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        ClassSection classSection = classSectionRepository.findById(request.getClassSectionId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        if (!enrollment.getClassSection().getId().equals(request.getClassSectionId())) {
            long currentCount = enrollmentRepository.countActiveEnrollmentsByClassSectionId(classSection.getId());
            if (currentCount >= classSection.getCapacity()) {
                throw new AppException(ErrorCode.CLASS_SECTION_FULL);
            }
        }

        enrollmentMapper.updateEnrollment(enrollment, request);
        enrollment.setStudent(student);
        enrollment.setClassSection(classSection);

        if (request.getStatus() != null) {
            enrollment.setStatus(EnrollmentStatus.valueOf(request.getStatus()));
        }

        return enrollmentMapper.toEnrollmentResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    public PageResponse<EnrollmentResponse> getEnrollments(Long studentId, Long classSectionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollmentPage = enrollmentRepository.searchEnrollments(studentId, classSectionId, pageable);

        List<EnrollmentResponse> content = enrollmentPage.getContent().stream()
                .map(enrollmentMapper::toEnrollmentResponse)
                .collect(Collectors.toList());

        return PageResponse.<EnrollmentResponse>builder()
                .content(content)
                .pageNumber(enrollmentPage.getNumber())
                .pageSize(enrollmentPage.getSize())
                .totalElements(enrollmentPage.getTotalElements())
                .totalPages(enrollmentPage.getTotalPages())
                .last(enrollmentPage.isLast())
                .build();
    }

    @Override
    public EnrollmentResponse getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENROLLMENT_NOT_FOUND));
        return enrollmentMapper.toEnrollmentResponse(enrollment);
    }

    @Override
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENROLLMENT_NOT_FOUND));
        enrollmentRepository.delete(enrollment);
    }
}
