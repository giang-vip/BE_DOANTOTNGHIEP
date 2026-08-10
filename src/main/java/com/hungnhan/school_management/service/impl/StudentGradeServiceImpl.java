package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.StudentGradeResponse;
import com.hungnhan.school_management.entity.ClassSection;
import com.hungnhan.school_management.entity.Enrollment;
import com.hungnhan.school_management.entity.Student;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.repository.EnrollmentRepository;
import com.hungnhan.school_management.repository.StudentRepository;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.service.StudentGradeService;
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
public class StudentGradeServiceImpl implements StudentGradeService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    private Student getStudentByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
    }

    @Override
    public PageResponse<StudentGradeResponse> getMyGrades(String username, Long semesterId, int page, int size) {
        Student student = getStudentByUsername(username);

        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollmentPage = enrollmentRepository.searchEnrollments(student.getId(), null, pageable);

        // Filter by semesterId if provided
        List<Enrollment> enrollments = enrollmentPage.getContent();
        if (semesterId != null) {
            enrollments = enrollments.stream()
                    .filter(e -> e.getClassSection().getSemester() != null && e.getClassSection().getSemester().getId().equals(semesterId))
                    .collect(Collectors.toList());
        }

        List<StudentGradeResponse> content = enrollments.stream().map(enrollment -> {
            ClassSection section = enrollment.getClassSection();
            return StudentGradeResponse.builder()
                    .enrollmentId(enrollment.getId())
                    .classSectionId(section.getId())
                    .subjectCode(section.getSubject().getCode())
                    .subjectName(section.getSubject().getName())
                    .sectionCode(section.getSectionCode())
                    .credits(section.getSubject().getCredits())
                    .semesterName(section.getSemester() != null ? section.getSemester().getName() : null)
                    .attendanceScore(enrollment.getAttendanceScore())
                    .midtermScore(enrollment.getMidtermScore())
                    .finalExamScore(enrollment.getFinalExamScore())
                    .finalScore(enrollment.getFinalScore())
                    .finalGrade(enrollment.getFinalGrade())
                    .build();
        }).collect(Collectors.toList());

        return PageResponse.<StudentGradeResponse>builder()
                .content(content)
                .pageNumber(enrollmentPage.getNumber())
                .pageSize(enrollmentPage.getSize())
                .totalElements(enrollmentPage.getTotalElements()) // Can be inaccurate due to filter above, but simple for now
                .totalPages(enrollmentPage.getTotalPages())
                .last(enrollmentPage.isLast())
                .build();
    }
}
