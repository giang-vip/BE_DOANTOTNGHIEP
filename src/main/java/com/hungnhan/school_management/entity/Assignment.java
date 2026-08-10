package com.hungnhan.school_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_section_id", nullable = false)
    private ClassSection classSection;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "due_at", nullable = false)
    private LocalDateTime dueAt;

    @Column(name = "max_points", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal maxPoints = BigDecimal.TEN;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AssignmentType type = AssignmentType.essay;

    @Column(name = "exam_file_url", length = 500)
    private String examFileUrl;

    @Column(name = "exam_file_name")
    private String examFileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_file_type")
    private ExamFileType examFileType;

    /** So cau hoi neu dang quiz */
    @Column(name = "question_count")
    private Integer questionCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public enum AssignmentType {
        essay, quiz
    }

    public enum ExamFileType {
        pdf, image
    }
}
