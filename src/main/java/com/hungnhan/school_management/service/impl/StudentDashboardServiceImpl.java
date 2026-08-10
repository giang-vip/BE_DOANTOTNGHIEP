package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.response.AnnouncementResponse;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.Announcement;
import com.hungnhan.school_management.entity.ClassSection;
import com.hungnhan.school_management.entity.Enrollment;
import com.hungnhan.school_management.entity.Student;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.AnnouncementMapper;
import com.hungnhan.school_management.mapper.ClassSectionMapper;
import com.hungnhan.school_management.repository.AnnouncementRepository;
import com.hungnhan.school_management.repository.EnrollmentRepository;
import com.hungnhan.school_management.repository.StudentRepository;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.service.StudentDashboardService;
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
    private final ClassSectionMapper classSectionMapper;
    private final AnnouncementMapper announcementMapper;

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
                .map(enrollment -> classSectionMapper.toClassSectionResponse(enrollment.getClassSection()))
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
}
