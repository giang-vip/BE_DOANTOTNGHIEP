package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.LearningMaterialRequest;
import com.hungnhan.school_management.dto.response.LearningMaterialResponse;
import com.hungnhan.school_management.dto.response.DocumentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.TeacherMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.hungnhan.school_management.service.FileUploadService;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@Tag(name = "Teacher - Learning Material", description = "API quản lý học liệu dành cho Giảng viên (Phase 3)")
public class TeacherMaterialController {

    private final TeacherMaterialService teacherMaterialService;
    private final FileUploadService fileUploadService;

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

    @GetMapping("/classes/{classSectionId}/subject-materials")
    @Operation(summary = "Danh sách học liệu gốc của môn học (API_TC_07_SUBJ)")
    public ApiResponse<PageResponse<DocumentResponse>> getSubjectMaterials(
            @PathVariable Long classSectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<PageResponse<DocumentResponse>>builder()
                .result(teacherMaterialService.getSubjectMaterials(username, classSectionId, page, size))
                .build();
    }

    @PostMapping(value = "/classes/{classSectionId}/materials", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tải lên học liệu môn học (API_TC_08)")
    public ApiResponse<LearningMaterialResponse> uploadMaterial(
            @PathVariable Long classSectionId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("title") String title
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            String fileUrl = fileUploadService.uploadFile(file);
            LearningMaterialRequest request = LearningMaterialRequest.builder()
                    .title(title)
                    .fileName(file.getOriginalFilename())
                    .fileUrl(fileUrl)
                    .mimeType(file.getContentType())
                    .build();
            return ApiResponse.<LearningMaterialResponse>builder()
                    .result(teacherMaterialService.uploadMaterial(username, classSectionId, request))
                    .build();
        } catch (java.io.IOException e) {
            throw new com.hungnhan.school_management.exception.AppException(com.hungnhan.school_management.exception.ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
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

    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tải lên tệp tin chung (PDF/Ảnh) lên Cloudinary")
    public ApiResponse<String> uploadFile(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file
    ) {
        try {
            String fileUrl = fileUploadService.uploadFile(file);
            return ApiResponse.<String>builder()
                    .result(fileUrl)
                    .build();
        } catch (java.io.IOException e) {
            throw new com.hungnhan.school_management.exception.AppException(com.hungnhan.school_management.exception.ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
