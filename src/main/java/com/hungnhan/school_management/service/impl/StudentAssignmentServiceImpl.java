package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.constant.SubmissionStatus;
import com.hungnhan.school_management.dto.request.SubmissionRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.StudentAssignmentResponse;
import com.hungnhan.school_management.dto.response.SubmissionResponse;
import com.hungnhan.school_management.entity.*;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.AssignmentMapper;
import com.hungnhan.school_management.repository.*;
import com.hungnhan.school_management.service.StudentAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentAssignmentServiceImpl implements StudentAssignmentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final AssignmentMapper assignmentMapper;

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
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
    }

    @Override
    public PageResponse<StudentAssignmentResponse> getAssignments(String username, Long classSectionId, int page, int size) {
        Student student = getStudentByUsername(username);
        Enrollment enrollment = getEnrollmentOrThrow(student, classSectionId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Assignment> assignmentPage = assignmentRepository.findByClassSectionIdOrderByDueAtAsc(classSectionId, pageable);

        List<StudentAssignmentResponse> content = assignmentPage.getContent().stream().map(assignment -> {
            Optional<Submission> submissionOpt = submissionRepository.findByAssignmentIdAndEnrollmentId(assignment.getId(), enrollment.getId());
            
            StudentAssignmentResponse res = StudentAssignmentResponse.builder()
                    .id(assignment.getId())
                    .classSectionId(assignment.getClassSection().getId())
                    .title(assignment.getTitle())
                    .description(assignment.getDescription())
                    .dueAt(assignment.getDueAt())
                    .maxPoints(assignment.getMaxPoints())
                    .type(assignment.getType() != null ? assignment.getType().name() : null)
                    .examFileUrl(assignment.getExamFileUrl())
                    .examFileName(assignment.getExamFileName())
                    .examFileType(assignment.getExamFileType() != null ? assignment.getExamFileType().name() : null)
                    .questionCount(assignment.getQuestionCount())
                    .build();

            if (submissionOpt.isPresent()) {
                Submission sub = submissionOpt.get();
                res.setSubmissionId(sub.getId());
                res.setSubmissionStatus(sub.getStatus() != null ? sub.getStatus().name() : null);
                res.setSubmissionScore(sub.getScore());
                res.setSubmittedAt(sub.getSubmittedAt());
            }

            return res;
        }).collect(Collectors.toList());

        return PageResponse.<StudentAssignmentResponse>builder()
                .content(content)
                .pageNumber(assignmentPage.getNumber())
                .pageSize(assignmentPage.getSize())
                .totalElements(assignmentPage.getTotalElements())
                .totalPages(assignmentPage.getTotalPages())
                .last(assignmentPage.isLast())
                .build();
    }

    @Override
    public SubmissionResponse submitAssignment(String username, Long assignmentId, SubmissionRequest request) {
        Student student = getStudentByUsername(username);
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        
        Enrollment enrollment = getEnrollmentOrThrow(student, assignment.getClassSection().getId());

        Submission submission = submissionRepository.findByAssignmentIdAndEnrollmentId(assignmentId, enrollment.getId())
                .orElse(new Submission());

        submission.setAssignment(assignment);
        submission.setEnrollment(enrollment);
        submission.setContent(request.getContent());
        submission.setFileUrl(request.getFileUrl());
        submission.setStatus(SubmissionStatus.SUBMITTED); // Set trang thai no bai

        return assignmentMapper.toSubmissionResponse(submissionRepository.save(submission));
    }

    @Override
    public SubmissionResponse getMySubmission(String username, Long assignmentId) {
        Student student = getStudentByUsername(username);
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        Enrollment enrollment = getEnrollmentOrThrow(student, assignment.getClassSection().getId());

        Submission submission = submissionRepository.findByAssignmentIdAndEnrollmentId(assignmentId, enrollment.getId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBMISSION_NOT_FOUND));

        return assignmentMapper.toSubmissionResponse(submission);
    }
}
