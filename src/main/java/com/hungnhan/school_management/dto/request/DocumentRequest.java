package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {

    @NotBlank(message = "Tên file không được để trống")
    private String fileName;

    @NotBlank(message = "URL/Storage Key không được để trống")
    private String storageKey;

    private String mimeType;
}
