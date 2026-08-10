package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.constant.SubmissionStatus;
import com.hungnhan.school_management.dto.request.QuizAnswerRequest;
import com.hungnhan.school_management.dto.request.QuizSubmissionRequest;
import com.hungnhan.school_management.dto.response.QuizResultResponse;
import com.hungnhan.school_management.entity.*;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentQuizServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private QuizQuestionRepository quizQuestionRepository;

    @Mock
    private QuizAnswerRepository quizAnswerRepository;

    @InjectMocks
    private StudentQuizServiceImpl studentQuizService;

    private User mockUser;
    private Student mockStudent;
    private Enrollment mockEnrollment;
    private Assignment mockAssignment;
    private ClassSection mockClassSection;
    private Submission mockSubmission;
    private QuizQuestion mockQuestion1;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("student1");

        mockStudent = new Student();
        mockStudent.setId(1L);

        mockClassSection = new ClassSection();
        mockClassSection.setId(10L);

        mockEnrollment = new Enrollment();
        mockEnrollment.setId(100L);
        mockEnrollment.setStudent(mockStudent);
        mockEnrollment.setClassSection(mockClassSection);

        mockAssignment = new Assignment();
        mockAssignment.setId(200L);
        mockAssignment.setType(Assignment.AssignmentType.quiz);
        mockAssignment.setClassSection(mockClassSection);
        mockAssignment.setMaxPoints(new BigDecimal("10.0"));

        mockSubmission = new Submission();
        mockSubmission.setId(300L);
        mockSubmission.setAssignment(mockAssignment);
        mockSubmission.setEnrollment(mockEnrollment);
        mockSubmission.setStatus(SubmissionStatus.IN_PROGRESS);

        mockQuestion1 = new QuizQuestion();
        mockQuestion1.setId(400L);
        mockQuestion1.setCorrectChoice(QuizQuestion.Choice.A);
        mockQuestion1.setPoints(new BigDecimal("10.0"));
        mockQuestion1.setOrderIndex(1);
    }

    @Test
    void submitQuiz_Success_CalculatesCorrectScore() {
        // Arrange
        QuizAnswerRequest ans1 = new QuizAnswerRequest(400L, "A"); // Correct answer
        QuizSubmissionRequest request = new QuizSubmissionRequest(List.of(ans1));

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(mockUser));
        when(studentRepository.findByUserId(1L)).thenReturn(Optional.of(mockStudent));
        when(assignmentRepository.findById(200L)).thenReturn(Optional.of(mockAssignment));
        when(enrollmentRepository.searchEnrollments(eq(1L), eq(10L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(mockEnrollment)));
        when(submissionRepository.findByAssignmentIdAndEnrollmentId(200L, 100L))
                .thenReturn(Optional.of(mockSubmission));
        when(quizQuestionRepository.findByAssignmentIdOrderByOrderIndexAsc(200L))
                .thenReturn(Collections.singletonList(mockQuestion1));
        when(quizAnswerRepository.findBySubmissionId(300L)).thenReturn(Collections.emptyList());

        // For getQuizResult at the end
        when(quizAnswerRepository.findBySubmissionId(300L)).thenReturn(List.of(
                QuizAnswer.builder()
                        .submission(mockSubmission)
                        .question(mockQuestion1)
                        .selectedChoice(QuizQuestion.Choice.A)
                        .isCorrect(true)
                        .build()
        ));

        // Act
        QuizResultResponse response = studentQuizService.submitQuiz("student1", 200L, request);

        // Assert
        assertNotNull(response);
        assertEquals(new BigDecimal("10.0"), mockSubmission.getScore());
        assertEquals(SubmissionStatus.GRADED, mockSubmission.getStatus());
        verify(submissionRepository, times(1)).save(mockSubmission);
        verify(quizAnswerRepository, times(1)).saveAll(anyList());
    }

    @Test
    void submitQuiz_AlreadySubmitted_ThrowsException() {
        // Arrange
        mockSubmission.setStatus(SubmissionStatus.SUBMITTED);
        QuizSubmissionRequest request = new QuizSubmissionRequest(List.of());

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(mockUser));
        when(studentRepository.findByUserId(1L)).thenReturn(Optional.of(mockStudent));
        when(assignmentRepository.findById(200L)).thenReturn(Optional.of(mockAssignment));
        when(enrollmentRepository.searchEnrollments(eq(1L), eq(10L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(mockEnrollment)));
        when(submissionRepository.findByAssignmentIdAndEnrollmentId(200L, 100L))
                .thenReturn(Optional.of(mockSubmission));

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> 
                studentQuizService.submitQuiz("student1", 200L, request));
        assertEquals(ErrorCode.UNCATEGORIZED_EXCEPTION, exception.getErrorCode());
        
        verify(quizAnswerRepository, never()).saveAll(anyList());
    }
}
