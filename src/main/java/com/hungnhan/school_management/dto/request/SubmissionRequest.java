package com.hungnhan.school_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionRequest {

    @NotBlank(message = "Nội dung nộp bài không được để trống")
    private String content;

    private String fileUrl;
}
