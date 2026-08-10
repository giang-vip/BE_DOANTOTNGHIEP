package com.hungnhan.school_management.dto.response;

import com.hungnhan.school_management.constant.SectionStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassSectionResponse {
    private Long id;
    private Long departmentId;
    private String departmentName;
    private Long majorId;
    private String majorName;
    private Long subjectId;
    private String subjectName;
    private Long teacherId;
    private String teacherName;
    private String sectionCode;
    private String room;
    private Integer weekday;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer capacity;
    private SectionStatus status;
    private Long semesterId;
    private String semesterCode;
}
