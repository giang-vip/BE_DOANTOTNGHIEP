package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentGradeResponse {
    private Long enrollmentId;
    private Long classSectionId;
    private String subjectCode;
    private String subjectName;
    private String sectionCode;
    private Integer credits;
    private String semesterName;
    private BigDecimal attendanceScore;
    private BigDecimal midtermScore;
    private BigDecimal finalExamScore;
    private BigDecimal finalScore;
    private String finalGrade;
}
