package com.hungnhan.school_management.entity;

import jakarta.persistence.*;
import lombok.*;
import com.hungnhan.school_management.constant.EnrollmentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(name = "uq_enrollment_student_class", columnNames = {"student_id", "class_section_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_section_id", nullable = false)
    private ClassSection classSection;

    @Column(name = "enrolled_at", updatable = false)
    private LocalDateTime enrolledAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "attendance_score", precision = 5, scale = 2)
    private java.math.BigDecimal attendanceScore;

    @Column(name = "midterm_score", precision = 5, scale = 2)
    private java.math.BigDecimal midtermScore;

    @Column(name = "final_exam_score", precision = 5, scale = 2)
    private java.math.BigDecimal finalExamScore;

    @Column(name = "final_score", precision = 5, scale = 2)
    private java.math.BigDecimal finalScore;

    @Column(name = "final_grade", length = 5)
    private String finalGrade;

    @PrePersist
    void prePersist() {
        enrolledAt = LocalDateTime.now();
    }
}
