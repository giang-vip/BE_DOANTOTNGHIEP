package com.hungnhan.school_management.entity;

import jakarta.persistence.*;
import lombok.*;
import com.hungnhan.school_management.constant.SectionStatus;

import java.time.LocalDate;
import java.time.LocalTime;

/** Anh xa bang "class_sections" (lop hoc phan - MaLHP). */
@Entity
@Table(name = "class_sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Major major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Teacher teacher;

    /** MaLHP, VD: LHP001 */
    @Column(name = "section_code", unique = true, nullable = false, length = 50)
    private String sectionCode;

    @Column(length = 100)
    private String room;

    /** 1 = Chu Nhat ... 7 = Thu Bay (theo CHECK trong SQL: 1..7) */
    @Column(nullable = false)
    private Integer weekday;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    @Builder.Default
    private Integer capacity = 50;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SectionStatus status = SectionStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Column(name = "attendance_weight")
    private Integer attendanceWeight; // Ví dụ: 10 (%)

    @Column(name = "midterm_weight")
    private Integer midtermWeight; // Ví dụ: 30 (%)

    @Column(name = "final_weight")
    private Integer finalWeight; // Ví dụ: 60 (%)

}
