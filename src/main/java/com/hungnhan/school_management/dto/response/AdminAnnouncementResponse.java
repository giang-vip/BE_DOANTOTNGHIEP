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
public class AdminAnnouncementResponse {
    private Long id;
    private String title;
    private String content;
    private String recipientGroup;
    private String sender;
    private LocalDateTime createdAt;
}
