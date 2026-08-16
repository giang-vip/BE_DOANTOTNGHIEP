package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.EnrollmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.ClassSection;
import com.hungnhan.school_management.entity.Enrollment;
import com.hungnhan.school_management.entity.Teacher;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.ClassSectionMapper;
import com.hungnhan.school_management.mapper.EnrollmentMapper;
import com.hungnhan.school_management.repository.ClassSectionRepository;
import com.hungnhan.school_management.repository.EnrollmentRepository;
import com.hungnhan.school_management.repository.SemesterRepository;
import com.hungnhan.school_management.repository.TeacherRepository;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.service.TeacherClassService;
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
public class TeacherClassServiceImpl implements TeacherClassService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final ClassSectionRepository classSectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SemesterRepository semesterRepository;
    private final ClassSectionMapper classSectionMapper;
    private final EnrollmentMapper enrollmentMapper;

    private Teacher getTeacherByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
    }

    @Override
    public PageResponse<ClassSectionResponse> getTeacherClassSections(String username, String search, Long semesterId, int page, int size) {
        Teacher teacher = getTeacherByUsername(username);
        
        Long actualSemesterId = semesterId;
        if (actualSemesterId == null) {
            actualSemesterId = semesterRepository.findByIsCurrentTrue()
                .map(com.hungnhan.school_management.entity.Semester::getId)
                .orElse(null);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ClassSection> classSectionPage = classSectionRepository.searchTeacherClassSections(teacher.getId(), search, actualSemesterId, pageable);

        List<ClassSectionResponse> content = classSectionPage.getContent().stream()
                .map(classSectionMapper::toClassSectionResponse)
                .collect(Collectors.toList());

        return PageResponse.<ClassSectionResponse>builder()
                .content(content)
                .pageNumber(classSectionPage.getNumber())
                .pageSize(classSectionPage.getSize())
                .totalElements(classSectionPage.getTotalElements())
                .totalPages(classSectionPage.getTotalPages())
                .last(classSectionPage.isLast())
                .build();
    }

    @Override
    public PageResponse<EnrollmentResponse> getStudentsInClassSection(String username, Long classSectionId, int page, int size) {
        Teacher teacher = getTeacherByUsername(username);

        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        // Kiểm tra giảng viên có quyền xem lớp này không
        if (!classSection.getTeacher().getId().equals(teacher.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED); // Hoặc tạo mã lỗi riêng "Bạn không giảng dạy lớp này"
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollmentPage = enrollmentRepository.searchEnrollments(null, classSectionId, pageable);

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
}
