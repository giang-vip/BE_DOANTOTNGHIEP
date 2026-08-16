package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.response.AnnouncementResponse;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.RegistrationPeriodResponse;
import com.hungnhan.school_management.dto.response.EnrollmentResponse;
import com.hungnhan.school_management.entity.*;
import com.hungnhan.school_management.constant.EnrollmentStatus;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.AnnouncementMapper;
import com.hungnhan.school_management.mapper.ClassSectionMapper;
import com.hungnhan.school_management.mapper.EnrollmentMapper;
import com.hungnhan.school_management.repository.*;
import com.hungnhan.school_management.service.StudentDashboardService;
import com.hungnhan.school_management.service.RegistrationPeriodService;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class StudentDashboardServiceImpl implements StudentDashboardService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AnnouncementRepository announcementRepository;
    private final ClassSectionRepository classSectionRepository;
    private final RegistrationPeriodService registrationPeriodService;
    private final ClassSectionMapper classSectionMapper;
    private final AnnouncementMapper announcementMapper;
    private final EnrollmentMapper enrollmentMapper;
    private final AttendanceRecordRepository attendanceRecordRepository;

    private Student getStudentByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
    }

    @Override
    public PageResponse<ClassSectionResponse> getStudentClasses(String username, int page, int size) {
        Student student = getStudentByUsername(username);

        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollmentPage = enrollmentRepository.searchEnrollments(student.getId(), null, pageable);

        List<ClassSectionResponse> content = enrollmentPage.getContent().stream()
                .map(enrollment -> {
                    ClassSectionResponse res = classSectionMapper.toClassSectionResponse(enrollment.getClassSection());
                    res.setEnrolledCount((int) enrollmentRepository.countActiveEnrollmentsByClassSectionId(res.getId()));
                    return res;
                })
                .collect(Collectors.toList());

        return PageResponse.<ClassSectionResponse>builder()
                .content(content)
                .pageNumber(enrollmentPage.getNumber())
                .pageSize(enrollmentPage.getSize())
                .totalElements(enrollmentPage.getTotalElements())
                .totalPages(enrollmentPage.getTotalPages())
                .last(enrollmentPage.isLast())
                .build();
    }

    @Override
    public PageResponse<AnnouncementResponse> getStudentAnnouncements(String username, int page, int size) {
        Student student = getStudentByUsername(username);

        // Lấy danh sách ID các lớp sinh viên đang học
        List<Long> classSectionIds = enrollmentRepository.searchEnrollments(student.getId(), null, Pageable.unpaged())
                .getContent().stream()
                .map(enrollment -> enrollment.getClassSection().getId())
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        Page<Announcement> announcementPage;
        
        if (classSectionIds.isEmpty()) {
            // Nếu không có lớp nào, chỉ trả về thông báo chung (nếu cần) hoặc trả về page rỗng. Ở đây trả về thông báo chung
            // Chúng ta có thể dùng một truy vấn custom khác nhưng tạm thời mock
            announcementPage = new PageImpl<>(List.of(), pageable, 0); // Đơn giản hóa: không học lớp nào thì không có thông báo
        } else {
            announcementPage = announcementRepository.findByClassSectionIdInOrClassSectionIsNullOrderByCreatedAtDesc(classSectionIds, pageable);
        }

        List<AnnouncementResponse> content = announcementPage.getContent().stream()
                .map(announcementMapper::toAnnouncementResponse)
                .collect(Collectors.toList());

        return PageResponse.<AnnouncementResponse>builder()
                .content(content)
                .pageNumber(announcementPage.getNumber())
                .pageSize(announcementPage.getSize())
                .totalElements(announcementPage.getTotalElements())
                .totalPages(announcementPage.getTotalPages())
                .last(announcementPage.isLast())
                .build();
    }

    @Override
    public RegistrationPeriodResponse getCurrentRegistrationPeriod() {
        return registrationPeriodService.getCurrentRegistrationPeriod();
    }

    @Override
    public PageResponse<ClassSectionResponse> getAvailableClasses(String username, String search, Long semesterId, int page, int size) {
        Student student = getStudentByUsername(username);
        Long majorId = student.getMajor() != null ? student.getMajor().getId() : null;

        Pageable pageable = PageRequest.of(page, size);
        Page<ClassSection> classSections = classSectionRepository.searchAvailableClassesForStudent(semesterId, majorId, search, pageable);

        List<ClassSectionResponse> content = classSections.getContent().stream()
                .map(classSection -> {
                    ClassSectionResponse res = classSectionMapper.toClassSectionResponse(classSection);
                    res.setEnrolledCount((int) enrollmentRepository.countActiveEnrollmentsByClassSectionId(res.getId()));
                    return res;
                })
                .collect(Collectors.toList());

        return PageResponse.<ClassSectionResponse>builder()
                .content(content)
                .pageNumber(classSections.getNumber())
                .pageSize(classSections.getSize())
                .totalElements(classSections.getTotalElements())
                .totalPages(classSections.getTotalPages())
                .last(classSections.isLast())
                .build();
    }

    @Override
    @Transactional
    public EnrollmentResponse enrollClass(String username, Long classSectionId) {
        RegistrationPeriodResponse period = registrationPeriodService.getCurrentRegistrationPeriod();
        if (period == null || !period.getIsOpen()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        
        LocalDate now = LocalDate.now();
        if (period.getStartDate() != null && period.getEndDate() != null) {
            if (now.isBefore(period.getStartDate()) || now.isAfter(period.getEndDate())) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }
        }

        Student student = getStudentByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        if (classSection.getMajor() != null && student.getMajor() != null) {
            if (!classSection.getMajor().getId().equals(student.getMajor().getId())) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        List<Enrollment> enrolled = enrollmentRepository.searchEnrollments(student.getId(), null, Pageable.unpaged()).getContent();
        boolean duplicateSubject = enrolled.stream().anyMatch(e -> {
            if (e.getClassSection().getSubject() == null || classSection.getSubject() == null) return false;
            if (!e.getClassSection().getSubject().getId().equals(classSection.getSubject().getId())) return false;
            
            if (e.getClassSection().getSemester() == null || classSection.getSemester() == null) return false;
            return e.getClassSection().getSemester().getId().equals(classSection.getSemester().getId());
        });
        if (duplicateSubject) {
            throw new AppException(ErrorCode.ENROLLMENT_EXISTED);
        }

        long currentCount = enrollmentRepository.countActiveEnrollmentsByClassSectionId(classSectionId);
        if (currentCount >= classSection.getCapacity()) {
            throw new AppException(ErrorCode.CLASS_SECTION_FULL);
        }

        boolean hasScheduleConflict = enrolled.stream().anyMatch(e -> {
            ClassSection c = e.getClassSection();
            if (c.getWeekday() == null || classSection.getWeekday() == null) return false;
            if (!c.getWeekday().equals(classSection.getWeekday())) return false;
            if (c.getStartDate() == null || classSection.getStartDate() == null) return false;
            if (c.getEndDate() == null || classSection.getEndDate() == null) return false;
            if (c.getStartTime() == null || classSection.getStartTime() == null) return false;
            if (c.getEndTime() == null || classSection.getEndTime() == null) return false;

            LocalDate maxStart = c.getStartDate().isAfter(classSection.getStartDate()) ? c.getStartDate() : classSection.getStartDate();
            LocalDate minEnd = c.getEndDate().isBefore(classSection.getEndDate()) ? c.getEndDate() : classSection.getEndDate();
            if (maxStart.isAfter(minEnd)) return false;

            LocalTime maxTimeStart = c.getStartTime().isAfter(classSection.getStartTime()) ? c.getStartTime() : classSection.getStartTime();
            LocalTime minTimeEnd = c.getEndTime().isBefore(classSection.getEndTime()) ? c.getEndTime() : classSection.getEndTime();
            return !maxTimeStart.isAfter(minTimeEnd);
        });
        if (hasScheduleConflict) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .classSection(classSection)
                .status(EnrollmentStatus.ACTIVE)
                .build();

        return enrollmentMapper.toEnrollmentResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional
    public void dropClass(String username, Long classSectionId) {
        RegistrationPeriodResponse period = registrationPeriodService.getCurrentRegistrationPeriod();
        if (period == null || !period.getIsOpen()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        LocalDate now = LocalDate.now();
        if (period.getStartDate() != null && period.getEndDate() != null) {
            if (now.isBefore(period.getStartDate()) || now.isAfter(period.getEndDate())) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }
        }

        Student student = getStudentByUsername(username);
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndClassSectionId(student.getId(), classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_FOUND));

        if (!enrollment.getStudent().getId().equals(student.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Delete associated attendance records first to avoid FK constraint violation
        List<AttendanceRecord> records = attendanceRecordRepository.findByEnrollmentId(enrollment.getId());
        if (!records.isEmpty()) {
            attendanceRecordRepository.deleteAll(records);
        }

        enrollmentRepository.delete(enrollment);
    }
}
