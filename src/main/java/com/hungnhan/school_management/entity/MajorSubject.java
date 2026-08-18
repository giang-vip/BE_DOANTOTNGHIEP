package com.hungnhan.school_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Bang chuong trinh dao tao: mon nao thuoc/duoc phep hoc trong tung nganh.
 * Anh xa bang "major_subjects" (khoa chinh ghep tu major_id + subject_id).
 */
@Entity
@Table(name = "major_subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MajorSubject {

    @EmbeddedId
    private MajorSubjectId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("majorId")
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subjectId")
    @JoinColumn(name = "subject_id")
    private Subject subject;

    /** Hoc ky goi y trong chuong trinh dao tao */
    @Column(name = "recommended_semester")
    private Integer recommendedSemester;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_type", nullable = false)
    @Builder.Default
    private com.hungnhan.school_management.constant.SubjectType subjectType = com.hungnhan.school_management.constant.SubjectType.COMPULSORY;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
