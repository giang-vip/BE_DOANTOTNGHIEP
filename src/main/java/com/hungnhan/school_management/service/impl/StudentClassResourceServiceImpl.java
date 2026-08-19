package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.response.AttendanceRecordResponse;
import com.hungnhan.school_management.dto.response.LearningMaterialResponse;
import com.hungnhan.school_management.dto.response.DocumentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.*;
import com.hungnhan.school_management.constant.SessionStatus;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.AttendanceMapper;
import com.hungnhan.school_management.mapper.LearningMaterialMapper;
import com.hungnhan.school_management.mapper.DocumentMapper;
import com.hungnhan.school_management.repository.*;
import com.hungnhan.school_management.service.StudentClassResourceService;
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
public class StudentClassResourceServiceImpl implements StudentClassResourceService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final LearningMaterialRepository learningMaterialRepository;
    private final AttendanceSessionRepository attendanceSessionRepository;
    private final DocumentRepository documentRepository;
    private final AttendanceMapper attendanceMapper;
    private final LearningMaterialMapper learningMaterialMapper;
    private final DocumentMapper documentMapper;

    private Student getStudentByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
    }

    private Enrollment getEnrollmentOrThrow(Student student, Long classSectionId) {
        return enrollmentRepository.searchEnrollments(student.getId(), classSectionId, Pageable.unpaged())
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED)); // Khong phai hoc vien cua lop
    }

    @Override
    public List<AttendanceRecordResponse> getMyAttendance(String username, Long classSectionId) {
        Student student = getStudentByUsername(username);
        Enrollment enrollment = getEnrollmentOrThrow(student, classSectionId);

        List<AttendanceRecord> records = attendanceRecordRepository.findByEnrollmentId(enrollment.getId());

        return records.stream()
                .map(attendanceMapper::toAttendanceRecordResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<LearningMaterialResponse> getMyMaterials(String username, Long classSectionId, int page, int size) {
        Student student = getStudentByUsername(username);
        getEnrollmentOrThrow(student, classSectionId); // Kiem tra quyen

        Pageable pageable = PageRequest.of(page, size);
        Page<LearningMaterial> materialPage = learningMaterialRepository.findByClassSectionIdOrderByUploadedAtDesc(classSectionId, pageable);

        List<LearningMaterialResponse> content = materialPage.getContent().stream()
                .map(learningMaterialMapper::toLearningMaterialResponse)
                .collect(Collectors.toList());

        return PageResponse.<LearningMaterialResponse>builder()
                .content(content)
                .pageNumber(materialPage.getNumber())
                .pageSize(materialPage.getSize())
                .totalElements(materialPage.getTotalElements())
                .totalPages(materialPage.getTotalPages())
                .last(materialPage.isLast())
                .build();
    }

    @Override
    public PageResponse<DocumentResponse> getMySubjectMaterials(String username, Long classSectionId, int page, int size) {
        Student student = getStudentByUsername(username);
        Enrollment enrollment = getEnrollmentOrThrow(student, classSectionId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Document> documentPage = documentRepository.findByOwnerTypeAndOwnerIdOrderByUploadedAtDesc("SUBJECT", enrollment.getClassSection().getSubject().getId(), pageable);

        List<DocumentResponse> content = documentPage.getContent().stream()
                .map(documentMapper::toDocumentResponse)
                .collect(Collectors.toList());

        return PageResponse.<DocumentResponse>builder()
                .content(content)
                .pageNumber(documentPage.getNumber())
                .pageSize(documentPage.getSize())
                .totalElements(documentPage.getTotalElements())
                .totalPages(documentPage.getTotalPages())
                .last(documentPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public AttendanceRecordResponse checkIn(String username, Long classSectionId, Long sessionId) {
        Student student = getStudentByUsername(username);
        Enrollment enrollment = getEnrollmentOrThrow(student, classSectionId);

        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException(ErrorCode.SESSION_NOT_FOUND));

        if (session.getStatus() != SessionStatus.OPEN) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        AttendanceRecord record = attendanceRecordRepository.findByAttendanceSessionIdAndEnrollmentId(sessionId, enrollment.getId())
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_FOUND));

        record.setStatus(AttendanceRecord.AttendanceStatus.PRESENT);
        record.setCheckedAt(java.time.LocalDateTime.now());
        record.setCheckedBy(student.getUser());

        return attendanceMapper.toAttendanceRecordResponse(attendanceRecordRepository.save(record));
    }
}
