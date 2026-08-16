package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.GradeConfigRequest;
import com.hungnhan.school_management.dto.response.FinalGradeResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.ClassSection;
import com.hungnhan.school_management.entity.Enrollment;
import com.hungnhan.school_management.entity.Teacher;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.repository.ClassSectionRepository;
import com.hungnhan.school_management.repository.EnrollmentRepository;
import com.hungnhan.school_management.repository.TeacherRepository;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.service.TeacherGradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherGradeServiceImpl implements TeacherGradeService {

    private final ClassSectionRepository classSectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

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
    @Transactional
    public void configureGradeWeights(String username, Long classSectionId, GradeConfigRequest request) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        int totalWeight = request.getAttendanceWeight() + request.getMidtermWeight() + request.getFinalWeight();
        if (totalWeight != 100) {
            throw new AppException(ErrorCode.INVALID_WEIGHT_SUM);
        }

        classSection.setAttendanceWeight(request.getAttendanceWeight());
        classSection.setMidtermWeight(request.getMidtermWeight());
        classSection.setFinalWeight(request.getFinalWeight());

        classSectionRepository.save(classSection);
    }

    @Override
    @Transactional
    public PageResponse<FinalGradeResponse> getFinalGrades(String username, Long classSectionId, int page, int size) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollmentPage = enrollmentRepository.searchEnrollments(null, classSectionId, pageable);

        // Optional: Tự động tính toán điểm khi truy vấn nếu chưa có, hoặc có logic tự động tính điểm
        List<FinalGradeResponse> content = enrollmentPage.getContent().stream()
                .map(this::calculateFinalGradeAndMap)
                .collect(Collectors.toList());

        return PageResponse.<FinalGradeResponse>builder()
                .content(content)
                .pageNumber(enrollmentPage.getNumber())
                .pageSize(enrollmentPage.getSize())
                .totalElements(enrollmentPage.getTotalElements())
                .totalPages(enrollmentPage.getTotalPages())
                .last(enrollmentPage.isLast())
                .build();
    }

    private FinalGradeResponse calculateFinalGradeAndMap(Enrollment enrollment) {
        ClassSection classSection = enrollment.getClassSection();
        
        // Neu lop chua cau hinh trong so thi chi tra ve cac diem hien co
        if (classSection.getAttendanceWeight() != null && classSection.getMidtermWeight() != null && classSection.getFinalWeight() != null) {
            BigDecimal attendance = enrollment.getAttendanceScore() != null ? enrollment.getAttendanceScore() : BigDecimal.ZERO;
            BigDecimal midterm = enrollment.getMidtermScore() != null ? enrollment.getMidtermScore() : BigDecimal.ZERO;
            BigDecimal finalExam = enrollment.getFinalExamScore() != null ? enrollment.getFinalExamScore() : BigDecimal.ZERO;

            BigDecimal attWeight = new BigDecimal(classSection.getAttendanceWeight()).divide(new BigDecimal(100));
            BigDecimal midWeight = new BigDecimal(classSection.getMidtermWeight()).divide(new BigDecimal(100));
            BigDecimal finWeight = new BigDecimal(classSection.getFinalWeight()).divide(new BigDecimal(100));

            BigDecimal finalScore = attendance.multiply(attWeight)
                    .add(midterm.multiply(midWeight))
                    .add(finalExam.multiply(finWeight))
                    .setScale(2, RoundingMode.HALF_UP);

            enrollment.setFinalScore(finalScore);
            enrollment.setFinalGrade(convertToLetterGrade(finalScore));
            
            enrollmentRepository.save(enrollment);
        }

        return FinalGradeResponse.builder()
                .enrollmentId(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentCode(enrollment.getStudent().getStudentCode())
                .studentName(enrollment.getStudent().getFullName())
                .attendanceScore(enrollment.getAttendanceScore())
                .midtermScore(enrollment.getMidtermScore())
                .finalExamScore(enrollment.getFinalExamScore())
                .finalScore(enrollment.getFinalScore())
                .finalGrade(enrollment.getFinalGrade())
                .build();
    }

    private String convertToLetterGrade(BigDecimal score) {
        if (score == null) return "";
        double val = score.doubleValue();
        if (val >= 8.5) return "A";
        if (val >= 7.0) return "B";
        if (val >= 5.5) return "C";
        if (val >= 4.0) return "D";
        return "F";
    }

    @Override
    @Transactional
    public void updateStudentGrades(String username, Long classSectionId, List<com.hungnhan.school_management.dto.request.TeacherGradeUpdateRequest> requests) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        for (com.hungnhan.school_management.dto.request.TeacherGradeUpdateRequest req : requests) {
            Enrollment enrollment = enrollmentRepository.findById(req.getEnrollmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_FOUND));

            if (!enrollment.getClassSection().getId().equals(classSectionId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            if (req.getAttendanceScore() != null) {
                enrollment.setAttendanceScore(req.getAttendanceScore());
            }
            if (req.getMidtermScore() != null) {
                enrollment.setMidtermScore(req.getMidtermScore());
            }
            if (req.getFinalExamScore() != null) {
                enrollment.setFinalExamScore(req.getFinalExamScore());
            }

            // Recalculate and save final score immediately
            calculateFinalGradeAndMap(enrollment);
        }
    }
}
