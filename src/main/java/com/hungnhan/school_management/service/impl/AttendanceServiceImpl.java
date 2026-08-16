package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.AttendanceRecordRequest;
import com.hungnhan.school_management.dto.request.AttendanceSessionRequest;
import com.hungnhan.school_management.dto.response.AttendanceRecordResponse;
import com.hungnhan.school_management.dto.response.AttendanceSessionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.*;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.constant.SessionStatus;
import com.hungnhan.school_management.mapper.AttendanceMapper;
import com.hungnhan.school_management.repository.*;
import com.hungnhan.school_management.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceSessionRepository attendanceSessionRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final ClassSectionRepository classSectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final AttendanceMapper attendanceMapper;

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void checkTeacherPermission(User user, ClassSection classSection) {
        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        if (!classSection.getTeacher().getId().equals(teacher.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AttendanceSessionResponse> getAttendanceSessions(String username, Long classSectionId, int page,
            int size) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        Pageable pageable = PageRequest.of(page, size);
        Page<AttendanceSession> sessionPage = attendanceSessionRepository
                .findByClassSectionIdOrderBySessionDateDesc(classSectionId, pageable);

        List<AttendanceSessionResponse> content = sessionPage.getContent().stream()
                .map(attendanceMapper::toAttendanceSessionResponse)
                .collect(Collectors.toList());

        return PageResponse.<AttendanceSessionResponse>builder()
                .content(content)
                .pageNumber(sessionPage.getNumber())
                .pageSize(sessionPage.getSize())
                .totalElements(sessionPage.getTotalElements())
                .totalPages(sessionPage.getTotalPages())
                .last(sessionPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public AttendanceSessionResponse createAttendanceSession(String username, Long classSectionId,
            AttendanceSessionRequest request) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        if (attendanceSessionRepository.existsByClassSectionIdAndSessionDate(classSectionId,
                request.getSessionDate())) {
            throw new AppException(ErrorCode.SESSION_EXISTED);
        }

        AttendanceSession session = attendanceMapper.toAttendanceSession(request);
        session.setClassSection(classSection);
        session.setCreatedBy(user);

        if (request.getStatus() != null) {
            session.setStatus(SessionStatus.valueOf(request.getStatus()));
        }

        AttendanceSession savedSession = attendanceSessionRepository.save(session);

        // Tự động tạo record trống (ABSENT) cho tất cả sinh viên trong lớp
        Pageable unpaged = Pageable.unpaged(); // Lấy tất cả
        Page<Enrollment> enrollments = enrollmentRepository.searchEnrollments(null, classSectionId, unpaged);

        List<AttendanceRecord> records = new ArrayList<>();
        for (Enrollment enrollment : enrollments.getContent()) {
            if (enrollment.getStatus() == com.hungnhan.school_management.constant.EnrollmentStatus.ACTIVE) {
                AttendanceRecord record = AttendanceRecord.builder()
                        .attendanceSession(savedSession)
                        .enrollment(enrollment)
                        .status(AttendanceRecord.AttendanceStatus.ABSENT) // Mặc định là vắng mặt, GV sẽ điểm danh sau
                        .build();
                records.add(record);
            }
        }

        attendanceRecordRepository.saveAll(records);

        return attendanceMapper.toAttendanceSessionResponse(savedSession);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecordResponse> getAttendanceRecords(String username, Long sessionId) {
        User user = getUserByUsername(username);
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException(ErrorCode.SESSION_NOT_FOUND));

        checkTeacherPermission(user, session.getClassSection());

        List<AttendanceRecord> records = attendanceRecordRepository.findByAttendanceSessionId(sessionId);

        return records.stream()
                .map(attendanceMapper::toAttendanceRecordResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AttendanceRecordResponse updateAttendanceRecord(String username, Long recordId,
            AttendanceRecordRequest request) {
        User user = getUserByUsername(username);
        AttendanceRecord record = attendanceRecordRepository.findById(recordId)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_FOUND));

        checkTeacherPermission(user, record.getAttendanceSession().getClassSection());

        record.setStatus(AttendanceRecord.AttendanceStatus.valueOf(request.getStatus()));
        record.setNote(request.getNote());
        record.setCheckedAt(LocalDateTime.now());
        record.setCheckedBy(user);

        return attendanceMapper.toAttendanceRecordResponse(attendanceRecordRepository.save(record));
    }

    @Override
    @Transactional
    public AttendanceSessionResponse updateSessionStatus(String username, Long sessionId, String status) {
        User user = getUserByUsername(username);
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException(ErrorCode.SESSION_NOT_FOUND));

        checkTeacherPermission(user, session.getClassSection());

        try {
            session.setStatus(SessionStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        return attendanceMapper.toAttendanceSessionResponse(attendanceSessionRepository.save(session));
    }
}
