package com.hungnhan.school_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ các trường null khi trả về JSON
public class ApiResponse<T> {
    @Builder.Default
    private int code = 1000; // 1000 đại diện cho Success, các số khác là mã lỗi tự định nghĩa
    private String message;
    private T result;
}
