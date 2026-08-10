package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningMaterialResponse {
    private Long id;
    private Long classSectionId;
    private String title;
    private String fileName;
    private String fileUrl;
    private String mimeType;
    private Long uploadedById;
    private String uploadedByUsername;
    private LocalDateTime uploadedAt;
}
