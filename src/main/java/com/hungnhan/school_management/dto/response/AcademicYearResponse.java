package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicYearResponse {
    private Long id;
    private String code;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;
}
