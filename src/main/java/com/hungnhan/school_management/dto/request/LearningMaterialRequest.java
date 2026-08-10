package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningMaterialRequest {

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Tên file không được để trống")
    private String fileName;

    @NotBlank(message = "URL file không được để trống")
    private String fileUrl;

    private String mimeType;
}
