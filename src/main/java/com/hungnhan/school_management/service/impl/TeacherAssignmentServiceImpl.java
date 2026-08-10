package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.AssignmentRequest;
import com.hungnhan.school_management.dto.request.QuizQuestionRequest;
import com.hungnhan.school_management.dto.request.SubmissionGradeRequest;
import com.hungnhan.school_management.dto.response.AssignmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.QuizQuestionResponse;
import com.hungnhan.school_management.dto.response.SubmissionResponse;
import com.hungnhan.school_management.entity.*;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.constant.SubmissionStatus;
import com.hungnhan.school_management.mapper.AssignmentMapper;
import com.hungnhan.school_management.repository.*;
import com.hungnhan.school_management.service.TeacherAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final ClassSectionRepository classSectionRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final AssignmentMapper assignmentMapper;

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
    public PageResponse<AssignmentResponse> getAssignments(String username, Long classSectionId, int page, int size) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        Pageable pageable = PageRequest.of(page, size);
        Page<Assignment> assignmentPage = assignmentRepository.findByClassSectionIdOrderByDueAtAsc(classSectionId, pageable);

        List<AssignmentResponse> content = assignmentPage.getContent().stream()
                .map(assignmentMapper::toAssignmentResponse)
                .collect(Collectors.toList());

        return PageResponse.<AssignmentResponse>builder()
                .content(content)
                .pageNumber(assignmentPage.getNumber())
                .pageSize(assignmentPage.getSize())
                .totalElements(assignmentPage.getTotalElements())
                .totalPages(assignmentPage.getTotalPages())
                .last(assignmentPage.isLast())
                .build();
    }

    @Override
    public AssignmentResponse createAssignment(String username, Long classSectionId, AssignmentRequest request) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        Assignment assignment = assignmentMapper.toAssignment(request);
        assignment.setClassSection(classSection);
        assignment.setCreatedBy(user);

        if (request.getType() != null) {
            assignment.setType(Assignment.AssignmentType.valueOf(request.getType()));
        }
        if (request.getExamFileType() != null) {
            assignment.setExamFileType(Assignment.ExamFileType.valueOf(request.getExamFileType()));
        }

        return assignmentMapper.toAssignmentResponse(assignmentRepository.save(assignment));
    }

    @Override
    public AssignmentResponse updateAssignment(String username, Long id, AssignmentRequest request) {
        User user = getUserByUsername(username);
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        checkTeacherPermission(user, assignment.getClassSection());

        assignmentMapper.updateAssignment(assignment, request);
        
        if (request.getType() != null) {
            assignment.setType(Assignment.AssignmentType.valueOf(request.getType()));
        }
        if (request.getExamFileType() != null) {
            assignment.setExamFileType(Assignment.ExamFileType.valueOf(request.getExamFileType()));
        }

        return assignmentMapper.toAssignmentResponse(assignmentRepository.save(assignment));
    }

    @Override
    public void deleteAssignment(String username, Long id) {
        User user = getUserByUsername(username);
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        checkTeacherPermission(user, assignment.getClassSection());

        assignmentRepository.delete(assignment);
    }

    @Override
    @Transactional
    public List<QuizQuestionResponse> configureQuiz(String username, Long assignmentId, List<QuizQuestionRequest> requests) {
        User user = getUserByUsername(username);
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        checkTeacherPermission(user, assignment.getClassSection());

        // Delete old questions if needed, or we can just append/update. Here we simple delete old and save new for simplicity
        List<QuizQuestion> oldQuestions = quizQuestionRepository.findByAssignmentIdOrderByOrderIndexAsc(assignmentId);
        quizQuestionRepository.deleteAll(oldQuestions);

        List<QuizQuestion> newQuestions = requests.stream().map(req -> {
            QuizQuestion q = assignmentMapper.toQuizQuestion(req);
            q.setAssignment(assignment);
            q.setCorrectChoice(QuizQuestion.Choice.valueOf(req.getCorrectChoice()));
            return q;
        }).collect(Collectors.toList());

        List<QuizQuestion> savedQuestions = quizQuestionRepository.saveAll(newQuestions);

        assignment.setQuestionCount(savedQuestions.size());
        assignmentRepository.save(assignment);

        return savedQuestions.stream()
                .map(assignmentMapper::toQuizQuestionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SubmissionResponse> getSubmissions(String username, Long assignmentId, int page, int size) {
        User user = getUserByUsername(username);
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        checkTeacherPermission(user, assignment.getClassSection());

        Pageable pageable = PageRequest.of(page, size);
        Page<Submission> submissionPage = submissionRepository.findByAssignmentId(assignmentId, pageable);

        List<SubmissionResponse> content = submissionPage.getContent().stream()
                .map(assignmentMapper::toSubmissionResponse)
                .collect(Collectors.toList());

        return PageResponse.<SubmissionResponse>builder()
                .content(content)
                .pageNumber(submissionPage.getNumber())
                .pageSize(submissionPage.getSize())
                .totalElements(submissionPage.getTotalElements())
                .totalPages(submissionPage.getTotalPages())
                .last(submissionPage.isLast())
                .build();
    }

    @Override
    public SubmissionResponse gradeSubmission(String username, Long submissionId, SubmissionGradeRequest request) {
        User user = getUserByUsername(username);
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBMISSION_NOT_FOUND));

        checkTeacherPermission(user, submission.getAssignment().getClassSection());

        submission.setScore(request.getScore());
        submission.setFeedback(request.getFeedback());
        submission.setStatus(SubmissionStatus.GRADED);

        return assignmentMapper.toSubmissionResponse(submissionRepository.save(submission));
    }
}
