package com.hungnhan.school_management.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private String fileName;
    private String storageKey;
    private String mimeType;
    private String uploadedBy;
    private LocalDateTime uploadedAt;
}
