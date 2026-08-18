package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MajorRequest {

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotBlank(message = "Major code is required")
    private String code;

    @NotBlank(message = "Major name is required")
    private String name;

    private String description;

    private Integer totalCredits;

    private String status; // ACTIVE, INACTIVE
}
