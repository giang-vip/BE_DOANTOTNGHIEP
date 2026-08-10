package com.hungnhan.school_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationPeriodResponse {
    private Long id;
    private Long semesterId;
    private String semesterCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isOpen;
}
