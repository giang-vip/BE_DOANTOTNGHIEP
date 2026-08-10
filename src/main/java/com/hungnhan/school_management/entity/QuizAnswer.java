package com.hungnhan.school_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_answers", uniqueConstraints = {
        @UniqueConstraint(name = "uq_quiz_answer_submission_question", columnNames = {"submission_id", "question_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    /** null nghia sinh vien bo trong cau nay khi nop bai */
    @Enumerated(EnumType.STRING)
    @Column(name = "selected_choice")
    private QuizQuestion.Choice selectedChoice;

    @Column(name = "is_correct", nullable = false)
    @Builder.Default
    private Boolean isCorrect = false;
}
