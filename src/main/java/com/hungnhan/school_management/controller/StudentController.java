package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.ApiResponse;
import com.hungnhan.school_management.dto.request.StudentRequest;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.StudentGradeResponse;
import com.hungnhan.school_management.dto.response.StudentResponse;
import com.hungnhan.school_management.service.StudentService;
import com.hungnhan.school_management.service.StudentGradeService;
import com.hungnhan.school_management.service.StudentDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
@Tag(name = "Student Management", description = "API quản lý Sinh viên (Dành cho ADMIN)")
public class StudentController {

    private final StudentService studentService;
    private final StudentGradeService studentGradeService;
    private final StudentDashboardService studentDashboardService;

    @PostMapping
    @Operation(summary = "Tạo mới sinh viên (API_AD_16)")
    public ApiResponse<StudentResponse> createStudent(@RequestBody @Valid StudentRequest request) {
        return ApiResponse.<StudentResponse>builder()
                .result(studentService.createStudent(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Danh sách sinh viên (API_AD_15)")
    public ApiResponse<PageResponse<StudentResponse>> getStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long majorId,
            @RequestParam(required = false) Long classId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<StudentResponse>>builder()
                .result(studentService.getStudents(search, departmentId, majorId, classId, page, size))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết sinh viên")
    public ApiResponse<StudentResponse> getStudentById(@PathVariable Long id) {
        return ApiResponse.<StudentResponse>builder()
                .result(studentService.getStudentById(id))
                .build();
    }

    @GetMapping("/{id}/grades")
    @Operation(summary = "Lấy điểm của sinh viên (Admin)")
    public ApiResponse<PageResponse<StudentGradeResponse>> getStudentGrades(
            @PathVariable Long id,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<StudentGradeResponse>>builder()
                .result(studentGradeService.getStudentGradesByStudentId(id, semesterId, page, size))
                .build();
    }

    @GetMapping("/{id}/classes")
    @Operation(summary = "Lấy danh sách lớp học phần của sinh viên (Admin)")
    public ApiResponse<PageResponse<ClassSectionResponse>> getStudentClasses(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ApiResponse.<PageResponse<ClassSectionResponse>>builder()
                .result(studentDashboardService.getStudentClassesByStudentId(id, page, size))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật sinh viên (API_AD_16)")
    public ApiResponse<StudentResponse> updateStudent(@PathVariable Long id, @RequestBody @Valid StudentRequest request) {
        return ApiResponse.<StudentResponse>builder()
                .result(studentService.updateStudent(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa sinh viên")
    public ApiResponse<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ApiResponse.<String>builder()
                .result("Sinh viên đã được xóa thành công")
                .build();
    }
}
