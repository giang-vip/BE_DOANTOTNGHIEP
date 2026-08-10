package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.LearningMaterialRequest;
import com.hungnhan.school_management.dto.response.LearningMaterialResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.TeacherMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@Tag(name = "Teacher - Learning Material", description = "API quản lý học liệu dành cho Giảng viên (Phase 3)")
public class TeacherMaterialController {

    private final TeacherMaterialService teacherMaterialService;

    @GetMapping("/classes/{classSectionId}/materials")
    @Operation(summary = "Danh sách học liệu đã đăng (API_TC_07)")
    public ApiResponse<PageResponse<LearningMaterialResponse>> getMaterials(
            @PathVariable Long classSectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<LearningMaterialResponse>>builder()
                .result(teacherMaterialService.getMaterials(username, classSectionId, page, size))
                .build();
    }

    @PostMapping("/classes/{classSectionId}/materials")
    @Operation(summary = "Tải lên học liệu môn học (API_TC_08)")
    public ApiResponse<LearningMaterialResponse> uploadMaterial(
            @PathVariable Long classSectionId,
            @RequestBody @Valid LearningMaterialRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<LearningMaterialResponse>builder()
                .result(teacherMaterialService.uploadMaterial(username, classSectionId, request))
                .build();
    }

    @DeleteMapping("/materials/{id}")
    @Operation(summary = "Xóa học liệu (API_TC_09)")
    public ApiResponse<String> deleteMaterial(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        teacherMaterialService.deleteMaterial(username, id);
        return ApiResponse.<String>builder()
                .result("Học liệu đã được xóa thành công")
                .build();
    }
}
