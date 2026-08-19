package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.SubjectRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.SubjectResponse;
import com.hungnhan.school_management.dto.request.DocumentRequest;
import com.hungnhan.school_management.dto.response.DocumentResponse;
import com.hungnhan.school_management.service.SubjectService;
import com.hungnhan.school_management.service.SubjectMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/subjects")
@RequiredArgsConstructor
@Tag(name = "Subject Management", description = "API quản lý Môn học (Dành cho ADMIN)")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectMaterialService subjectMaterialService;
    private final com.hungnhan.school_management.service.FileUploadService fileUploadService;

    @PostMapping
    @Operation(summary = "Tạo mới môn học (API_AD_22)")
    public ApiResponse<SubjectResponse> createSubject(@RequestBody @Valid SubjectRequest request) {
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.createSubject(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Danh sách môn học (API_AD_21)")
    public ApiResponse<PageResponse<SubjectResponse>> getSubjects(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long majorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<SubjectResponse>>builder()
                .result(subjectService.getSubjects(search, departmentId, majorId, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết môn học")
    public ApiResponse<SubjectResponse> getSubjectById(@PathVariable Long id) {
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.getSubjectById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật môn học (API_AD_22)")
    public ApiResponse<SubjectResponse> updateSubject(@PathVariable Long id, @RequestBody @Valid SubjectRequest request) {
        return ApiResponse.<SubjectResponse>builder()
                .result(subjectService.updateSubject(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa môn học")
    public ApiResponse<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ApiResponse.<Void>builder().build();
    }

    // --- Quản lý tài liệu gốc của Môn học ---
    @GetMapping("/{id}/materials")
    @Operation(summary = "Lấy danh sách tài liệu môn học")
    public ApiResponse<PageResponse<DocumentResponse>> getSubjectMaterials(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return ApiResponse.<PageResponse<DocumentResponse>>builder()
                .result(subjectMaterialService.getMaterials(id, page, size))
                .build();
    }

    @PostMapping("/{id}/materials")
    @Operation(summary = "Thêm mới tài liệu môn học")
    public ApiResponse<DocumentResponse> uploadSubjectMaterial(
            @PathVariable Long id,
            @RequestBody @Valid DocumentRequest request
    ) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ApiResponse.<DocumentResponse>builder()
                .result(subjectMaterialService.uploadMaterial(username, id, request))
                .build();
    }

    @DeleteMapping("/materials/{materialId}")
    @Operation(summary = "Xóa tài liệu môn học")
    public ApiResponse<String> deleteSubjectMaterial(@PathVariable Long materialId) {
        subjectMaterialService.deleteMaterial(materialId);
        return ApiResponse.<String>builder()
                .result("Đã xóa tài liệu môn học thành công")
                .build();
    }

    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tải lên tệp tin chung cho môn học lên Cloudinary")
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

    @GetMapping("/debug")
    public Object debugSubjects(@RequestParam(required = false) Long departmentId, @RequestParam(required = false) Long majorId) {
        return subjectService.getSubjects(null, departmentId, majorId, 0, 50);
    }
}
