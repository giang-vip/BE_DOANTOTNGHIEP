package com.hungnhan.school_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "quiz_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    /** Thu tu cau hoi trong de (1, 2, 3...) */
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "correct_choice", nullable = false)
    private Choice correctChoice;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal points;

    @Column(name = "explanation_text", columnDefinition = "TEXT")
    private String explanationText;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "choice_a_text", length = 500)
    private String choiceAText;

    @Column(name = "choice_b_text", length = 500)
    private String choiceBText;

    @Column(name = "choice_c_text", length = 500)
    private String choiceCText;

    @Column(name = "choice_d_text", length = 500)
    private String choiceDText;

    @Enumerated(EnumType.STRING)
    @Column(name = "ocr_status", nullable = false)
    @Builder.Default
    private OcrStatus ocrStatus = OcrStatus.NOT_PROCESSED;

    @Column(name = "ocr_extracted_text", columnDefinition = "TEXT")
    private String ocrExtractedText;

    public enum Choice {
        A, B, C, D
    }

    public enum OcrStatus {
        NOT_PROCESSED, IN_PROGRESS, COMPLETED, FAILED
    }
}
