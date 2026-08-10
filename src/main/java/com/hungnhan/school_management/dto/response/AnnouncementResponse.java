package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementResponse {
    private Long id;
    private Long classSectionId;
    private String title;
    private String content;
    private Long createdById;
    private String createdByUsername;
    private LocalDateTime createdAt;
}
