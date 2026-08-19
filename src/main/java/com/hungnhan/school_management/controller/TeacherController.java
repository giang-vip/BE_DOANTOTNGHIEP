package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.TeacherRequest;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.TeacherResponse;
import com.hungnhan.school_management.service.TeacherService;
import com.hungnhan.school_management.service.TeacherClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/teachers")
@RequiredArgsConstructor
@Tag(name = "Teacher Management", description = "API quản lý Giảng viên (Dành cho ADMIN)")
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherClassService teacherClassService;

    @PostMapping
    @Operation(summary = "Tạo mới giảng viên (API_AD_18)")
    public ApiResponse<TeacherResponse> createTeacher(@RequestBody @Valid TeacherRequest request) {
        return ApiResponse.<TeacherResponse>builder()
                .result(teacherService.createTeacher(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Danh sách giảng viên (API_AD_17)")
    public ApiResponse<PageResponse<TeacherResponse>> getTeachers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<TeacherResponse>>builder()
                .result(teacherService.getTeachers(search, departmentId, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết giảng viên")
    public ApiResponse<TeacherResponse> getTeacherById(@PathVariable Long id) {
        return ApiResponse.<TeacherResponse>builder()
                .result(teacherService.getTeacherById(id))
                .build();
    }

    @GetMapping("/{id}/classes")
    @Operation(summary = "Lấy danh sách lớp học phần của giảng viên (Admin)")
    public ApiResponse<PageResponse<ClassSectionResponse>> getTeacherClasses(
            @PathVariable Long id,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<ClassSectionResponse>>builder()
                .result(teacherClassService.getTeacherClassSectionsByTeacherId(id, search, semesterId, page, size))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật giảng viên (API_AD_18)")
    public ApiResponse<TeacherResponse> updateTeacher(@PathVariable Long id, @RequestBody @Valid TeacherRequest request) {
        return ApiResponse.<TeacherResponse>builder()
                .result(teacherService.updateTeacher(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa giảng viên")
    public ApiResponse<String> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ApiResponse.<String>builder()
                .result("Giảng viên đã được xóa thành công")
                .build();
    }
}
