package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.constant.SubmissionStatus;
import com.hungnhan.school_management.dto.request.QuizAnswerRequest;
import com.hungnhan.school_management.dto.request.QuizSubmissionRequest;
import com.hungnhan.school_management.dto.response.QuizAnswerResponse;
import com.hungnhan.school_management.dto.response.QuizResultResponse;
import com.hungnhan.school_management.dto.response.SubmissionResponse;
import com.hungnhan.school_management.entity.*;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.AssignmentMapper;
import com.hungnhan.school_management.repository.*;
import com.hungnhan.school_management.service.StudentQuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentQuizServiceImpl implements StudentQuizService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizAnswerRepository quizAnswerRepository;
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
    public SubmissionResponse startQuiz(String username, Long assignmentId) {
        Student student = getStudentByUsername(username);
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        if (assignment.getType() != Assignment.AssignmentType.quiz) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION); // Not a quiz
        }

        Enrollment enrollment = getEnrollmentOrThrow(student, assignment.getClassSection().getId());

        Optional<Submission> existing = submissionRepository.findByAssignmentIdAndEnrollmentId(assignmentId, enrollment.getId());
        if (existing.isPresent()) {
            return assignmentMapper.toSubmissionResponse(existing.get());
        }

        Submission submission = Submission.builder()
                .assignment(assignment)
                .enrollment(enrollment)
                .status(SubmissionStatus.IN_PROGRESS)
                .build();

        return assignmentMapper.toSubmissionResponse(submissionRepository.save(submission));
    }

    @Override
    @Transactional
    public QuizResultResponse submitQuiz(String username, Long assignmentId, QuizSubmissionRequest request) {
        Student student = getStudentByUsername(username);
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        if (assignment.getType() != Assignment.AssignmentType.quiz) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        Enrollment enrollment = getEnrollmentOrThrow(student, assignment.getClassSection().getId());

        Submission submission = submissionRepository.findByAssignmentIdAndEnrollmentId(assignmentId, enrollment.getId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBMISSION_NOT_FOUND)); // Phai startQuiz truoc

        if (submission.getStatus() == SubmissionStatus.SUBMITTED || submission.getStatus() == SubmissionStatus.GRADED) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION); // Da nop roi
        }

        // Lay tat ca cau hoi cua bai quiz
        List<QuizQuestion> questions = quizQuestionRepository.findByAssignmentIdOrderByOrderIndexAsc(assignmentId);
        Map<Long, QuizQuestion> questionMap = questions.stream().collect(Collectors.toMap(QuizQuestion::getId, q -> q));

        // Tinh diem
        BigDecimal totalScore = BigDecimal.ZERO;
        List<QuizAnswer> quizAnswers = new ArrayList<>();

        for (QuizAnswerRequest ansReq : request.getAnswers()) {
            QuizQuestion question = questionMap.get(ansReq.getQuestionId());
            if (question == null) continue;

            boolean isCorrect = false;
            QuizQuestion.Choice selectedChoice = null;
            
            if (ansReq.getSelectedChoice() != null && !ansReq.getSelectedChoice().isEmpty()) {
                try {
                    selectedChoice = QuizQuestion.Choice.valueOf(ansReq.getSelectedChoice());
                    if (selectedChoice == question.getCorrectChoice()) {
                        isCorrect = true;
                        totalScore = totalScore.add(question.getPoints());
                    }
                } catch (IllegalArgumentException e) {
                    // Invalid choice
                }
            }

            QuizAnswer quizAnswer = QuizAnswer.builder()
                    .submission(submission)
                    .question(question)
                    .selectedChoice(selectedChoice)
                    .isCorrect(isCorrect)
                    .build();
            quizAnswers.add(quizAnswer);
        }

        // Xoa cau tra loi cu (neu co) do dang update
        List<QuizAnswer> oldAnswers = quizAnswerRepository.findBySubmissionId(submission.getId());
        quizAnswerRepository.deleteAll(oldAnswers);

        quizAnswerRepository.saveAll(quizAnswers);

        // Cap nhat thong tin submission
        submission.setScore(totalScore);
        submission.setStatus(SubmissionStatus.GRADED); // Tu dong cham diem
        submissionRepository.save(submission);

        return getQuizResult(username, assignmentId);
    }

    @Override
    public QuizResultResponse getQuizResult(String username, Long assignmentId) {
        Student student = getStudentByUsername(username);
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        Enrollment enrollment = getEnrollmentOrThrow(student, assignment.getClassSection().getId());

        Submission submission = submissionRepository.findByAssignmentIdAndEnrollmentId(assignmentId, enrollment.getId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBMISSION_NOT_FOUND));

        List<QuizAnswer> quizAnswers = quizAnswerRepository.findBySubmissionId(submission.getId());

        List<QuizAnswerResponse> answerResponses = quizAnswers.stream().map(qa -> {
            QuizQuestion q = qa.getQuestion();
            return QuizAnswerResponse.builder()
                    .questionId(q.getId())
                    .orderIndex(q.getOrderIndex())
                    .questionText(q.getQuestionText())
                    .selectedChoice(qa.getSelectedChoice() != null ? qa.getSelectedChoice().name() : null)
                    .correctChoice(q.getCorrectChoice().name())
                    .isCorrect(qa.getIsCorrect())
                    .pointsAwarded(qa.getIsCorrect() ? q.getPoints() : BigDecimal.ZERO)
                    .explanationText(q.getExplanationText())
                    .build();
        }).collect(Collectors.toList());

        return QuizResultResponse.builder()
                .submissionId(submission.getId())
                .assignmentId(assignmentId)
                .status(submission.getStatus().name())
                .totalScore(submission.getScore())
                .maxPoints(assignment.getMaxPoints())
                .submittedAt(submission.getSubmittedAt())
                .answers(answerResponses)
                .build();
    }
}
