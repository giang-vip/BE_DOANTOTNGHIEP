package com.hungnhan.school_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false, unique = true)
    private Enrollment enrollment;

    /** Diem tong ket hoc phan, tu tinh tu grade_component_scores */
    @Column(name = "final_score", precision = 5, scale = 2)
    private BigDecimal finalScore;

    /** Quy doi thang diem 4 tu final_score */
    @Column(name = "gpa_4_scale", precision = 3, scale = 2)
    private BigDecimal gpa4Scale;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    void touch() {
        updatedAt = LocalDateTime.now();
    }
}
