package com.hungnhan.school_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MajorResponse {
    private Long id;
    private Long departmentId;
    private String departmentName;
    private String code;
    private String name;
    private String description;
    private String status;
    private LocalDateTime createdAt;
}
